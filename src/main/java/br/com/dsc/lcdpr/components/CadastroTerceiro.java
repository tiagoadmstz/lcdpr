/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools "," Templates
 * and open the template in the editor.
 */
package br.com.dsc.lcdpr.components;

import br.com.dsc.lcdpr.enumerated.TIPO_CONTRAPARTE;
import br.com.dsc.lcdpr.interfaces.LcdprHandler;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.*;

import java.io.Serializable;

/**
 * Registro 0045: Cadastro de Terceiros
 * <p>
 * Apresenta os dados de terceiros.
 * Nesse bloco, deve constar a identificação das partes envolvidas, assim como o percentual de participação de cada produtor rural na exploração
 * de uma unidade rural, quando for o caso.
 * Este registro é “filho” do 0040 e, assim, deve ser apresentado após cada registro 0040 a que se refere quando obrigatório.
 * O Campo 6 “CAEPF” é de preenchimento obrigatório para os contribuintes que exploram a propriedade rural individualmente e, na exploração
 * coletiva, para o primeiro titular ou representante do coletivo de produtores. A não obrigatoriedade de preenchimento desta informação no
 * registro 0040 não isenta o contribuinte de suas obrigações relativas a este cadastro.
 * Ocorrência [1:N]
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
        "registro", "codigoImovel", "tipoContraparte", "cpfCnpjContraparte", "nomeContraparte", "percentualContraparte"
})
public class CadastroTerceiro implements Serializable, LcdprHandler {

    private static final long serialVersionUID = -784279936625837037L;
    @Builder.Default
    @JsonProperty("reg")
    private String registro = "0045"; //t = 4, o = sim, p = 0045
    @JsonProperty("cod_imovel")
    private String codigoImovel; //t = 3, o = sim
    @JsonProperty("tipo_contraparte")
    private TIPO_CONTRAPARTE tipoContraparte; //t = 1, o = sim
    @JsonProperty("id_contraparte")
    private String cpfCnpjContraparte; //t = 11-14, o = sim
    @JsonProperty("nome_contraparte")
    private String nomeContraparte; //t = 50, o = sim
    @JsonProperty("perc_contraparte")
    private String percentualContraparte; //t = 3, o = sim

    /**
     * String[] values = {"0045","002","3","12345678912","JOÃO DE SOUSA","00520"}
     *
     * @param values String[] with values in same order of the document
     * @return CadastroTerceiro
     */
    public static CadastroTerceiro buildFromArray(String[] values) {
        return CadastroTerceiro.builder()
                .codigoImovel(values[1])
                .tipoContraparte(TIPO_CONTRAPARTE.getEnum(Integer.parseInt(values[2])))
                .cpfCnpjContraparte(values[3])
                .nomeContraparte(values[4])
                .percentualContraparte(values[5])
                .build();
    }
}
