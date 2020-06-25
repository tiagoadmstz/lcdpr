/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.dsc.lcdpr.enumerated;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Forma de Apuração:
 * 1 – Livro Caixa.
 * 2 – Apuração do lucro pelo disposto no art. 5º da Lei nº 8.023, de 1990
 * A forma de apuração do resultado da atividade rural adotada, indica que a pessoa física apurou o resultado com base no:
 * 1) Livro Caixa: Apurou o resultado da atividade rural com base nos lançamentos registrados no Livro Caixa do Produtor Rural.
 * 2) Apurou o resultado da atividade rural com base na faculdade disposta no art. 5º da Lei nº 8.023, de 1990 (20% da Receita Bruta)
 *
 * @author Tiago D.
 */
public enum FORMA_APURACAO {

    LIVRO_CAIXA(1), APURACAO_LUCRO_8023(2);

    private final int valor;

    FORMA_APURACAO(int valor) {
        this.valor = valor;
    }

    @JsonValue
    public int getValor() {
        return valor;
    }

    @JsonCreator
    public static FORMA_APURACAO getEnum(int valor) {
        switch (valor) {
            default:
            case 1:
                return LIVRO_CAIXA;
            case 2:
                return APURACAO_LUCRO_8023;
        }
    }

    public static FORMA_APURACAO getEnum(String valor) {
        if (valor != null) {
            return getEnum(Integer.parseInt(valor));
        }
        return LIVRO_CAIXA;
    }

}
