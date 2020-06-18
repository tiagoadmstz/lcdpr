/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.dsc.lcdpr.blocos;

import br.com.dsc.lcdpr.components.DemoLivroCaixa;
import br.com.dsc.lcdpr.components.ResumoDemoLivroCaixa;
import br.com.dsc.lcdpr.interfaces.LcdprHandler;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

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

}
