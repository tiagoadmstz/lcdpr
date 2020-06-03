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
public class Contribuinte implements Serializable, LcdprHandler {

    private static final long serialVersionUID = 8869191708501012066L;
    @Builder.Default
    private String reg = "0030"; //t = 4, o = sim, p = 0030
    private String endereco; //t = 150, o = sim
    private String numero; //t = 6, o = sim
    private String complemento; //t = 50, o = não
    private String bairro; //t = 50, o = sim
    private String uf; //t = 2, o = sim
    private String codigo_municipio; //t = 7, o = sim
    private String cep; //t = 8, o = sim
    private Long numero_telefone; //t = 15, o = não
    private String email; //t = 115, o = sim

}
