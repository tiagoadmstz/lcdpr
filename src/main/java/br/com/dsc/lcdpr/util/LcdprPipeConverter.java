/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.dsc.lcdpr.util;

import br.com.dev.engine.date.Datas;
import br.com.dsc.lcdpr.annotations.Validation;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.AbstractList;
import java.util.Arrays;
import java.util.List;
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
    public static String convertToLcdprPattern(Object object) {
        if (object == null) return "|";
        if (object.getClass() == LocalDate.class) {
            object = ((LocalDate) object).format(DateTimeFormatter.ofPattern("ddMMyyyy"));
        } else if (object.getClass().isEnum()) {
            object = tryCatch(object, obj -> obj.getClass().getMethod("getValor").invoke(obj));
        } else if (object.getClass() == BigDecimal.class) {
            object = BigDecimalUtil.bigDecimalToStringAndRemovePuncts((BigDecimal) object);
        }
        return String.valueOf(object).concat("|");
    }

    private static List<Field> getFields(Class<?> clazz) {
        return ExceptionUtil.tryCatch(null, ef ->
                Arrays.asList(clazz.getDeclaredFields()).stream()
                        .filter(f -> !"serialversionuid".equalsIgnoreCase(f.getName()))
                        .collect(Collectors.toList())
        );
    }

    private static Object getFieldValue(Object object, Field field) {
        return tryCatch(null, fct -> {
            field.setAccessible(true);
            return field.get(object);
        });
    }

    private static String executeMethodRecursively(Object object, String methodName) {
        try {
            String rst = "";
            if (object.getClass().getSuperclass() == AbstractList.class) {
                for (Object item : ((List) object)) {
                    rst += String.valueOf(item.getClass().getMethod(methodName).invoke(item));
                }
            } else {
                rst = String.valueOf(object.getClass().getMethod(methodName).invoke(object));
            }
            return rst;
        } catch (Exception ex) {
        }
        return LcdprPipeConverter.convertToLcdprPattern(object);
    }

    public static String generatePipeText(Object object) {
        String result = getFields(object.getClass()).stream()
                .map(f -> executeMethodRecursively(getFieldValue(object, f), "generatePipeText"))
                .reduce("", String::concat);
        return result.length() > 0 ? result.substring(0, result.length() - 1).concat("\n") : result;
    }

    public static boolean validateByAnnotation(Object object) {
        List<Boolean> collect = getFields(object.getClass()).stream().map(f -> {
            try {
                Object value = getFieldValue(object, f);
                Validation validation = f.getAnnotation(Validation.class);
                if ('S' == validation.required()) {
                    if (value == null || "".equals(value)) {
                        throw new NullPointerException();
                    }
                }
                if ('C' == validation.type()) {
                    return Pattern.matches("[\\p{Alpha}]", (String) value);
                } else if ('N' == validation.type()) {
                    if (value instanceof LocalDate) {
                        return Pattern.matches("[\\d]", Datas.getDateString((LocalDate) value));
                    } else if (value instanceof Enum) {
                        return Pattern.matches("[\\d]", value.getClass().getMethod("getValor").invoke(value).toString());
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
