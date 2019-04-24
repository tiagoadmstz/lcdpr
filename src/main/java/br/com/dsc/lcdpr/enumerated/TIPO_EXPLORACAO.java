/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.dsc.lcdpr.enumerated;

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
    
    IMOVEL_PROPRIO(1), CONDOMINIO(2), IMOVEL_ARRENDADO(3), PARCERIA(4), COMODATO(5);
    
    private final int valor;

    private TIPO_EXPLORACAO(int valor) {
        this.valor = valor;
    }

    public int getValor() {
        return valor;
    }
    
}
