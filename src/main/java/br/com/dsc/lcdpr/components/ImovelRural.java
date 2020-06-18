/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.dsc.lcdpr.components;

import br.com.dsc.lcdpr.enumerated.TIPO_EXPLORACAO;
import br.com.dsc.lcdpr.interfaces.LcdprHandler;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.*;

import java.io.Serializable;

/**
 * Registro 0040: Cadastro dos Imóveis Rurais
 * Apresenta os dados cadastrais e de exploração dos imóveis rurais do produtor rural pessoa física.
 * Quando houver mudança na exploração dos imóveis durante o ano calendário, em que um imóvel é explorado individualmente e passa a sê-lo coletivamente
 * (condomínio ou parceria, por exemplo) ou quando a exploração coletiva tem os percentuais de participação alterados durante o ano, deve ser preenchido um novo
 * registro do mesmo imóvel para cada alteração da sua exploração.
 * O Campo 6 “CAEPF” é de preenchimento obrigatório para os contribuintes que exploram a propriedade rural individualmente e, na exploração
 * coletiva, para o primeiro titular ou representante do coletivo de produtores. A não obrigatoriedade de preenchimento desta informação no registro
 * 0040 não isenta o contribuinte de suas obrigações relativas a este cadastro.
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
        "registro", "codigoImovel", "pais", "moeda", "cadastroImpostoTerritorialRural",
        "cadastroAtividadeEconomicaPessoaFisica", "inscricaoEstadual", "nomeImovel", "endereco",
        "numero", "complemento", "bairro", "uf", "codigoMunicipio", "cep", "tipoExploracao", "participacao"
})
public class ImovelRural implements Serializable, LcdprHandler {

    private static final long serialVersionUID = 5884927715383063502L;
    @Builder.Default
    @JsonProperty("reg")
    private String registro = "0040";    // t = 4, o = sim, p = 0040
    @JsonProperty("cod_imovel")
    private String codigoImovel;    // t = 3, o = sim
    @Builder.Default
    private String pais = "BR";    // t = 2, o = sim, p = BRA-Código ISO 3166-1
    @Builder.Default
    private String moeda = "BRL";    // t = 3, o = sim, Codigo ISO 4217:2015
    @JsonProperty("cad_ITR")
    private String cadastroImpostoTerritorialRural; // t = 8, o = sim(BRA) - CAFIR
    @JsonProperty("CAEPF")
    private String cadastroAtividadeEconomicaPessoaFisica;  // t = 14, o = sim(BRA)
    @Builder.Default
    @JsonProperty("inscr_estadual")
    private String inscricaoEstadual = ""; // t = 14, o = não
    @JsonProperty("nome_imovel")
    private String nomeImovel; // t = 50, o = sim
    private String endereco; // t = 150, o = sim
    @Builder.Default
    @JsonProperty("num")
    private String numero = ""; // t = 6, o = nao
    @Builder.Default
    @JsonProperty("compl")
    private String complemento = ""; // t = 50, o = nao
    private String bairro; // t = 50, o = sim
    private String uf; // t = 2, o = sim(BRA) http://sped.rfb.gov.br/pasta/show/1932
    @JsonProperty("cod_mun")
    private String codigoMunicipio; // t = 7, o = sim http://sped.rfb.gov.br/pasta/show/1932
    private String cep; // t = 8, o = sim(BRA)
    @Builder.Default
    @JsonProperty("tipo_exploracao")
    private TIPO_EXPLORACAO tipoExploracao = TIPO_EXPLORACAO.IMOVEL_PROPRIO; // t = 1, o = sim
    private String participacao; // t = 4, o = sim

}
