/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.dsc.lcdpr.components;

import br.com.dsc.lcdpr.enumerated.TIPO_CONTRAPARTE;
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
public class ExploracaoMedianteContrato implements Serializable, LcdprHandler {

    private static final long serialVersionUID = -784279936625837037L;
    @Builder.Default
    private String reg = "0045"; //t = 4, o = sim, p = 0045
    private String cod_imovel; //t = 3, o = sim
    private TIPO_CONTRAPARTE tipo_contraparte; //t = 1, o = sim
    private String cpf_contraparte; //t = 11, o = sim
    private String nome_contraparte; //t = 50, o = sim
    private String percentual_contraparte; //t = 3, o = sim

}
