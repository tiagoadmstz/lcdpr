package br.com.dsc.lcdpr.util;

import java.text.Normalizer;
import java.util.Arrays;
import java.util.stream.Collectors;

public abstract class WordUtils {

    /**
     * Makes the first letter of each word capitalized
     * Ex: capitalize("the first letter is uppercase") = "The First Letter is Uppercase"
     *
     * @param text
     * @return String result
     */
    public static String capitalize(String text) {
        return Arrays.stream(text.toLowerCase().split(" "))
                .map(p -> p.length() > 2 ? p.replaceFirst("\\w{1}", firstLetter(p).toUpperCase()) : p
                ).collect(Collectors.joining(" "))
                .trim();
    }

    /**
     * Fill in the left
     * Ex: lpad("1", 3, "0") = "001"
     * lpad("1", 4, "X") = "XXX1"
     *
     * @param text
     * @param size
     * @param filler
     * @return
     */
    public static String lpad(String text, int size, String filler) {
        return ExceptionUtil.tryCatch(text, "", t -> {
            String txt = (String) t;
            if (txt.length() < size) {
                String p = "";
                for (int i = 0; i < (size - txt.length()); i++) {
                    p = p.concat(filler);
                }
                txt = p.concat(txt);
            }
            return txt;
        });
    }

    /**
     * Removes all accentuations from the text
     *
     * @param text
     * @return String normalized
     */
    public static String removeAccentuarion(String text) {
        return ExceptionUtil.tryCatch(text, text, t -> Normalizer.normalize((String) t, Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", ""));
    }

    /**
     * Inverts case for first letter in paragraph
     *
     * @param string text
     * @return String result
     */
    public static String toogleFirstLetterCase(String string) {
        return string.replaceFirst(firstLetter(string), isUpperCase(string) ? firstLetter(string).toLowerCase() : firstLetter(string).toUpperCase());
    }

    /**
     * Gets first letter from the paragraph
     *
     * @param string text
     * @return String first letter
     */
    public static String firstLetter(String string) {
        return string.substring(0, 1);
    }

    /**
     * Verifies if text is in upper case
     *
     * @param string text
     * @return true if it is in upper case
     */
    public static boolean isUpperCase(String string) {
        return string.equals(string.toUpperCase());
    }

    /**
     * Verifies if text is in lower case
     *
     * @param string text
     * @return true if it is in lower case
     */
    public static boolean isLowerCase(String string) {
        return !isUpperCase(string);
    }

}
