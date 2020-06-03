/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.dsc.lcdpr.components;

import br.com.dsc.lcdpr.interfaces.LcdprHandler;
import lombok.*;

import java.io.Serializable;

/**
 * @author Tiago Q200
 */
@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class ResumoDemoLivroCaixa implements Serializable, LcdprHandler {

    private static final long serialVersionUID = -268680092317624306L;
    @Builder.Default
    private String reg = "Q200"; // t = 4, o = sim, p = Q200
    private String pais; // t = 3, o = sim
    private String mes; // t = 6, o = sim
    private String valor_entrada; // t = 19d2, o = nao
    private String valor_saida; // t = 19d2, o = nao
    private String saldo_final; // t = 19d2, o = nao

}
