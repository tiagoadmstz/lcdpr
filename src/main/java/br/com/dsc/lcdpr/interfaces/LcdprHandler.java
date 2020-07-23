/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.dsc.lcdpr.interfaces;

import br.com.dsc.lcdpr.util.LcdprPipeConverter;

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
        return LcdprPipeConverter.generatePipeText(this);
    }

    default boolean validate() {
        return LcdprPipeConverter.validateByAnnotation(this);
    }

}
