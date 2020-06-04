/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.dsc.lcdpr.components;

import br.com.dsc.lcdpr.enumerated.INICIO_PERIODO;
import br.com.dsc.lcdpr.enumerated.SITUACAO_ESPECIAL;
import br.com.dsc.lcdpr.interfaces.LcdprHandler;
import com.google.gson.annotations.SerializedName;
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
public class IdentificacaoPessoaFisica implements Serializable, LcdprHandler {

    private static final long serialVersionUID = -1849600283483749425L;
    @Builder.Default
    private String reg = "0000"; //t = 4, o = sim, p = 0000
    @Builder.Default
    private String nome_esc = "LCDPR"; //t = 5, o = sim, p = LCDPR
    @Builder.Default
    @SerializedName("cod_ver")
    private String cod_versao = "0001"; //t = 4, o = sim, p = 0001
    private Long cpf; //t = 11, o = sim
    private String nome;
    private INICIO_PERIODO ind_sit_ini_per;
    @SerializedName("sit_especial")
    private SITUACAO_ESPECIAL situacao_especial;
    @SerializedName("dt_sit_esp")
    private Integer dataSituacaoEspecial; //t = 8, o = não
    @SerializedName("dt_ini")
    private LocalDate dataInicioPeriodo; //t = 8, o = sim
    @SerializedName("dt_fin")
    private LocalDate dataFinalPeriodo; //t = 8, o = sim

}
