/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.dsc.lcdpr.components;

import br.com.dsc.lcdpr.annotations.Validation;
import br.com.dsc.lcdpr.deserializers.LocalDateDeserializer;
import br.com.dsc.lcdpr.enumerated.INICIO_PERIODO;
import br.com.dsc.lcdpr.enumerated.SITUACAO_ESPECIAL;
import br.com.dsc.lcdpr.interfaces.LcdprHandler;
import br.com.dsc.lcdpr.serializers.LocalDateSerializer;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
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
    @Validation(size = 4, defaultValue = "0000")
    private String registro = "0000";
    @Builder.Default
    @JsonProperty("nome_esc")
    @Validation(size = 5, defaultValue = "LCDPR")
    private String nomeEsc = "LCDPR";
    @Builder.Default
    @JsonProperty("cod_ver")
    @Validation(size = 4, defaultValue = "0013")
    private String codigoVersao = "0013";
    @Validation(type = 'N', size = 11)
    private String cpf;
    @Validation
    private String nome;
    @Builder.Default
    @JsonProperty("ind_sit_ini_per")
    @Validation(type = 'N', size = 1)
    private INICIO_PERIODO indicadorInicioPeriodo = INICIO_PERIODO.REGULAR;
    @Builder.Default
    @JsonProperty("sit_especial")
    @Validation(type = 'N', size = 1)
    private SITUACAO_ESPECIAL situacaoEspecial = SITUACAO_ESPECIAL.NORMAL;
    @JsonProperty("dt_sit_esp")
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @Validation(type = 'N', size = 8, required = 'N')
    private LocalDate dataSituacaoEspecial;
    @JsonProperty("dt_ini")
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @Validation(type = 'N', size = 8)
    private LocalDate dataInicioPeriodo;
    @JsonProperty("dt_fin")
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @Validation(type = 'N', size = 8)
    private LocalDate dataFinalPeriodo;

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
                .cpf(values[3])
                .nome(values[4])
                .indicadorInicioPeriodo(INICIO_PERIODO.getEnum(values[5]))
                .situacaoEspecial(SITUACAO_ESPECIAL.getEnum(values[6]))
                .dataSituacaoEspecial(!"".equals(values[7]) ? LocalDate.parse(values[7], formatter) : null)
                .dataInicioPeriodo(LocalDate.parse(values[8], formatter))
                .dataFinalPeriodo(LocalDate.parse(values[9], formatter))
                .build();
    }

}
