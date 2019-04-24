/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.dsc.lcdpr.components;

import br.com.dsc.lcdpr.enumerated.INICIO_PERIODO;
import br.com.dsc.lcdpr.enumerated.SITUACAO_ESPECIAL;
import br.com.dsc.lcdpr.interfaces.LcdprHandler;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 *
 * @author Tiago
 */
public class IdentificacaoPessoaFisica implements Serializable, LcdprHandler {

    private static final long serialVersionUID = -1849600283483749425L;
    private String reg; //t = 4, o = sim, p = 0000
    private String nome_esc; //t = 5, o = sim, p = LCDPR
    private String cod_versao; //t = 4, o = sim, p = 0001
    private Long cpf; //t = 11, o = sim
    private String nome;
    private INICIO_PERIODO ind_sit_ini_per;
    private SITUACAO_ESPECIAL situacao_especial;
    private Integer dataSituacaoEspecial; //t = 8, o = não
    private LocalDate dataInicioPeriodo; //t = 8, o = sim
    private LocalDate dataFinalPeriodo; //t = 8, o = sim

    public IdentificacaoPessoaFisica() {
        reg = "0000";
        nome_esc = "LCDPR";
        cod_versao = "0001";
    }

    public String getReg() {
        return reg;
    }

    public void setReg(String reg) {
        this.reg = reg;
    }

    public String getNome_esc() {
        return nome_esc;
    }

    public void setNome_esc(String nome_esc) {
        this.nome_esc = nome_esc;
    }

    public String getCod_versao() {
        return cod_versao;
    }

    public void setCod_versao(String cod_versao) {
        this.cod_versao = cod_versao;
    }

    public Long getCpf() {
        return cpf;
    }

    public void setCpf(Long cpf) {
        this.cpf = cpf;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public INICIO_PERIODO getInd_sit_ini_per() {
        return ind_sit_ini_per;
    }

    public void setInd_sit_ini_per(INICIO_PERIODO ind_sit_ini_per) {
        this.ind_sit_ini_per = ind_sit_ini_per;
    }

    public SITUACAO_ESPECIAL getSituacao_especial() {
        return situacao_especial;
    }

    public void setSituacao_especial(SITUACAO_ESPECIAL situacao_especial) {
        this.situacao_especial = situacao_especial;
    }

    public Integer getDataSituacaoEspecial() {
        return dataSituacaoEspecial;
    }

    public void setDataSituacaoEspecial(Integer dataSituacaoEspecial) {
        this.dataSituacaoEspecial = dataSituacaoEspecial;
    }

    public LocalDate getDataInicioPeriodo() {
        return dataInicioPeriodo;
    }

    public void setDataInicioPeriodo(LocalDate dataInicioPeriodo) {
        this.dataInicioPeriodo = dataInicioPeriodo;
    }

    public LocalDate getDataFinalPeriodo() {
        return dataFinalPeriodo;
    }

    public void setDataFinalPeriodo(LocalDate dataFinalPeriodo) {
        this.dataFinalPeriodo = dataFinalPeriodo;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 13 * hash + Objects.hashCode(this.reg);
        hash = 13 * hash + Objects.hashCode(this.nome_esc);
        hash = 13 * hash + Objects.hashCode(this.cod_versao);
        hash = 13 * hash + Objects.hashCode(this.cpf);
        hash = 13 * hash + Objects.hashCode(this.nome);
        hash = 13 * hash + Objects.hashCode(this.ind_sit_ini_per);
        hash = 13 * hash + Objects.hashCode(this.situacao_especial);
        hash = 13 * hash + Objects.hashCode(this.dataSituacaoEspecial);
        hash = 13 * hash + Objects.hashCode(this.dataInicioPeriodo);
        hash = 13 * hash + Objects.hashCode(this.dataFinalPeriodo);
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
        final IdentificacaoPessoaFisica other = (IdentificacaoPessoaFisica) obj;
        if (!Objects.equals(this.reg, other.reg)) {
            return false;
        }
        if (!Objects.equals(this.nome_esc, other.nome_esc)) {
            return false;
        }
        if (!Objects.equals(this.cod_versao, other.cod_versao)) {
            return false;
        }
        if (!Objects.equals(this.nome, other.nome)) {
            return false;
        }
        if (!Objects.equals(this.cpf, other.cpf)) {
            return false;
        }
        if (this.ind_sit_ini_per != other.ind_sit_ini_per) {
            return false;
        }
        if (this.situacao_especial != other.situacao_especial) {
            return false;
        }
        if (!Objects.equals(this.dataSituacaoEspecial, other.dataSituacaoEspecial)) {
            return false;
        }
        if (!Objects.equals(this.dataInicioPeriodo, other.dataInicioPeriodo)) {
            return false;
        }
        if (!Objects.equals(this.dataFinalPeriodo, other.dataFinalPeriodo)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "IdentificacaoPessoaFisica{" + "reg=" + reg + ", nome_esc=" + nome_esc + ", cod_versao=" + cod_versao + ", cpf=" + cpf + ", nome=" + nome + ", ind_sit_ini_per=" + ind_sit_ini_per + ", situacao_especial=" + situacao_especial + ", dataSituacaoEspecial=" + dataSituacaoEspecial + ", dataInicioPeriodo=" + dataInicioPeriodo + ", dataFinalPeriodo=" + dataFinalPeriodo + '}';
    }

}
