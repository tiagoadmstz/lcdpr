/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.dsc.lcdpr.lcdpr;

import br.com.dsc.lcdpr.blocos.AberturaIdentificacao;
import br.com.dsc.lcdpr.blocos.DemonstrativoLivroCaixa;
import br.com.dsc.lcdpr.blocos.EncerramentoArquivo;
import br.com.dsc.lcdpr.interfaces.LcdprHandler;
import java.io.Serializable;
import java.util.Objects;

/**
 *
 * @author Tiago
 */
public class LCDPR implements Serializable, LcdprHandler {

    private static final long serialVersionUID = 9197775706508460101L;
    private AberturaIdentificacao bloco0;
    private DemonstrativoLivroCaixa blocoq;
    private EncerramentoArquivo bloco9;

    public LCDPR() {
    }

    public LCDPR(AberturaIdentificacao bloco0, DemonstrativoLivroCaixa blocoq, EncerramentoArquivo bloco9) {
        this.bloco0 = bloco0;
        this.blocoq = blocoq;
        this.bloco9 = bloco9;
    }

    public AberturaIdentificacao getBloco0() {
        return bloco0;
    }

    public void setBloco0(AberturaIdentificacao bloco0) {
        this.bloco0 = bloco0;
    }

    public DemonstrativoLivroCaixa getBlocoq() {
        return blocoq;
    }

    public void setBlocoq(DemonstrativoLivroCaixa blocoq) {
        this.blocoq = blocoq;
    }

    public EncerramentoArquivo getBloco9() {
        return bloco9;
    }

    public void setBloco9(EncerramentoArquivo bloco9) {
        this.bloco9 = bloco9;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 83 * hash + Objects.hashCode(this.bloco0);
        hash = 83 * hash + Objects.hashCode(this.blocoq);
        hash = 83 * hash + Objects.hashCode(this.bloco9);
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
        final LCDPR other = (LCDPR) obj;
        if (!Objects.equals(this.bloco0, other.bloco0)) {
            return false;
        }
        if (!Objects.equals(this.blocoq, other.blocoq)) {
            return false;
        }
        if (!Objects.equals(this.bloco9, other.bloco9)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "LCDPR{" + "bloco0=" + bloco0 + ", blocoq=" + blocoq + ", bloco9=" + bloco9 + '}';
    }

}
