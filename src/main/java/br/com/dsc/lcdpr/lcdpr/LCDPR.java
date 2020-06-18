/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.dsc.lcdpr.lcdpr;

import br.com.dsc.lcdpr.blocos.AberturaIdentificacao;
import br.com.dsc.lcdpr.blocos.DemonstrativoLivroCaixa;
import br.com.dsc.lcdpr.blocos.EncerramentoArquivo;
import br.com.dsc.lcdpr.interfaces.LcdprHandler;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.*;

import java.io.Serializable;

/**
 * Bloco 0: Abertura, Identificação e Referências
 * Bloco Q: Demonstrativo do Resultado da Atividade Rural
 * Bloco 9: Encerramento do Arquivo Digital
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
        "bloco0", "blocoQ", "bloco9"
})
public class LCDPR implements Serializable, LcdprHandler {

    private static final long serialVersionUID = 9197775706508460101L;
    @JsonProperty("bloco_0")
    private AberturaIdentificacao bloco0;
    @JsonProperty("bloco_Q")
    private DemonstrativoLivroCaixa blocoQ;
    @JsonProperty("bloco_9")
    private EncerramentoArquivo bloco9;

}
