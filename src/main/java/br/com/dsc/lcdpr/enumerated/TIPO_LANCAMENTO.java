/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.dsc.lcdpr.enumerated;

/**
 * Tipo de Lançamento;
 * 1 - Receita da Atividade Rural
 * 2 - Despesas de custeio e investimentos
 * 3 - Despesas não dedutíveis
 * 4 - Produtos entregues no ano referente a adiantamentos de recursos financeiros 
 * 5 - Adiantamentos recebidos por conta de vendas para entregas futuras
 * 
 * @author Tiago
 */
public enum TIPO_LANCAMENTO {
    
    RECEITA_ATV_RURAL(1), DESP_CUSTEIO_INVTM(2), DESP_DEDUTIVEIS(3), PRODUTOS_ENTREGUES(4), ADIANTAMENTOS_RECEBIDOS(5);
    
    private final int valor;

    private TIPO_LANCAMENTO(int valor) {
        this.valor = valor;
    }

    public int getValor() {
        return valor;
    }
    
}
