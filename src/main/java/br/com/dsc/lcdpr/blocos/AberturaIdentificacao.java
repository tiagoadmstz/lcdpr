/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.dsc.lcdpr.blocos;

import br.com.dsc.lcdpr.components.ContaBancaria;
import br.com.dsc.lcdpr.components.Contribuinte;
import br.com.dsc.lcdpr.components.ExploracaoMedianteContrato;
import br.com.dsc.lcdpr.components.IdentificacaoPessoaFisica;
import br.com.dsc.lcdpr.components.ImovelRural;
import br.com.dsc.lcdpr.components.ParametrosTributacao;
import br.com.dsc.lcdpr.interfaces.LcdprHandler;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;

/**
 *
 * @author Tiago
 */
public class AberturaIdentificacao implements Serializable, LcdprHandler {

    private static final long serialVersionUID = 5203632030799409016L;
    private IdentificacaoPessoaFisica identificacao_pessoa_fisica; //H = 0, 0000, 1:1, o
    private ParametrosTributacao parametros_tributacao; //H = 2, 0010, 1:1, o
    private Contribuinte dados_cadastrais_contribuinte; //H = 2, 0030, 1:1, o
    private List<ImovelRural> imoveis_rurais; //H = 2, 0040, 1:N, o
    private List<ExploracaoMedianteContrato> exploracao_imoveis_rurais; //H = 3, 0045, 1:N, f != o = Se 0040.PARTICIPACAO for menor que 100% ou 0040.TIPO_EXPLORACAO diferente de "1"
    private List<ContaBancaria> contas_bancarias; //H = 2, 0050, 1:N, o

    public AberturaIdentificacao() {
    }

    public AberturaIdentificacao(IdentificacaoPessoaFisica identificacao_pessoa_fisica, ParametrosTributacao parametros_tributacao, Contribuinte dados_cadastrais_contribuinte, List<ImovelRural> imoveis_rurais, List<ExploracaoMedianteContrato> exploracao_imoveis_rurais, List<ContaBancaria> contas_bancarias) {
        this.identificacao_pessoa_fisica = identificacao_pessoa_fisica;
        this.parametros_tributacao = parametros_tributacao;
        this.dados_cadastrais_contribuinte = dados_cadastrais_contribuinte;
        this.imoveis_rurais = imoveis_rurais;
        this.exploracao_imoveis_rurais = exploracao_imoveis_rurais;
        this.contas_bancarias = contas_bancarias;
    }

    public IdentificacaoPessoaFisica getIdentificacao_pessoa_fisica() {
        return identificacao_pessoa_fisica;
    }

    public void setIdentificacao_pessoa_fisica(IdentificacaoPessoaFisica identificacao_pessoa_fisica) {
        this.identificacao_pessoa_fisica = identificacao_pessoa_fisica;
    }

    public ParametrosTributacao getParametros_tributacao() {
        return parametros_tributacao;
    }

    public void setParametros_tributacao(ParametrosTributacao parametros_tributacao) {
        this.parametros_tributacao = parametros_tributacao;
    }

    public Contribuinte getDados_cadastrais_contribuinte() {
        return dados_cadastrais_contribuinte;
    }

    public void setDados_cadastrais_contribuinte(Contribuinte dados_cadastrais_contribuinte) {
        this.dados_cadastrais_contribuinte = dados_cadastrais_contribuinte;
    }

    public List<ImovelRural> getImoveis_rurais() {
        return imoveis_rurais;
    }

    public void setImoveis_rurais(List<ImovelRural> imoveis_rurais) {
        this.imoveis_rurais = imoveis_rurais;
    }

    public List<ExploracaoMedianteContrato> getExploracao_imoveis_rurais() {
        return exploracao_imoveis_rurais;
    }

    public void setExploracao_imoveis_rurais(List<ExploracaoMedianteContrato> exploracao_imoveis_rurais) {
        this.exploracao_imoveis_rurais = exploracao_imoveis_rurais;
    }

    public List<ContaBancaria> getContas_bancarias() {
        return contas_bancarias;
    }

    public void setContas_bancarias(List<ContaBancaria> contas_bancarias) {
        this.contas_bancarias = contas_bancarias;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 41 * hash + Objects.hashCode(this.identificacao_pessoa_fisica);
        hash = 41 * hash + Objects.hashCode(this.parametros_tributacao);
        hash = 41 * hash + Objects.hashCode(this.dados_cadastrais_contribuinte);
        hash = 41 * hash + Objects.hashCode(this.imoveis_rurais);
        hash = 41 * hash + Objects.hashCode(this.exploracao_imoveis_rurais);
        hash = 41 * hash + Objects.hashCode(this.contas_bancarias);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final AberturaIdentificacao other = (AberturaIdentificacao) obj;
        if (!Objects.equals(this.identificacao_pessoa_fisica, other.identificacao_pessoa_fisica)) {
            return false;
        }
        if (!Objects.equals(this.parametros_tributacao, other.parametros_tributacao)) {
            return false;
        }
        if (!Objects.equals(this.dados_cadastrais_contribuinte, other.dados_cadastrais_contribuinte)) {
            return false;
        }
        if (!Objects.equals(this.imoveis_rurais, other.imoveis_rurais)) {
            return false;
        }
        if (!Objects.equals(this.exploracao_imoveis_rurais, other.exploracao_imoveis_rurais)) {
            return false;
        }
        if (!Objects.equals(this.contas_bancarias, other.contas_bancarias)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "AberturaIdentificacao{" + "identificacao_pessoa_fisica=" + identificacao_pessoa_fisica + ", parametros_tributacao=" + parametros_tributacao + ", dados_cadastrais_contribuinte=" + dados_cadastrais_contribuinte + ", imoveis_rurais=" + imoveis_rurais + ", exploracao_imoveis_rurais=" + exploracao_imoveis_rurais + ", contas_bancarias=" + contas_bancarias + '}';
    }

}
