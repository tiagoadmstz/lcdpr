/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.dsc.lcdpr.components;

import br.com.dsc.lcdpr.enumerated.FORMA_APURACAO;
import br.com.dsc.lcdpr.interfaces.LcdprHandler;
import java.io.Serializable;
import java.util.Objects;

/**
 *
 * @author Tiago
 */
public class ParametrosTributacao implements Serializable, LcdprHandler {

    private static final long serialVersionUID = 901801438322291828L;
    private String reg; //t = 4, o = sim, p = 0010
    private String hashIrrfAnterior; //t = 40, o = nao, p = 0010
    private FORMA_APURACAO forma_apuracao; //t = 1, o = sim

    public ParametrosTributacao() {
        reg = "0010";
    }

    public String getReg() {
        return reg;
    }

    public void setReg(String reg) {
        this.reg = reg;
    }

    public String getHashIrrfAnterior() {
        return hashIrrfAnterior;
    }

    public void setHashIrrfAnterior(String hashIrrfAnterior) {
        this.hashIrrfAnterior = hashIrrfAnterior;
    }

    public FORMA_APURACAO getForma_apuracao() {
        return forma_apuracao;
    }

    public void setForma_apuracao(FORMA_APURACAO forma_apuracao) {
        this.forma_apuracao = forma_apuracao;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 73 * hash + Objects.hashCode(this.reg);
        hash = 73 * hash + Objects.hashCode(this.hashIrrfAnterior);
        hash = 73 * hash + Objects.hashCode(this.forma_apuracao);
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
        final ParametrosTributacao other = (ParametrosTributacao) obj;
        if (!Objects.equals(this.reg, other.reg)) {
            return false;
        }
        if (!Objects.equals(this.hashIrrfAnterior, other.hashIrrfAnterior)) {
            return false;
        }
        if (this.forma_apuracao != other.forma_apuracao) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ParametrosTributacao{" + "reg=" + reg + ", hashIrrfAnterior=" + hashIrrfAnterior + ", forma_apuracao=" + forma_apuracao + '}';
    }

}
