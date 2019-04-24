/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.dsc.lcdpr.enumerated;

/**
 * Tipo de Documento:
 * 1 - Nota Fiscal
 * 2 - Fatura
 * 3 - Recibo
 * 4 - Contrato
 * 5 - Folha de Pagamento
 * 6 - Outros
 * 
 * @author Tiago
 */
public enum TIPO_DOCUMENTO {
    
    NOTA_FISCAL(1), FATURA(2), RECIBO(3), CONTRATO(4), FOLHA_PAGAMENTO(5), OUTROS(6);
    
    private final int valor;

    private TIPO_DOCUMENTO(int valor) {
        this.valor = valor;
    }

    public int getValor() {
        return valor;
    }
    
}
