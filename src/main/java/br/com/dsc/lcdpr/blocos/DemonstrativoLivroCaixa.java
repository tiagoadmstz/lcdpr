/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.dsc.lcdpr.blocos;

import br.com.dsc.lcdpr.components.DemoLivroCaixa;
import br.com.dsc.lcdpr.components.ResumoDemoLivroCaixa;
import br.com.dsc.lcdpr.enumerated.NATUREZA_SALDO_FINAL;
import br.com.dsc.lcdpr.exceptions.ServiceException;
import br.com.dsc.lcdpr.interfaces.LcdprHandler;
import br.com.dsc.lcdpr.util.WordUtils;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Year;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Q - Apresenta o demonstrativo do resultado da atividade rural
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
        "demonstrativoLivroCaixa", "resumoDemonstrativoLivroCaixa"
})
public class DemonstrativoLivroCaixa implements Serializable, LcdprHandler {

    private static final long serialVersionUID = 6287575698325024956L;
    @Builder.Default
    @JsonProperty("demonstrativo_livro_caixa")
    private List<DemoLivroCaixa> demonstrativoLivroCaixa = new ArrayList(); //H = 2, Q100, 1:N, o
    @Builder.Default
    @JsonProperty("resumo_demonstrativo_livro_caixa")
    private List<ResumoDemoLivroCaixa> resumoDemonstrativoLivroCaixa = new ArrayList(); //H = 2, Q200, 1:N, o

    public DemonstrativoLivroCaixa addDemoLivroCaixa(DemoLivroCaixa demoLivroCaixa) {
        demonstrativoLivroCaixa.add(demoLivroCaixa);
        return this;
    }

    public DemonstrativoLivroCaixa addResumoDemonstrativoLivroCaixa(ResumoDemoLivroCaixa resumoDemoLivroCaixa) {
        resumoDemonstrativoLivroCaixa.add(resumoDemoLivroCaixa);
        return this;
    }

    public DemonstrativoLivroCaixa buildFromLinesList(List<String> lines) {
        demonstrativoLivroCaixa = DemoLivroCaixa.buildFromLinesList(lines.stream().filter(l -> l.contains("Q100")).collect(Collectors.toList()));
        resumoDemonstrativoLivroCaixa = ResumoDemoLivroCaixa.buildFromLinesList(lines.stream().filter(l -> l.contains("Q200")).collect(Collectors.toList()));
        return this;
    }

    public int getOperationsYear() {
        return demonstrativoLivroCaixa.stream().map(d -> d.getData().getYear()).findFirst().orElseGet(() -> Year.now().getValue());
    }

    public void sortDemonstrativoLivroCaixaByData() {
        demonstrativoLivroCaixa = demonstrativoLivroCaixa.stream().sorted(Comparator.comparing(DemoLivroCaixa::getData)).collect(Collectors.toList());
    }

    public void validate() {
        List<String> messages = new ArrayList();
        if (demonstrativoLivroCaixa == null) {
            messages.add("BlockQ100 not found");
        }
        if (!messages.isEmpty()) throw new ServiceException(messages);
    }

    public BigDecimal calculate() {
        BigDecimal saldo = new BigDecimal(0.00);
        for (DemoLivroCaixa demoLivroCaixa : demonstrativoLivroCaixa) {
            saldo = demoLivroCaixa.getValorEntrada() != null && demoLivroCaixa.getValorSaida() == null
                    ? saldo.add(demoLivroCaixa.getValorEntrada())
                    : saldo.subtract(demoLivroCaixa.getValorSaida());
            verifyAndSetSaldo(demoLivroCaixa, saldo);
            calculateBlockQ200(demoLivroCaixa);
        }
        return saldo;
    }

    /**
     * Arrumar bloco resumo para cada mes
     */
    public void startBlockQ200ByMonth() {
        resumoDemonstrativoLivroCaixa = new ArrayList();
        IntStream.range(1, 13).forEachOrdered(i ->
                resumoDemonstrativoLivroCaixa.add(ResumoDemoLivroCaixa.builder()
                        .mes(WordUtils.lpad(String.valueOf(i), 2, "0") + getOperationsYear())
                        .build())
        );
    }

    /**
     * note que o saldo sempre fica + ou - na hora demoLivroCaixa salvar que mudamos!
     * natureza e saldo sempre vai ter esse campo => obrigatorio
     *
     * @param demoLivroCaixa
     * @param saldo
     */
    public void verifyAndSetSaldo(DemoLivroCaixa demoLivroCaixa, BigDecimal saldo) {
        //TODO: refazer método
        if (saldo.compareTo(BigDecimal.ZERO) >= 0) {
            demoLivroCaixa.setNaturezaSaldoFinal(NATUREZA_SALDO_FINAL.POSITIVO);
            demoLivroCaixa.setSaldoFinal(saldo);
        } else {
            demoLivroCaixa.setNaturezaSaldoFinal(NATUREZA_SALDO_FINAL.NEGATIVO);
            demoLivroCaixa.setSaldoFinal(saldo.multiply(BigDecimal.ONE.negate()));
        }
    }

    /**
     * ja vai calculando o bloco Q200
     *
     * @param demoLivroCaixa
     */
    public void calculateBlockQ200(DemoLivroCaixa demoLivroCaixa) {
        //TODO: refazer método
        int mesOp = demoLivroCaixa.getData().getMonthValue();
        ResumoDemoLivroCaixa opResumo = resumoDemonstrativoLivroCaixa.get(mesOp - 1);

        if (demoLivroCaixa.getValorEntrada() != null && demoLivroCaixa.getValorEntrada().compareTo(BigDecimal.ZERO) != 0) {
            opResumo.setValorEntrada(opResumo.getValorEntrada().add(demoLivroCaixa.getValorEntrada()));

        } else if (demoLivroCaixa.getValorSaida() != null && demoLivroCaixa.getValorSaida().compareTo(BigDecimal.ZERO) != 0) {
            opResumo.setValorSaida(opResumo.getValorSaida().add(demoLivroCaixa.getValorSaida()));
        }
    }

    public void recalculateBlockQ200() {
        //TODO: refazer método
        BigDecimal saldoResumo = BigDecimal.ZERO;
        for (int i = 0; i < resumoDemonstrativoLivroCaixa.size(); i++) {

            ResumoDemoLivroCaixa opResumo = resumoDemonstrativoLivroCaixa.get(i);
            saldoResumo = saldoResumo.add(opResumo.getValorEntrada()).subtract(opResumo.getValorSaida());

            resumoDemonstrativoLivroCaixa.get(i).setSaldoFinal(saldoResumo);

            if (saldoResumo.compareTo(BigDecimal.ZERO) >= 0) {
                opResumo.setNaturezaSaldoFinal(NATUREZA_SALDO_FINAL.POSITIVO);
                opResumo.setSaldoFinal(saldoResumo);
            } else {
                opResumo.setNaturezaSaldoFinal(NATUREZA_SALDO_FINAL.NEGATIVO);
                opResumo.setSaldoFinal(saldoResumo.multiply(BigDecimal.ONE.negate()));
            }

            resumoDemonstrativoLivroCaixa.set(i, opResumo);
        }
    }

}
