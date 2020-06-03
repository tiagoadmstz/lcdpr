/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.dsc.lcdpr.components;

import br.com.dsc.lcdpr.enumerated.TIPO_DOCUMENTO;
import br.com.dsc.lcdpr.enumerated.TIPO_LANCAMENTO;
import br.com.dsc.lcdpr.interfaces.LcdprHandler;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * @author Tiago
 */
@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class DemoLivroCaixa implements Serializable, LcdprHandler {

    private static final long serialVersionUID = 3804635706937859453L;
    @Builder.Default
    private String reg = "Q100"; //t = 4, o = sim, p = Q100
    private LocalDate data; //t = 8, o = sim
    private String cod_imovel; //t = 3, o = sim
    private String cod_conta; //t = 3, o = sim
    private String numero_documento; //t = sem limite, o = nao
    private TIPO_DOCUMENTO tipo_documento; //t = 1, o = sim
    private String historico; //t = sem limite, o = sim
    private String id_participante; //t = 11/14, o = sim
    private TIPO_LANCAMENTO tipo_lancamento; //t = 1, o = sim
    private String valor_entrada; //t = 19d2, o = nao
    private String valor_saida; //t = 19d2, o = nao
    private String saldo_final; //t = 19d2, o = sim

}
