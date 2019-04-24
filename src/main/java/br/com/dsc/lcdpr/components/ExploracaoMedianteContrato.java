/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.dsc.lcdpr.components;

import br.com.dsc.lcdpr.enumerated.TIPO_CONTRAPARTE;
import br.com.dsc.lcdpr.interfaces.LcdprHandler;
import java.io.Serializable;
import java.util.Objects;

/**
 *
 * @author Tiago
 */
public class ExploracaoMedianteContrato implements Serializable, LcdprHandler {

    private static final long serialVersionUID = -784279936625837037L;
    private String reg; //t = 4, o = sim, p = 0045
    private String cod_imovel; //t = 3, o = sim
    private TIPO_CONTRAPARTE tipo_contraparte; //t = 1, o = sim
    private String cpf_contraparte; //t = 11, o = sim
    private String nome_contraparte; //t = 50, o = sim
    private String percentual_contraparte; //t = 3, o = sim

    public ExploracaoMedianteContrato() {
        reg = "0045";
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

    public TIPO_CONTRAPARTE getTipo_contraparte() {
        return tipo_contraparte;
    }

    public void setTipo_contraparte(TIPO_CONTRAPARTE tipo_contraparte) {
        this.tipo_contraparte = tipo_contraparte;
    }

    public String getCpf_contraparte() {
        return cpf_contraparte;
    }

    public void setCpf_contraparte(String cpf_contraparte) {
        this.cpf_contraparte = cpf_contraparte;
    }

    public String getNome_contraparte() {
        return nome_contraparte;
    }

    public void setNome_contraparte(String nome_contraparte) {
        this.nome_contraparte = nome_contraparte;
    }

    public String getPercentual_contraparte() {
        return percentual_contraparte;
    }

    public void setPercentual_contraparte(String percentual_contraparte) {
        this.percentual_contraparte = percentual_contraparte;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 11 * hash + Objects.hashCode(this.reg);
        hash = 11 * hash + Objects.hashCode(this.cod_imovel);
        hash = 11 * hash + Objects.hashCode(this.tipo_contraparte);
        hash = 11 * hash + Objects.hashCode(this.cpf_contraparte);
        hash = 11 * hash + Objects.hashCode(this.nome_contraparte);
        hash = 11 * hash + Objects.hashCode(this.percentual_contraparte);
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
        final ExploracaoMedianteContrato other = (ExploracaoMedianteContrato) obj;
        if (!Objects.equals(this.nome_contraparte, other.nome_contraparte)) {
            return false;
        }
        if (!Objects.equals(this.reg, other.reg)) {
            return false;
        }
        if (!Objects.equals(this.cod_imovel, other.cod_imovel)) {
            return false;
        }
        if (this.tipo_contraparte != other.tipo_contraparte) {
            return false;
        }
        if (!Objects.equals(this.cpf_contraparte, other.cpf_contraparte)) {
            return false;
        }
        if (!Objects.equals(this.percentual_contraparte, other.percentual_contraparte)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ExploracaoMedianteContrato{" + "reg=" + reg + ", cod_imovel=" + cod_imovel + ", tipo_contraparte=" + tipo_contraparte + ", cpf_contraparte=" + cpf_contraparte + ", nome_contraparte=" + nome_contraparte + ", percentual_contraparte=" + percentual_contraparte + '}';
    }
    
}
