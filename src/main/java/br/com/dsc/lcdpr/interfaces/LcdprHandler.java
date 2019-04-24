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

/**
 *
 * @author Tiago
 */
public interface LcdprHandler {

    public default String generatedPipeText() {
        String result = "";
        try {
            Field[] fields = getClass().getDeclaredFields();
            result = Arrays.asList(fields).stream()
                    .filter(f -> !f.getName().equals("serialVersionUID"))
                    .map(f -> {
                        f.setAccessible(true);
                        Object ob = null;
                        try {
                            ob = f.get(this);
                        } catch (Exception e) {
                        }
                        if (ob != null) {
                            try {
                                //System.out.println(ob.getClass().toGenericString());
                                //System.out.println(ob.getClass().getSuperclass().toGenericString());
                                if (ob.getClass().getSuperclass() == AbstractList.class) {
                                    for (Object item : ((List) ob)) {
                                        ob = String.valueOf(item.getClass().getMethod("generatedPipeText").invoke(item));
                                    }
                                } else {
                                    return String.valueOf(ob.getClass().getMethod("generatedPipeText").invoke(ob));
                                }
                                return String.valueOf(ob);
                            } catch (Exception e) {
                            }
                            return LcdprPipeConverter.convertToLcdprPattern(ob);
                        }
                        return "";
                    })
                    .reduce("", (str1, str2) -> str1.concat(str2));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return result.length() > 0 ? result.substring(0, result.length() - 1).concat("\n") : result;
    }

}
