/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.dsc.lcdpr.blocos;

import br.com.dsc.lcdpr.interfaces.LcdprHandler;
import lombok.*;

import java.io.Serializable;

/**
 * 9 - Identifica o Contador e encerra o arquivo digital.
 *
 * @author Tiago D.
 */
@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class EncerramentoArquivo implements Serializable, LcdprHandler {

    private static final long serialVersionUID = 136224468484970674L;
    @Builder.Default
    private String reg = "9999"; // t = 4, o = sim, p = 9999
    private Integer quantidade_linhas; // t = sem limite, o = sim

}
