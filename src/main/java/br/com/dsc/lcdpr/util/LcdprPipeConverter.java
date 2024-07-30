package br.com.dsc.lcdpr.util;

import br.com.dsc.lcdpr.annotations.Validation;
import br.com.dsc.lcdpr.components.CadastroTerceiro;
import br.com.dsc.lcdpr.components.ImovelRural;
import br.com.dsc.lcdpr.lcdpr.Lcdpr;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.AbstractList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.StringJoiner;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static br.com.dsc.lcdpr.util.ExceptionUtil.tryCatch;

/**
 * This class have methods to convert Java classes to String values separated by pipe
 *
 * @author Tiago D.
 */
public abstract class LcdprPipeConverter {

    /**
     * Cast an object to String separated by pipe
     *
     * @param object value to convert
     * @return String with converted value
     */
    public static String convertToLcdprPattern(final Object object) {
        final Object newObject;
        if (Objects.isNull(object)) return "|";
        if (object.getClass() == LocalDate.class) {
            newObject = ((LocalDate) object).format(DateTimeFormatter.ofPattern("ddMMyyyy"));
        } else if (object.getClass().isEnum()) {
            newObject = tryCatch(object, obj -> obj.getClass().getMethod("getValor").invoke(obj));
        } else if (object.getClass() == BigDecimal.class) {
            newObject = BigDecimalUtil.bigDecimalToStringAndRemovePuncts((BigDecimal) object);
        } else {
            newObject = object;
        }
        return String.valueOf(newObject).concat("|");
    }

    private static List<Field> getFields(final Class<?> clazz) {
        return ExceptionUtil.tryCatch(null, ef ->
                Arrays.stream(clazz.getDeclaredFields())
                        .filter(f -> !"serialversionuid".equalsIgnoreCase(f.getName()))
                        .collect(Collectors.toList())
        );
    }

    private static Object getFieldValue(final Object object, final Field field) {
        return tryCatch(null, fct -> {
            field.setAccessible(true);
            return field.get(object);
        });
    }

    private static String executeMethodRecursively(final Object object, final String methodName) {
        try {
            String rst = "";
            if (object.getClass().getSuperclass() == AbstractList.class) {
                for (Object item : ((List) object)) {
                    if (item instanceof ImovelRural) {
                        rst += splitImoveisRuraisTerceiros(item);
                    } else {
                        rst += String.valueOf(item.getClass().getMethod(methodName).invoke(item));
                    }
                }
            } else {
                rst = String.valueOf(object.getClass().getMethod(methodName).invoke(object));
            }
            return rst;
        } catch (Exception ex) {
        }
        return LcdprPipeConverter.convertToLcdprPattern(object);
    }

    private static String splitImoveisRuraisTerceiros(final Object object) {
        final ImovelRural imovelRural = (ImovelRural) object;
        final Optional<List<CadastroTerceiro>> cadastroTerceiros = Optional.ofNullable(imovelRural.getCadastroTerceiros());
        imovelRural.setCadastroTerceiros(null);
        final StringJoiner result = new StringJoiner("\r\n");
        result.add(generatePipeText(imovelRural).replace("|\r\n", ""));
        for (final CadastroTerceiro cadastroTerceiro : cadastroTerceiros.orElse(Collections.emptyList())) {
            result.add(generatePipeText(cadastroTerceiro));
        }
        return result.toString();
    }

    public static String generatePipeText(final Object object) {
        final String result = getFields(filterSubClasses(object)).stream()
                .map(f -> executeMethodRecursively(getFieldValue(object, f), "generatePipeText"))
                .reduce("", String::concat);
        return concatLcdprSufix(result);
    }

    private static String concatLcdprSufix(final String line) {
        if (line.isEmpty())
            return line;
        final String newline = line.lastIndexOf("|") == (line.length() - 1) ? line.substring(0, line.length() - 1) : line;
        if (!newline.contains("\r\n"))
            return newline.concat("\r\n");
        return newline;
    }

    public static Class<?> filterSubClasses(final Object object) {
        if (Objects.nonNull(object.getClass().getSuperclass()) && object.getClass().getSuperclass() == Lcdpr.class) {
            return object.getClass().getSuperclass();
        }
        return object.getClass();
    }

    public static boolean validateByAnnotation(final Object object) {
        final List<Boolean> collect = getFields(object.getClass()).stream().map(f -> {
            try {
                final Object value = getFieldValue(object, f);
                Validation validation = f.getAnnotation(Validation.class);
                if (Objects.equals('N', validation.required())) {
                    return true;
                }
                if (Objects.equals('S', validation.required()) && (Objects.isNull(value) || StringUtils.isBlank(value.toString()))) {
                    throw new NullPointerException();
                }
                if (Objects.equals('C', validation.type())) {
                    return Pattern.matches("[^\\p{Digit}]+", value.toString());
                } else if (Objects.equals('N', validation.type())) {
                    if (value instanceof String) {
                        return Pattern.matches("[\\d]+", value.toString());
                    } else if (value instanceof LocalDate) {
                        final String formattedDate = ((LocalDate) value).format(DateTimeFormatter.ofPattern("ddMMyyyy"));
                        return Pattern.matches("[\\d]+", formattedDate);
                    } else if (value instanceof Enum) {
                        return Pattern.matches("[\\d]+", value.getClass().getMethod("getValor").invoke(value).toString());
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return false;
        }).collect(Collectors.toList());
        return collect.stream().noneMatch(o -> !o);
    }
}
