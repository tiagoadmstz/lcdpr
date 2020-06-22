/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.dsc.lcdpr.components;

import br.com.dsc.lcdpr.enumerated.FORMA_APURACAO;
import br.com.dsc.lcdpr.interfaces.LcdprHandler;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.*;

import java.io.Serializable;

/**
 * Registro 0010: Parâmetros de Tributação
 * Apresenta os parâmetros fiscais que identificam quais blocos e registros serão preenchidos.
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
@JsonPropertyOrder({"registro", "formaApuracao"})
public class ParametrosTributacao implements Serializable, LcdprHandler {

    private static final long serialVersionUID = 901801438322291828L;
    @Builder.Default
    @JsonProperty("reg")
    private String registro = "0010"; //t = 4, o = sim, p = 0010
    @Builder.Default
    @JsonProperty("forma_apur")
    private FORMA_APURACAO formaApuracao = FORMA_APURACAO.LIVRO_CAIXA; //t = 1, o = sim

    /**
     * String[] values = {"0010","1"}
     *
     * @param values String[] with values in same order of the document
     * @return ParametrosTributacao
     */
    public static ParametrosTributacao buildFromArray(String[] values) {
        return ParametrosTributacao.builder()
                .formaApuracao(FORMA_APURACAO.getEnum(Integer.valueOf(values[1])))
                .build();
    }

}
