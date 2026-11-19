package io.github.tiagoadmstz.lcdpr;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.function.Function;

/**
 * Formatação e interpretação dos valores dos campos do leiaute: modelo para texto e texto para modelo.
 */
final class Formatos {

    static final String SEPARADOR = "|";
    static final String FIM_DE_LINHA = "\r\n";
    static final DateTimeFormatter FORMATO_DATA = DateTimeFormatter.ofPattern("ddMMuuuu")
            .withResolverStyle(ResolverStyle.STRICT);
    static final DateTimeFormatter FORMATO_MES = DateTimeFormatter.ofPattern("MMuuuu")
            .withResolverStyle(ResolverStyle.STRICT);

    private static final int CASAS_DECIMAIS = 2;
    private static final int DIGITOS_MINIMOS_VALOR = 3;
    private static final int DIGITOS_PERCENTUAL = 5;

    private Formatos() {
    }

    /** Texto do campo; nulo vira vazio. Lança exceção se houver "|", CR ou LF. */
    static String texto(String valor) {
        if (valor == null) {
            return "";
        }
        if (valor.contains(SEPARADOR) || valor.contains("\r") || valor.contains("\n")) {
            throw new LcdprException("texto do campo contém '|', CR ou LF");
        }
        return valor;
    }

    /** Campo de texto lido; vazio vira nulo. */
    static String lerTexto(String campo) {
        if (campo.isEmpty()) {
            return null;
        }
        return campo;
    }

    /** Data no formato ddMMaaaa; nula vira vazio. */
    static String data(LocalDate valor) {
        if (valor == null) {
            return "";
        }
        return valor.format(FORMATO_DATA);
    }

    /** Data lida no formato ddMMaaaa estrito; vazio vira nulo. */
    static LocalDate lerData(String campo) {
        if (campo.isEmpty()) {
            return null;
        }
        try {
            return LocalDate.parse(campo, FORMATO_DATA);
        } catch (DateTimeParseException e) {
            throw new LcdprException("data inválida no formato ddMMaaaa", e);
        }
    }

    /** Mês no formato MMaaaa; nulo vira vazio. */
    static String mes(YearMonth valor) {
        if (valor == null) {
            return "";
        }
        return valor.format(FORMATO_MES);
    }

    /** Mês lido no formato MMaaaa estrito; vazio vira nulo. */
    static YearMonth lerMes(String campo) {
        if (campo.isEmpty()) {
            return null;
        }
        try {
            return YearMonth.parse(campo, FORMATO_MES);
        } catch (DateTimeParseException e) {
            throw new LcdprException("mês inválido no formato MMaaaa", e);
        }
    }

    /** Valor monetário: duas casas implícitas, zeros à esquerda até três dígitos; nulo vira vazio. */
    static String valor(BigDecimal valor) {
        if (valor == null) {
            return "";
        }
        return completar(digitosDecimais(valor), DIGITOS_MINIMOS_VALOR);
    }

    /** Valor monetário lido; vazio vira nulo. */
    static BigDecimal lerValor(String campo) {
        return lerDecimal(campo);
    }

    /** Percentual: duas casas implícitas, cinco dígitos no total; nulo vira vazio. */
    static String percentual(BigDecimal valor) {
        if (valor == null) {
            return "";
        }
        String digitos = digitosDecimais(valor);
        if (digitos.length() > DIGITOS_PERCENTUAL) {
            throw new LcdprException("percentual com mais de 5 dígitos");
        }
        return completar(digitos, DIGITOS_PERCENTUAL);
    }

    /** Percentual lido; vazio vira nulo. */
    static BigDecimal lerPercentual(String campo) {
        return lerDecimal(campo);
    }

    /** Código de uma tabela; nulo vira vazio. */
    static <E> String codigo(E valor, Function<E, String> extrair) {
        if (valor == null) {
            return "";
        }
        return extrair.apply(valor);
    }

    /** Constante de uma tabela pelo código; vazio vira nulo e código inválido vira exceção. */
    static <E> E lerEnum(String campo, Function<String, E> fromCodigo) {
        if (campo.isEmpty()) {
            return null;
        }
        try {
            return fromCodigo.apply(campo);
        } catch (IllegalArgumentException e) {
            throw new LcdprException(e.getMessage(), e);
        }
    }

    private static String digitosDecimais(BigDecimal valor) {
        BigDecimal arredondado = valor.setScale(CASAS_DECIMAIS, RoundingMode.HALF_UP);
        if (arredondado.signum() < 0) {
            throw new LcdprException("valor negativo não é permitido no LCDPR");
        }
        return arredondado.unscaledValue().toString();
    }

    private static BigDecimal lerDecimal(String campo) {
        if (campo.isEmpty()) {
            return null;
        }
        if (!campo.matches("[0-9]+")) {
            throw new LcdprException("valor numérico com caractere que não é dígito");
        }
        return new BigDecimal(new BigInteger(campo), CASAS_DECIMAIS);
    }

    private static String completar(String digitos, int tamanho) {
        StringBuilder completo = new StringBuilder();
        for (int i = digitos.length(); i < tamanho; i++) {
            completo.append('0');
        }
        return completo.append(digitos).toString();
    }
}
