/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.dsc.lcdpr.components;

import br.com.dev.engine.date.Datas;
import br.com.dsc.lcdpr.deserializers.BigDecimalDeserializer;
import br.com.dsc.lcdpr.deserializers.LocalDateDeserializer;
import br.com.dsc.lcdpr.enumerated.NATUREZA_SALDO_FINAL;
import br.com.dsc.lcdpr.enumerated.TIPO_DOCUMENTO;
import br.com.dsc.lcdpr.enumerated.TIPO_LANCAMENTO;
import br.com.dsc.lcdpr.exceptions.ServiceException;
import br.com.dsc.lcdpr.interfaces.LcdprHandler;
import br.com.dsc.lcdpr.serializers.BigDecimalSerializer;
import br.com.dsc.lcdpr.serializers.LocalDateSerializer;
import br.com.dsc.lcdpr.util.BigDecimalUtil;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
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
    @JsonDeserialize(using = BigDecimalDeserializer.class)
    private BigDecimal valorEntrada = new BigDecimal(0); //t = 19d2, o = nao
    @Builder.Default
    @JsonProperty("vl_saida")
    @JsonSerialize(using = BigDecimalSerializer.class)
    @JsonDeserialize(using = BigDecimalDeserializer.class)
    private BigDecimal valorSaida = new BigDecimal(0); //t = 19d2, o = nao
    @Builder.Default
    @JsonProperty("sld_fin")
    @JsonSerialize(using = BigDecimalSerializer.class)
    @JsonDeserialize(using = BigDecimalDeserializer.class)
    private BigDecimal saldoFinal = new BigDecimal(0); //t = 19d2, o = sim
    @Builder.Default
    @JsonProperty("nat_sld_fin")
    private NATUREZA_SALDO_FINAL naturezaSaldoFinal = NATUREZA_SALDO_FINAL.POSITIVO;

    public static DemoLivroCaixa buildFromArray(String[] values) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("ddMMyyyy");
        return DemoLivroCaixa.builder()
                .data(LocalDate.parse(values[1], formatter))
                .codigoImovel(values[2])
                .codigoConta(values[3])
                .numeroDocumento(values[4])
                .tipoDocumento(TIPO_DOCUMENTO.getEnum(values[5]))
                .historico(values[6])
                .cpfCnpjParticipante(values[7])
                .tipoLancamento(TIPO_LANCAMENTO.getEnum(values[8]))
                .valorEntrada(BigDecimalUtil.stringToBigDecimal(values[9], 2))
                .valorSaida(BigDecimalUtil.stringToBigDecimal(values[10], 2))
                .saldoFinal(BigDecimalUtil.stringToBigDecimal(values[11], 2))
                .naturezaSaldoFinal(NATUREZA_SALDO_FINAL.getEnum(values[12]))
                .build();
    }

    public static List<DemoLivroCaixa> buildFromLinesList(List<String> lines) {
        return lines.stream().map(l -> buildFromArray(l.split("\\|"))).collect(Collectors.toList());
    }

    public String entryDescription() {
        return String.format("%s - %s, Valor %s: R$ %.2f",
                Datas.getDateString(data),
                historico,
                valorEntrada != null && valorSaida == null ? "entrada" : "saída",
                valorEntrada != null && valorSaida == null ? valorEntrada : valorSaida);
    }

    public List<String> validate(LocalDate dataInicio, LocalDate dataFinal) {
        List<String> messages = new ArrayList();
        if (valorEntrada == null && valorSaida == null) {
            messages.add(ServiceException.buildMessage("Operação '{}' sem 'Valor de Entrada ou de Saída'.", entryDescription()));
        }
        if (BigDecimal.ZERO.compareTo(valorEntrada) != 0 && BigDecimal.ZERO.compareTo(valorSaida) != 0) {
            messages.add(ServiceException.buildMessage("Operação '{}' não pode conter valor de entrada e de saída simultâneamente.", entryDescription()));
        }
        if (codigoImovel == null) {
            messages.add(ServiceException.buildMessage("Operação '{}' sem 'Código do imóvel'.", entryDescription()));
        }
        if (codigoConta == null) {
            messages.add(ServiceException.buildMessage("Operação '{}' sem 'Código da conta'.", entryDescription()));
        }
        if (data == null) {
            messages.add(ServiceException.buildMessage("Operação '{}' sem 'Data'.", entryDescription()));
        } else {
            if (data.isBefore(dataInicio)) {
                messages.add(ServiceException.buildMessage("Operação '{}' anterior ao  'Início do período' dessa declaração (Bloco 0000).", entryDescription()));
            }
            if (data.isAfter(dataFinal)) {
                messages.add(ServiceException.buildMessage("Operação '{}' posterior ao  'Final do período' dessa declaração (Bloco 0000).", entryDescription()));
            }
        }
        if (entryDescription() == null) {
            messages.add(ServiceException.buildMessage("Operação '{}' sem 'Histórico/Descrição'.", entryDescription()));
        }
        if (tipoDocumento == null) {
            messages.add(ServiceException.buildMessage("Operação '{}' sem 'Tipo de documento'.", entryDescription()));
        }
        if (tipoLancamento == null) {
            messages.add(ServiceException.buildMessage("Operação '{}' sem 'Tipo de lançamento'.", entryDescription()));
        }
        if (cpfCnpjParticipante == null) {
            messages.add(ServiceException.buildMessage("Operação '{}' sem 'CPF/CNPJ'.", entryDescription()));
        }
        return messages;
    }

}
