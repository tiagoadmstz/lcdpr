/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.dsc.lcdpr.blocos;

import br.com.dsc.lcdpr.components.*;
import br.com.dsc.lcdpr.interfaces.LcdprHandler;
import lombok.*;

import java.io.Serializable;
import java.util.List;

/**
 * @author Tiago
 */
@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class AberturaIdentificacao implements Serializable, LcdprHandler {

    private static final long serialVersionUID = 5203632030799409016L;
    private IdentificacaoPessoaFisica identificacao_pessoa_fisica; //H = 0, 0000, 1:1, o
    private ParametrosTributacao parametros_tributacao; //H = 2, 0010, 1:1, o
    private Contribuinte dados_cadastrais_contribuinte; //H = 2, 0030, 1:1, o
    private List<ImovelRural> imoveis_rurais; //H = 2, 0040, 1:N, o
    private List<ExploracaoMedianteContrato> exploracao_imoveis_rurais; //H = 3, 0045, 1:N, f != o = Se 0040.PARTICIPACAO for menor que 100% ou 0040.TIPO_EXPLORACAO diferente de "1"
    private List<ContaBancaria> contas_bancarias; //H = 2, 0050, 1:N, o

}
