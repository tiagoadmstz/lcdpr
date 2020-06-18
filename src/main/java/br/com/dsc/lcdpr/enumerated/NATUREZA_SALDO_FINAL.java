package br.com.dsc.lcdpr.enumerated;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Natureza do Saldo Final (Negativo ou Positivo)
 *
 * @author Tiago D.
 */
public enum NATUREZA_SALDO_FINAL {

    NEGATIVO("N"),
    POSITIVO("P");

    private String valor;

    NATUREZA_SALDO_FINAL(String valor) {
        this.valor = valor;
    }

    @JsonValue
    public String getValor() {
        return valor;
    }

}