/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.dsc.lcdpr.enumerated;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Tipo de Lançamento;
 * 1 - Receita da Atividade Rural
 * 2 - Despesas de custeio e investimentos
 * 3 - Receita de produtos entregues no ano referente a adiantamento de recursos financeiros
 *
 * @author Tiago
 */
public enum TIPO_LANCAMENTO {

    RECEITA_ATIVIDADE_RURAL(1),
    DESPESAS_CUSTEIO_INVESTIMENTOS(2),
    RECEITA_PRODUTOS_ENTREGUES(3);

    private final int valor;

    TIPO_LANCAMENTO(int valor) {
        this.valor = valor;
    }

    @JsonValue
    public int getValor() {
        return valor;
    }

}
