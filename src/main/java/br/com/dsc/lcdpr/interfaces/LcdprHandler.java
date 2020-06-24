/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.dsc.lcdpr.interfaces;

import br.com.dsc.lcdpr.util.LcdprPipeConverter;

import java.lang.reflect.Field;
import java.util.AbstractList;
import java.util.Arrays;
import java.util.List;

import static br.com.dsc.lcdpr.util.ExceptionUtil.tryCatch;

/**
 * Handler for LCDPR classes
 *
 * @author Tiago D.
 */
public interface LcdprHandler {

    /**
     * Generates piped text from the root class recursively
     *
     * @return String with converted values
     */
    default String generatePipeText() {
        String result = "";
        try {
            Field[] fields = getClass().getDeclaredFields();
            result = Arrays.asList(fields).stream()
                    .filter(f -> !"serialversionuid".equalsIgnoreCase(f.getName()))
                    .map(f -> {
                        f.setAccessible(true);
                        Object ob = tryCatch(null, fct -> f.get(this));
                        try {
                            String rst = "";
                            if (ob.getClass().getSuperclass() == AbstractList.class) {
                                for (Object item : ((List) ob)) {
                                    rst += String.valueOf(item.getClass().getMethod("generatePipeText").invoke(item));
                                }
                            } else {
                                rst = String.valueOf(ob.getClass().getMethod("generatePipeText").invoke(ob));
                            }
                            return rst;
                        } catch (Exception ex) {
                        }
                        return LcdprPipeConverter.convertToLcdprPattern(ob);
                    })
                    .reduce("", (str1, str2) -> str1.concat(str2));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return result.length() > 0 ? result.substring(0, result.length() - 1).concat("\n") : result;
    }

}
