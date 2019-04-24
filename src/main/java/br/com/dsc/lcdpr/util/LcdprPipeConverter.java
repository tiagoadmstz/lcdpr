/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.dsc.lcdpr.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 *
 * @author Tiago
 */
public abstract class LcdprPipeConverter {

    public static String convertToLcdprPattern(Object object) {
        if (object == null) {
            return "|";
        }
        if (object.getClass() == LocalDate.class) {
            object = ((LocalDate) object).format(DateTimeFormatter.ofPattern("ddMMyyyy"));
        } else if (object.getClass().getSuperclass() == Enum.class) {
            try {
                object = object.getClass().getMethod("getValor").invoke(object);
            } catch (Exception e) {
            }
        }
        return String.valueOf(object).concat("|");
    }

}
