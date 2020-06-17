/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.dsc.lcdpr.blocos;

import br.com.dsc.lcdpr.components.*;
import br.com.dsc.lcdpr.interfaces.LcdprHandler;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.*;

import java.io.Serializable;
import java.util.List;

/**
 * 0 - Abre o arquivo, identifica a pessoa física, os imóveis rurais,
 * as contas bancárias e referencia o período do LCDPR.
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
        "identificacaoPessoaFisica", "parametrosTributacao", "dadosCadastraisContribuinte",
        "imoveisRurais", "exploracaoImoveisRurais", "contasBancarias"
})
public class AberturaIdentificacao implements Serializable, LcdprHandler {

    private static final long serialVersionUID = 5203632030799409016L;
    @JsonProperty("identificacao_pessoa_fisica")
    private IdentificacaoPessoaFisica identificacaoPessoaFisica; //H = 0, 0000, 1:1, o
    @JsonProperty("parametros_tributacao")
    private ParametrosTributacao parametrosTributacao; //H = 2, 0010, 1:1, o
    @JsonProperty("dados_cadastrais_contribuinte")
    private Contribuinte dadosCadastraisContribuinte; //H = 2, 0030, 1:1, o
    @JsonProperty("imoveis_rurais")
    private List<ImovelRural> imoveisRurais; //H = 2, 0040, 1:N, o
    @JsonProperty("exploracao_imoveis_rurais")
    private List<ExploracaoMedianteContrato> exploracaoImoveisRurais; //H = 3, 0045, 1:N, f != o = Se 0040.PARTICIPACAO for menor que 100% ou 0040.TIPO_EXPLORACAO diferente de "1"
    @JsonProperty("contas_bancarias")
    private List<ContaBancaria> contasBancarias; //H = 2, 0050, 1:N, o

}
