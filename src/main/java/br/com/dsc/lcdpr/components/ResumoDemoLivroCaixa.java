/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.dsc.lcdpr.components;

import br.com.dsc.lcdpr.interfaces.LcdprHandler;
import java.io.Serializable;
import java.util.Objects;

/**
 *
 * @author Tiago Q200
 */
public class ResumoDemoLivroCaixa implements Serializable, LcdprHandler {

    private static final long serialVersionUID = -268680092317624306L;
    private String reg; // t = 4, o = sim, p = Q200
    private String pais; // t = 3, o = sim
    private String mes; // t = 6, o = sim
    private String valor_entrada; // t = 19d2, o = nao
    private String valor_saida; // t = 19d2, o = nao
    private String saldo_final; // t = 19d2, o = nao

    public ResumoDemoLivroCaixa() {
        reg = "Q200";
    }

    public String getReg() {
        return reg;
    }

    public void setReg(String reg) {
        this.reg = reg;
    }

    public String getPais() {
        return pais;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }

    public String getMes() {
        return mes;
    }

    public void setMes(String mes) {
        this.mes = mes;
    }

    public String getValor_entrada() {
        return valor_entrada;
    }

    public void setValor_entrada(String valor_entrada) {
        this.valor_entrada = valor_entrada;
    }

    public String getValor_saida() {
        return valor_saida;
    }

    public void setValor_saida(String valor_saida) {
        this.valor_saida = valor_saida;
    }

    public String getSaldo_final() {
        return saldo_final;
    }

    public void setSaldo_final(String saldo_final) {
        this.saldo_final = saldo_final;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 29 * hash + Objects.hashCode(this.reg);
        hash = 29 * hash + Objects.hashCode(this.pais);
        hash = 29 * hash + Objects.hashCode(this.mes);
        hash = 29 * hash + Objects.hashCode(this.valor_entrada);
        hash = 29 * hash + Objects.hashCode(this.valor_saida);
        hash = 29 * hash + Objects.hashCode(this.saldo_final);
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
        final ResumoDemoLivroCaixa other = (ResumoDemoLivroCaixa) obj;
        if (!Objects.equals(this.reg, other.reg)) {
            return false;
        }
        if (!Objects.equals(this.pais, other.pais)) {
            return false;
        }
        if (!Objects.equals(this.mes, other.mes)) {
            return false;
        }
        if (!Objects.equals(this.valor_entrada, other.valor_entrada)) {
            return false;
        }
        if (!Objects.equals(this.valor_saida, other.valor_saida)) {
            return false;
        }
        if (!Objects.equals(this.saldo_final, other.saldo_final)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ResumoDemonstrativoLivroCaixa{" + "reg=" + reg + ", pais=" + pais + ", mes=" + mes + ", valor_entrada=" + valor_entrada + ", valor_saida=" + valor_saida + ", saldo_final=" + saldo_final + '}';
    }

}
