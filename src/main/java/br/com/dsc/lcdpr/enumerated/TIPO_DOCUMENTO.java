/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.dsc.lcdpr.enumerated;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

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

    NOTA_FISCAL(1),
    FATURA(2),
    RECIBO(3),
    CONTRATO(4),
    FOLHA_PAGAMENTO(5),
    OUTROS(6);

    private final int valor;

    TIPO_DOCUMENTO(int valor) {
        this.valor = valor;
    }

    @JsonValue
    public int getValor() {
        return valor;
    }

    @JsonCreator
    public static TIPO_DOCUMENTO getEnum(int valor) {
        switch (valor) {
            default:
            case 1:
                return NOTA_FISCAL;
            case 2:
                return FATURA;
            case 3:
                return RECIBO;
            case 4:
                return CONTRATO;
            case 5:
                return FOLHA_PAGAMENTO;
            case 6:
                return OUTROS;
        }
    }

    public static TIPO_DOCUMENTO getEnum(String valor) {
        if (valor != null) {
            return getEnum(Integer.parseInt(valor));
        }
        return NOTA_FISCAL;
    }

}
