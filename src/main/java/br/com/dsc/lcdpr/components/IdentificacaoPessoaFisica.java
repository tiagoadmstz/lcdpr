/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.dsc.lcdpr.components;

import br.com.dsc.lcdpr.enumerated.INICIO_PERIODO;
import br.com.dsc.lcdpr.enumerated.SITUACAO_ESPECIAL;
import br.com.dsc.lcdpr.interfaces.LcdprHandler;
import br.com.dsc.lcdpr.serializers.LocalDateSerializer;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * Bloco 0: Abertura, Identificação e Referências
 * Registro 0000: Abertura do Arquivo Digital e Identificação da Pessoa Física
 * Ocorrência [1:1]
 *
 * @author Tiago D.
 */
@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "identificaoRegistro", "nomeEsc", "codigoVersao", "cpf", "nome", "indicadorInicioPeriodo",
        "situacaoEspecial", "dataSituacaoEspecial", "dataInicioPeriodo", "dataFinalPeriodo"
})
public class IdentificacaoPessoaFisica implements Serializable, LcdprHandler {

    private static final long serialVersionUID = -1849600283483749425L;
    @Builder.Default
    @JsonProperty("reg")
    private String identificaoRegistro = "0000"; //t = 4, o = sim, p = 0000
    @Builder.Default
    @JsonProperty("nome_esc")
    private String nomeEsc = "LCDPR"; //t = 5, o = sim, p = LCDPR
    @Builder.Default
    @JsonProperty("cod_ver")
    private String codigoVersao = "0013"; //t = 4, o = sim, p = 0013
    private Long cpf; //t = 11, o = sim
    private String nome;
    @Builder.Default
    @JsonProperty("ind_sit_ini_per")
    private INICIO_PERIODO indicadorInicioPeriodo = INICIO_PERIODO.REGULAR;
    @Builder.Default
    @JsonProperty("sit_especial")
    private SITUACAO_ESPECIAL situacaoEspecial = SITUACAO_ESPECIAL.NORMAL;
    @JsonProperty("dt_sit_esp")
    private Integer dataSituacaoEspecial; //t = 8, o = não
    @JsonProperty("dt_ini")
    @JsonSerialize(using = LocalDateSerializer.class)
    private LocalDate dataInicioPeriodo; //t = 8, o = sim
    @JsonProperty("dt_fin")
    @JsonSerialize(using = LocalDateSerializer.class)
    private LocalDate dataFinalPeriodo; //t = 8, o = sim

}
