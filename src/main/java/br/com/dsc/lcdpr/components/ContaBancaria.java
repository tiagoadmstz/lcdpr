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
 * @author Tiago
 */
public class ContaBancaria implements Serializable, LcdprHandler {

    private static final long serialVersionUID = -3013153228791071332L;
    private String reg; // t = 4, o = sim, p = 0050
    private String cod_conta; // t = 3, o = sim
    private String pais_cta; // t = 3, o = sim
    private String banco; // t = 3, o = sim(BRA)
    private String nome_banco; // t = 30, o = sim
    private String agencia; // t = 4, o = sim
    private String numero_conta; // t = 16, o = sim

    public ContaBancaria() {
        reg = "0050";
    }

    public String getReg() {
        return reg;
    }

    public void setReg(String reg) {
        this.reg = reg;
    }

    public String getCod_conta() {
        return cod_conta;
    }

    public void setCod_conta(String cod_conta) {
        this.cod_conta = cod_conta;
    }

    public String getPais_cta() {
        return pais_cta;
    }

    public void setPais_cta(String pais_cta) {
        this.pais_cta = pais_cta;
    }

    public String getBanco() {
        return banco;
    }

    public void setBanco(String banco) {
        this.banco = banco;
    }

    public String getNome_banco() {
        return nome_banco;
    }

    public void setNome_banco(String nome_banco) {
        this.nome_banco = nome_banco;
    }

    public String getAgencia() {
        return agencia;
    }

    public void setAgencia(String agencia) {
        this.agencia = agencia;
    }

    public String getNumero_conta() {
        return numero_conta;
    }

    public void setNumero_conta(String numero_conta) {
        this.numero_conta = numero_conta;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 17 * hash + Objects.hashCode(this.reg);
        hash = 17 * hash + Objects.hashCode(this.cod_conta);
        hash = 17 * hash + Objects.hashCode(this.pais_cta);
        hash = 17 * hash + Objects.hashCode(this.banco);
        hash = 17 * hash + Objects.hashCode(this.nome_banco);
        hash = 17 * hash + Objects.hashCode(this.agencia);
        hash = 17 * hash + Objects.hashCode(this.numero_conta);
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
        final ContaBancaria other = (ContaBancaria) obj;
        if (!Objects.equals(this.reg, other.reg)) {
            return false;
        }
        if (!Objects.equals(this.pais_cta, other.pais_cta)) {
            return false;
        }
        if (!Objects.equals(this.nome_banco, other.nome_banco)) {
            return false;
        }
        if (!Objects.equals(this.cod_conta, other.cod_conta)) {
            return false;
        }
        if (!Objects.equals(this.banco, other.banco)) {
            return false;
        }
        if (!Objects.equals(this.agencia, other.agencia)) {
            return false;
        }
        if (!Objects.equals(this.numero_conta, other.numero_conta)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ContaBancaria{" + "reg=" + reg + ", cod_conta=" + cod_conta + ", pais_cta=" + pais_cta + ", banco=" + banco + ", nome_banco=" + nome_banco + ", agencia=" + agencia + ", numero_conta=" + numero_conta + '}';
    }

}
