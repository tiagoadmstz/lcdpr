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
import java.time.format.DateTimeFormatter;

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
    private String registro = "0000"; //t = 4, o = sim, p = 0000
    @Builder.Default
    @JsonProperty("nome_esc")
    private String nomeEsc = "LCDPR"; //t = 5, o = sim, p = LCDPR
    @Builder.Default
    @JsonProperty("cod_ver")
    private String codigoVersao = "0013"; //t = 4, o = sim, p = 0013
    private Long cpf; //t = 11, o = sim
    private String nome; //t = sem limite, o = sim
    @Builder.Default
    @JsonProperty("ind_sit_ini_per")
    private INICIO_PERIODO indicadorInicioPeriodo = INICIO_PERIODO.REGULAR; //t = 1, o = sim
    @Builder.Default
    @JsonProperty("sit_especial")
    private SITUACAO_ESPECIAL situacaoEspecial = SITUACAO_ESPECIAL.NORMAL; //t = 1, o = sim
    @JsonProperty("dt_sit_esp")
    @JsonSerialize(using = LocalDateSerializer.class)
    private LocalDate dataSituacaoEspecial; //t = 8, o = não
    @JsonProperty("dt_ini")
    @JsonSerialize(using = LocalDateSerializer.class)
    private LocalDate dataInicioPeriodo; //t = 8, o = sim
    @JsonProperty("dt_fin")
    @JsonSerialize(using = LocalDateSerializer.class)
    private LocalDate dataFinalPeriodo; //t = 8, o = sim

    /**
     * String[] values = {"0000","LCDPR","0013","11111111191","JOSÉ DA SILVA","0","0","","01012019","31122019"}
     *
     * @param values String[] with values in same order of the document
     * @return IdentificacaoPessoaFisica
     */
    public static IdentificacaoPessoaFisica buildFromArray(String[] values) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("ddMMyyyy");
        return IdentificacaoPessoaFisica.builder()
                .nomeEsc(values[1])
                .codigoVersao(values[2])
                .cpf(Long.parseLong(values[3]))
                .nome(values[4])
                .indicadorInicioPeriodo(INICIO_PERIODO.getEnum(values[5]))
                .situacaoEspecial(SITUACAO_ESPECIAL.getEnum(values[6]))
                .dataSituacaoEspecial(!"".equals(values[7]) ? LocalDate.parse(values[7], formatter) : null)
                .dataInicioPeriodo(LocalDate.parse(values[8], formatter))
                .dataFinalPeriodo(LocalDate.parse(values[9], formatter))
                .build();
    }

}
