/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.dsc.lcdpr.blocos;

import br.com.dsc.lcdpr.interfaces.LcdprHandler;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.*;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

/**
 * Registro 9999: Identificação do Signatário do LCDPR e Encerramento do Arquivo Digital
 * <p>
 * Este registro traz informações do profissional de contabilidade responsável pela elaboração do LCDPR e a quantidade total de registros (linhas)
 * do arquivo do LCDPR. Há que se ressaltar que vários registros ocorrem mais de uma vez. Portanto, na contagem dos registros, deve-se levar em
 * consideração todos os registros informados, ainda que repetidos.
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
        "registro", "nomeContador", "cpfCnpjContador", "numeroConselhoRegionalContabilidade",
        "emailContador", "telefoneContador", "quantidadeRegistrosArquivo"
})
public class EncerramentoArquivo implements Serializable, LcdprHandler {

    private static final long serialVersionUID = 136224468484970674L;
    @Builder.Default
    @JsonProperty("reg")
    private String registro = "9999"; // t = 4, o = sim, p = 9999
    @JsonProperty("ident_nom")
    private String nomeContador; // t = sem limite, o = nao
    @JsonProperty("ident_cpf_cnpj")
    private String cpfCnpjContador; // t = 11-14, o = nao
    @JsonProperty("ind_crc")
    private String numeroConselhoRegionalContabilidade; // t = sem limite, o = nao
    @JsonProperty("email")
    private String emailContador; // t = 115, o = nao
    @JsonProperty("fone")
    private Long telefoneContador; // t = 15, o = nao
    @JsonProperty("qtd_lin")
    private Integer quantidadeRegistrosArquivo; // t = 30, o = sim

    public EncerramentoArquivo buildFromLinesList(List<String> lines) {
        Optional<String[]> optional = lines.stream().filter(l -> l.contains("9999")).map(l -> l.split("\\|")).findFirst();
        return optional.map(values -> EncerramentoArquivo.builder()
                .nomeContador(values[1])
                .cpfCnpjContador(values[2])
                .numeroConselhoRegionalContabilidade(values[3])
                .emailContador(values[4])
                .telefoneContador(values[5] != null ? Long.parseLong(values[5]) : null)
                .quantidadeRegistrosArquivo(values[6] != null ? Integer.parseInt(values[6]) : null)
                .build()
        ).orElse(null);
    }

}
