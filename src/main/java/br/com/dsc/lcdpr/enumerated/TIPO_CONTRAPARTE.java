/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.dsc.lcdpr.enumerated;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Tipo de terceiro relacionado ao imóvel:
 * 1 - Condomino
 * 2 - Arrendante
 * 3 - Parceiro
 * 4 - Comodatário
 *
 * @author Tiago
 */
public enum TIPO_CONTRAPARTE {

    CONDOMINO("1"),
    ARRENDADOR("2"),
    PARCEIRO("3"),
    COMODANTE("4"),
    OUTRO("5");

    private final String valor;

    TIPO_CONTRAPARTE(String valor) {
        this.valor = valor;
    }

    @JsonValue
    public String getValor() {
        return valor;
    }

}
