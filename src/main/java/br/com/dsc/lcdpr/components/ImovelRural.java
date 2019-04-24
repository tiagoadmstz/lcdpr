/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.dsc.lcdpr.components;

import br.com.dsc.lcdpr.enumerated.TIPO_EXPLORACAO;
import br.com.dsc.lcdpr.interfaces.LcdprHandler;
import java.io.Serializable;
import java.util.Objects;

/**
 *
 * @author Tiago
 */
public class ImovelRural implements Serializable, LcdprHandler {

    private static final long serialVersionUID = 5884927715383063502L;
    private String reg;	// t = 4, o = sim, p = 0040
    private String cod_imovel;	// t = 3, o = sim
    private String pais;	// t = 3, o = sim, p = BRA-Código ISO 3166-1
    private String moeda;	// t = 3, o = sim, Codigo ISO 4217:2015
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

    public ImovelRural() {
        reg = "0040";
        pais = "BRA";
    }

    public String getReg() {
        return reg;
    }

    public void setReg(String reg) {
        this.reg = reg;
    }

    public String getCod_imovel() {
        return cod_imovel;
    }

    public void setCod_imovel(String cod_imovel) {
        this.cod_imovel = cod_imovel;
    }

    public String getPais() {
        return pais;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }

    public String getMoeda() {
        return moeda;
    }

    public void setMoeda(String moeda) {
        this.moeda = moeda;
    }

    public String getCad_ITR() {
        return cad_ITR;
    }

    public void setCad_ITR(String cad_ITR) {
        this.cad_ITR = cad_ITR;
    }

    public String getAtividade_economica_pf() {
        return atividade_economica_pf;
    }

    public void setAtividade_economica_pf(String atividade_economica_pf) {
        this.atividade_economica_pf = atividade_economica_pf;
    }

    public String getInscricao_estadual() {
        return inscricao_estadual;
    }

    public void setInscricao_estadual(String inscricao_estadual) {
        this.inscricao_estadual = inscricao_estadual;
    }

    public String getNome_imovel() {
        return nome_imovel;
    }

    public void setNome_imovel(String nome_imovel) {
        this.nome_imovel = nome_imovel;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getComplemento() {
        return complemento;
    }

    public void setComplemento(String complemento) {
        this.complemento = complemento;
    }

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public String getUf() {
        return uf;
    }

    public void setUf(String uf) {
        this.uf = uf;
    }

    public String getCod_municipio() {
        return cod_municipio;
    }

    public void setCod_municipio(String cod_municipio) {
        this.cod_municipio = cod_municipio;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public TIPO_EXPLORACAO getTipo_exploracao() {
        return tipo_exploracao;
    }

    public void setTipo_exploracao(TIPO_EXPLORACAO tipo_exploracao) {
        this.tipo_exploracao = tipo_exploracao;
    }

    public String getParticipacao() {
        return participacao;
    }

    public void setParticipacao(String participacao) {
        this.participacao = participacao;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 41 * hash + Objects.hashCode(this.reg);
        hash = 41 * hash + Objects.hashCode(this.cod_imovel);
        hash = 41 * hash + Objects.hashCode(this.pais);
        hash = 41 * hash + Objects.hashCode(this.moeda);
        hash = 41 * hash + Objects.hashCode(this.cad_ITR);
        hash = 41 * hash + Objects.hashCode(this.atividade_economica_pf);
        hash = 41 * hash + Objects.hashCode(this.inscricao_estadual);
        hash = 41 * hash + Objects.hashCode(this.nome_imovel);
        hash = 41 * hash + Objects.hashCode(this.endereco);
        hash = 41 * hash + Objects.hashCode(this.numero);
        hash = 41 * hash + Objects.hashCode(this.complemento);
        hash = 41 * hash + Objects.hashCode(this.bairro);
        hash = 41 * hash + Objects.hashCode(this.uf);
        hash = 41 * hash + Objects.hashCode(this.cod_municipio);
        hash = 41 * hash + Objects.hashCode(this.cep);
        hash = 41 * hash + Objects.hashCode(this.tipo_exploracao);
        hash = 41 * hash + Objects.hashCode(this.participacao);
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
        final ImovelRural other = (ImovelRural) obj;
        if (!Objects.equals(this.reg, other.reg)) {
            return false;
        }
        if (!Objects.equals(this.pais, other.pais)) {
            return false;
        }
        if (!Objects.equals(this.moeda, other.moeda)) {
            return false;
        }
        if (!Objects.equals(this.nome_imovel, other.nome_imovel)) {
            return false;
        }
        if (!Objects.equals(this.endereco, other.endereco)) {
            return false;
        }
        if (!Objects.equals(this.numero, other.numero)) {
            return false;
        }
        if (!Objects.equals(this.complemento, other.complemento)) {
            return false;
        }
        if (!Objects.equals(this.bairro, other.bairro)) {
            return false;
        }
        if (!Objects.equals(this.uf, other.uf)) {
            return false;
        }
        if (!Objects.equals(this.cod_municipio, other.cod_municipio)) {
            return false;
        }
        if (!Objects.equals(this.cep, other.cep)) {
            return false;
        }
        if (!Objects.equals(this.cod_imovel, other.cod_imovel)) {
            return false;
        }
        if (!Objects.equals(this.cad_ITR, other.cad_ITR)) {
            return false;
        }
        if (!Objects.equals(this.atividade_economica_pf, other.atividade_economica_pf)) {
            return false;
        }
        if (!Objects.equals(this.inscricao_estadual, other.inscricao_estadual)) {
            return false;
        }
        if (this.tipo_exploracao != other.tipo_exploracao) {
            return false;
        }
        if (!Objects.equals(this.participacao, other.participacao)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ImovelRural{" + "reg=" + reg + ", cod_imovel=" + cod_imovel + ", pais=" + pais + ", moeda=" + moeda + ", cad_ITR=" + cad_ITR + ", atividade_economica_pf=" + atividade_economica_pf + ", inscricao_estadual=" + inscricao_estadual + ", nome_imovel=" + nome_imovel + ", endereco=" + endereco + ", numero=" + numero + ", complemento=" + complemento + ", bairro=" + bairro + ", uf=" + uf + ", cod_municipio=" + cod_municipio + ", cep=" + cep + ", tipo_exploracao=" + tipo_exploracao + ", participacao=" + participacao + '}';
    }

}
