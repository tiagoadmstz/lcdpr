/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.dsc.lcdpr.enumerated;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * TIPO_EXPLORAÇÃO "Tipo de Exploração do Imóvel:
 * 1 - Exploração individual (Imóvel próprio)
 * 2 - Condomínio
 * 3 - Imóvel arrendado
 * 4 - Parceria
 * 5 - Comodato
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

}
