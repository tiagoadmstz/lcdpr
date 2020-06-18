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
 * @author Tiago D.
 */
public interface LcdprHandler {

    default String generatedPipeText() {
        String result = "";
        try {
            Field[] fields = getClass().getDeclaredFields();
            result = Arrays.asList(fields).stream()
                    .filter(f -> !"serialversionuid".equalsIgnoreCase(f.getName()))
                    .map(f -> {
                        f.setAccessible(true);
                        String rst = "";
                        Object ob = tryCatch(null, fct -> f.get(this));
                        if (ob != null) {
                            try {
                                if (ob.getClass().getSuperclass() == AbstractList.class) {
                                    for (Object item : ((List) ob)) {
                                        rst += String.valueOf(item.getClass().getMethod("generatedPipeText").invoke(item));
                                    }
                                } else {
                                    rst = String.valueOf(ob.getClass().getMethod("generatedPipeText").invoke(ob));
                                }
                                return rst;
                            } catch (Exception e) {
                            }
                            return LcdprPipeConverter.convertToLcdprPattern(ob);
                        }
                        return rst;
                    })
                    .reduce("", (str1, str2) -> str1.concat(str2));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return result.length() > 0 ? result.substring(0, result.length() - 1).concat("\n") : result;
    }

}
