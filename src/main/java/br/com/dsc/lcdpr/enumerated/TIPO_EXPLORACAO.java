/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.dsc.lcdpr.enumerated;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * TIPO_EXPLORAÇÃO "Tipo de Exploração do Imóvel:
 * 1 - Exploração individual (Imóvel próprio)
 * 2 - Condomínio
 * 3 - Imóvel arrendado
 * 4 - Parceria
 * 5 - Comodato
 * 6 - Outro
 *
 * @author Tiago
 */
public enum TIPO_EXPLORACAO {

    IMOVEL_PROPRIO(1),
    CONDOMINIO(2),
    IMOVEL_ARRENDADO(3),
    PARCERIA(4),
    COMODATO(5),
    OUTROS(6);

    private final int valor;

    TIPO_EXPLORACAO(int valor) {
        this.valor = valor;
    }

    @JsonValue
    public int getValor() {
        return valor;
    }

    @JsonCreator
    public static TIPO_EXPLORACAO getEnum(int valor) {
        switch (valor) {
            case 1:
            default:
                return IMOVEL_PROPRIO;
            case 2:
                return CONDOMINIO;
            case 3:
                return IMOVEL_ARRENDADO;
            case 4:
                return PARCERIA;
            case 5:
                return COMODATO;
            case 6:
                return OUTROS;
        }
    }

    public static String getDescription(int valor) {
        switch (valor) {
            case 1:
            default:
                return "Imóvel Próprio";
            case 2:
                return "Condomínio";
            case 3:
                return "Imóvel Arrendado";
            case 4:
                return "Parceria";
            case 5:
                return "Comodato";
            case 6:
                return "Outros";
        }
    }

}
