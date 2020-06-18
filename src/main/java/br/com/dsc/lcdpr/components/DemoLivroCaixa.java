/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.dsc.lcdpr.components;

import br.com.dsc.lcdpr.enumerated.NATUREZA_SALDO_FINAL;
import br.com.dsc.lcdpr.enumerated.TIPO_DOCUMENTO;
import br.com.dsc.lcdpr.enumerated.TIPO_LANCAMENTO;
import br.com.dsc.lcdpr.interfaces.LcdprHandler;
import br.com.dsc.lcdpr.serializers.BigDecimalSerializer;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Registro Q100: Demonstrativo do Resultado da Atividade Rural
 * <p>
 * O saldo inicial a ser registrado é zero no início de cada ano. Os saldos intermediários correspondem à diferença entre as receitas e despesas até
 * o registro. O saldo final do último registro Q100 corresponde à diferença entre todas as receitas e despesas.
 * Ocorrência [0:N]
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
        "registro", "data", "codigoImovel", "codigoConta", "numeroDocumento",
        "tipoDocumento", "historico", "cpfCnpjParticipante", "tipoLancamento",
        "valorEntrada", "valorSaida", "saldoFinal", "naturezaSaldoFinal"
})
public class DemoLivroCaixa implements Serializable, LcdprHandler {

    private static final long serialVersionUID = 3804635706937859453L;
    @Builder.Default
    @JsonProperty("reg")
    private String registro = "Q100"; //t = 4, o = sim, p = Q100
    @Builder.Default
    private LocalDate data = LocalDate.now(); //t = 8, o = sim
    @JsonProperty("cod_imovel")
    private String codigoImovel; //t = 3, o = sim
    @JsonProperty("cod_conta")
    private String codigoConta; //t = 3, o = sim
    @Builder.Default
    @JsonProperty("num_doc")
    private String numeroDocumento = ""; //t = sem limite, o = nao
    @JsonProperty("tipo_doc")
    private TIPO_DOCUMENTO tipoDocumento; //t = 1, o = sim
    @JsonProperty("hist")
    private String historico; //t = sem limite, o = sim
    @JsonProperty("id_partic")
    private String cpfCnpjParticipante; //t = 11/14, o = sim
    @JsonProperty("tipo_lanc")
    private TIPO_LANCAMENTO tipoLancamento; //t = 1, o = sim
    @Builder.Default
    @JsonProperty("vl_entrada")
    @JsonSerialize(using = BigDecimalSerializer.class)
    private BigDecimal valorEntrada = new BigDecimal(0); //t = 19d2, o = nao
    @Builder.Default
    @JsonProperty("vl_saida")
    @JsonSerialize(using = BigDecimalSerializer.class)
    private BigDecimal valorSaida = new BigDecimal(0); //t = 19d2, o = nao
    @Builder.Default
    @JsonProperty("sld_fin")
    @JsonSerialize(using = BigDecimalSerializer.class)
    private BigDecimal saldoFinal = new BigDecimal(0); //t = 19d2, o = sim
    @Builder.Default
    @JsonProperty("nat_sld_fin")
    private NATUREZA_SALDO_FINAL naturezaSaldoFinal = NATUREZA_SALDO_FINAL.POSITIVO;

}
