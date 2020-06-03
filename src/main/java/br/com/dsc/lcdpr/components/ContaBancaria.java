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
 * @author Tiago
 */
@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class ContaBancaria implements Serializable, LcdprHandler {

    private static final long serialVersionUID = -3013153228791071332L;
    @Builder.Default
    private String reg = "0050"; // t = 4, o = sim, p = 0050
    private String cod_conta; // t = 3, o = sim
    private String pais_cta; // t = 3, o = sim
    private String banco; // t = 3, o = sim(BRA)
    private String nome_banco; // t = 30, o = sim
    private String agencia; // t = 4, o = sim
    private String numero_conta; // t = 16, o = sim

}
