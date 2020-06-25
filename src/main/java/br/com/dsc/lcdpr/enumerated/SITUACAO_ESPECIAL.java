/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.dsc.lcdpr.enumerated;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Indicador de Situação Especial e Outros Eventos:
 * 0 – Normal; (Sem ocorrência de situação especial ou evento)
 * 1 – Falecimento;
 * 2 - Espólio;
 * 3 - Saída definitiva do País.
 *
 * @author Tiago
 */
public enum SITUACAO_ESPECIAL {

    NORMAL("0"), FALECIMENTO("1"), ESPOLIO("2"), SAIDA_DEFINITIVA_PAIS("3");

    private final String valor;

    SITUACAO_ESPECIAL(String valor) {
        this.valor = valor;
    }

    @JsonValue
    public String getValor() {
        return valor;
    }

    public static SITUACAO_ESPECIAL getEnum(int valor) {
        switch (valor) {
            default:
            case 0:
                return NORMAL;
            case 1:
                return FALECIMENTO;
            case 2:
                return ESPOLIO;
            case 3:
                return SAIDA_DEFINITIVA_PAIS;
        }
    }

    @JsonCreator
    public static SITUACAO_ESPECIAL getEnum(String valor) {
        if (valor != null) {
            return getEnum(Integer.parseInt(valor));
        }
        return NORMAL;
    }

}
