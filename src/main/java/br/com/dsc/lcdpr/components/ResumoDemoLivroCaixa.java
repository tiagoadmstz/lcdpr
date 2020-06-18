/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.dsc.lcdpr.components;

import br.com.dsc.lcdpr.enumerated.NATUREZA_SALDO_FINAL;
import br.com.dsc.lcdpr.interfaces.LcdprHandler;
import br.com.dsc.lcdpr.serializers.BigDecimalSerializer;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Registro Q200: Resumo Mensal do Demonstrativo do Resultado da Atividade Rural
 * Bloco preenchido com base nas informações do Bloco Q100.
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
        "registro", "mes", "valorEntrada", "valorSaida", "saldoFinal", "naturezaSaldoFinal"
})
public class ResumoDemoLivroCaixa implements Serializable, LcdprHandler {

    private static final long serialVersionUID = -268680092317624306L;
    @Builder.Default
    @JsonProperty("reg")
    private String registro = "Q200"; // t = 4, o = sim, p = Q200
    private String mes; // t = 6, o = sim
    @Builder.Default
    @JsonProperty("vl_entrada")
    @JsonSerialize(using = BigDecimalSerializer.class)
    private BigDecimal valorEntrada = new BigDecimal(0); // t = 19d2, o = nao
    @Builder.Default
    @JsonProperty("vl_saida")
    @JsonSerialize(using = BigDecimalSerializer.class)
    private BigDecimal valorSaida = new BigDecimal(0); // t = 19d2, o = nao
    @Builder.Default
    @JsonProperty("sld_fin")
    @JsonSerialize(using = BigDecimalSerializer.class)
    private BigDecimal saldoFinal = new BigDecimal(0); // t = 19d2, o = nao
    @Builder.Default
    @JsonProperty("nat_sld_fin")
    private NATUREZA_SALDO_FINAL naturezaSaldoFinal = NATUREZA_SALDO_FINAL.POSITIVO;

}
