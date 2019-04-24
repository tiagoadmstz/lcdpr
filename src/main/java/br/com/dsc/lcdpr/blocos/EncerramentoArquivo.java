/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.dsc.lcdpr.blocos;

import br.com.dsc.lcdpr.interfaces.LcdprHandler;
import java.io.Serializable;
import java.util.Objects;

/**
 *
 * @author Tiago
 */
public class EncerramentoArquivo implements Serializable, LcdprHandler {

    private static final long serialVersionUID = 136224468484970674L;
    private String reg; // t = 4, o = sim, p = 9999
    private Integer quantidade_linhas; // t = sem limite, o = sim

    public EncerramentoArquivo() {
        reg = "9999";
    }

    public String getReg() {
        return reg;
    }

    public void setReg(String reg) {
        this.reg = reg;
    }

    public Integer getQuantidade_linhas() {
        return quantidade_linhas;
    }

    public void setQuantidade_linhas(Integer quantidade_linhas) {
        this.quantidade_linhas = quantidade_linhas;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 71 * hash + Objects.hashCode(this.reg);
        hash = 71 * hash + Objects.hashCode(this.quantidade_linhas);
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
        final EncerramentoArquivo other = (EncerramentoArquivo) obj;
        if (!Objects.equals(this.reg, other.reg)) {
            return false;
        }
        if (!Objects.equals(this.quantidade_linhas, other.quantidade_linhas)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "EncerramentoArquivo{" + "reg=" + reg + ", quantidade_linhas=" + quantidade_linhas + '}';
    }

}
