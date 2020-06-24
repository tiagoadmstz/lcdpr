package br.com.dsc.lcdpr.util;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public abstract class WordUtils {

    public static String capitalize(String text) {
        return Arrays.stream(text.toLowerCase().split(" "))
                .map(p -> p.length() > 2 ? p.replaceFirst("\\w{1}", firstLetter(p).toUpperCase()) : p
                ).collect(Collectors.joining(" "))
                .trim();
    }

    /**
     * Preencher a esquerda
     *
     * @param texto
     * @param tamanho
     * @param preenchedor
     * @return
     */
    public static String lpad(String texto, int tamanho, String preenchedor) {
        try {
            if (texto.length() < tamanho) {
                String p = "";
                for (int i = 0; i < (tamanho - texto.length()); i++) {
                    p = p.concat(preenchedor);
                }
                texto = p.concat(texto);
            }
            return texto;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String removerAscentuacao(String text) {
        try {
            //Pattern p = Pattern.compile("[ÁÀÃÂÄáàãâäÉÈÊËéèêëÍÌÎÏíìîïÓÒÕÔÖóòôõöÚÙÛÜúùûü]");
            //Matcher m = p.matcher(text);
            Map<String, String> letras = new HashMap();
            letras.put("[ÁÀÃÂÄ]", "A");
            letras.put("[áàãâä]", "a");
            letras.put("[ÉÈÊË]", "E");
            letras.put("[éèêë]", "e");
            letras.put("[ÍÌÎÏ]", "I");
            letras.put("[íìîï]", "i");
            letras.put("[ÓÒÕÔÖ]", "O");
            letras.put("[óòôõö]", "o");
            letras.put("[ÚÙÛÜ]", "U");
            letras.put("[úùûü]", "u");
            for (Map.Entry<String, String> entry : letras.entrySet()) {
                text = text.replaceAll(entry.getKey(), entry.getValue());
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return text;
    }

    public static String toogleFirstLetterCase(String string) {
        return string.replaceFirst(firstLetter(string), isUpperCase(string) ? firstLetter(string).toLowerCase() : firstLetter(string).toUpperCase());
    }

    public static String firstLetter(String string) {
        return string.substring(0, 1);
    }

    public static boolean isUpperCase(String string) {
        return string.equals(string.toUpperCase());
    }

    public static boolean isLowerCase(String string) {
        return !isUpperCase(string);
    }

}
