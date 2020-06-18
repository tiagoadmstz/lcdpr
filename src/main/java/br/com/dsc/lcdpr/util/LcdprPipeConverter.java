/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.dsc.lcdpr.util;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static br.com.dsc.lcdpr.util.ExceptionUtil.tryCatch;

/**
 * @author Tiago D.
 */
public abstract class LcdprPipeConverter {

    public static String convertToLcdprPattern(Object object) {
        if (object == null) return "|";
        if (object.getClass() == LocalDate.class) {
            object = ((LocalDate) object).format(DateTimeFormatter.ofPattern("ddMMyyyy"));
        } else if (object.getClass().isEnum()) {
            object = tryCatch(object, obj -> obj.getClass().getMethod("getValor").invoke(obj));
        } else if (object.getClass() == BigDecimal.class) {
            object = ((BigDecimal) object).setScale(2).toPlainString().replaceAll("[.,]*", "");
        }
        return String.valueOf(object).concat("|");
    }

}
