/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.dsc.lcdpr.components;

import br.com.dsc.lcdpr.enumerated.TIPO_EXPLORACAO;
import br.com.dsc.lcdpr.interfaces.LcdprHandler;
import lombok.*;

import java.io.Serializable;

/**
 * @author Tiago
 */
@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class ImovelRural implements Serializable, LcdprHandler {

    private static final long serialVersionUID = 5884927715383063502L;
    @Builder.Default
    private String reg = "0040";    // t = 4, o = sim, p = 0040
    private String cod_imovel;    // t = 3, o = sim
    @Builder.Default
    private String pais = "BRA";    // t = 3, o = sim, p = BRA-Código ISO 3166-1
    private String moeda;    // t = 3, o = sim, Codigo ISO 4217:2015
    private String cad_ITR; // t = 8, o = sim(BRA) - CAFIR
    private String atividade_economica_pf;  // t = 14, o = sim(BRA)
    private String inscricao_estadual; // t = 14, o = não
    private String nome_imovel; // t = 50, o = sim
    private String endereco; // t = 150, o = sim
    private String numero; // t = 6, o = nao
    private String complemento; // t = 50, o = nao
    private String bairro; // t = 50, o = sim
    private String uf; // t = 2, o = sim(BRA)
    private String cod_municipio; // t = 7, o = sim
    private String cep; // t = 8, o = sim(BRA)
    private TIPO_EXPLORACAO tipo_exploracao; // t = 1, o = sim
    private String participacao; // t = 4, o = sim

}
