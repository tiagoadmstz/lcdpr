package br.com.dsc.lcdpr.util;

import java.math.BigDecimal;

public abstract class BigDecimalUtil {

    /**
     * Casts BigDecimal to String and remove punctuation
     * Ex: bigDecimalToStringAndRemovePuncts(1,000.00) = "100000"
     *
     * @param value BigDecimal
     * @return String value without punctuation
     */
    public static String bigDecimalToStringAndRemovePuncts(BigDecimal value) {
        return value.setScale(2).toPlainString().replaceAll("[.,]*", "");
    }

    /**
     * Casts String to BigDecimal
     * Ex: stringToBigDecimal("100000", 0) = 1,00000.00
     * stringToBigDecimal("100000", 2) = 1,000.00
     *
     * @return BigDecimal
     */
    public static BigDecimal stringToBigDecimal(String value, int scale) {
        return new BigDecimal(value.substring(0, value.length() - scale)
                + "." + value.substring(value.length() - 2));
    }

}
