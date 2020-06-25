/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.dsc.lcdpr.enumerated;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Indicador do Início do Período:
 * 0 – Regular (Início no primeiro dia do ano).
 * 1 – Abertura (Início de atividades no ano-calendário).
 * 2 – Início de obrigatoriedade da escrituração no curso do ano calendário. (Exemplo:
 * desenquadramento como imune ou isento do IRPF)
 *
 * @author Tiago
 */
public enum INICIO_PERIODO {

    REGULAR(0), ABERTURA(1), OBRIGATORIO_ESCRITURACAO(2);

    private final int valor;

    INICIO_PERIODO(int valor) {
        this.valor = valor;
    }

    @JsonValue
    public int getValor() {
        return valor;
    }

    @JsonCreator
    public static INICIO_PERIODO getEnum(int valor) {
        switch (valor) {
            default:
            case 0:
                return REGULAR;
            case 1:
                return ABERTURA;
            case 2:
                return OBRIGATORIO_ESCRITURACAO;
        }
    }

    public static INICIO_PERIODO getEnum(String valor) {
        if (valor != null) {
            return getEnum(Integer.parseInt(valor));
        }
        return REGULAR;
    }

}
