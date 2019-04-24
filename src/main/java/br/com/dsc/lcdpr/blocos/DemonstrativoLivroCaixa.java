/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.dsc.lcdpr.blocos;

import br.com.dsc.lcdpr.components.DemoLivroCaixa;
import br.com.dsc.lcdpr.components.ResumoDemoLivroCaixa;
import br.com.dsc.lcdpr.interfaces.LcdprHandler;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;

/**
 *
 * @author Tiago
 */
public class DemonstrativoLivroCaixa implements Serializable, LcdprHandler {

    private static final long serialVersionUID = 6287575698325024956L;
    private List<DemoLivroCaixa> demonstrativo_livro_caixa; //H = 2, Q100, 1:N, o
    private List<ResumoDemoLivroCaixa> resumo_demonstrativo_livro_caixa; //H = 2, Q200, 1:N, o

    public DemonstrativoLivroCaixa() {
    }

    public DemonstrativoLivroCaixa(List<DemoLivroCaixa> demonstrativo_livro_caixa, List<ResumoDemoLivroCaixa> resumo_demonstrativo_livro_caixa) {
        this.demonstrativo_livro_caixa = demonstrativo_livro_caixa;
        this.resumo_demonstrativo_livro_caixa = resumo_demonstrativo_livro_caixa;
    }

    public List<DemoLivroCaixa> getDemonstrativo_livro_caixa() {
        return demonstrativo_livro_caixa;
    }

    public void setDemonstrativo_livro_caixa(List<DemoLivroCaixa> demonstrativo_livro_caixa) {
        this.demonstrativo_livro_caixa = demonstrativo_livro_caixa;
    }

    public List<ResumoDemoLivroCaixa> getResumo_demonstrativo_livro_caixa() {
        return resumo_demonstrativo_livro_caixa;
    }

    public void setResumo_demonstrativo_livro_caixa(List<ResumoDemoLivroCaixa> resumo_demonstrativo_livro_caixa) {
        this.resumo_demonstrativo_livro_caixa = resumo_demonstrativo_livro_caixa;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 67 * hash + Objects.hashCode(this.demonstrativo_livro_caixa);
        hash = 67 * hash + Objects.hashCode(this.resumo_demonstrativo_livro_caixa);
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
        final DemonstrativoLivroCaixa other = (DemonstrativoLivroCaixa) obj;
        if (!Objects.equals(this.demonstrativo_livro_caixa, other.demonstrativo_livro_caixa)) {
            return false;
        }
        if (!Objects.equals(this.resumo_demonstrativo_livro_caixa, other.resumo_demonstrativo_livro_caixa)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "DemonstrativoLivroCaixa{" + "demonstrativo_livro_caixa=" + demonstrativo_livro_caixa + ", resumo_demonstrativo_livro_caixa=" + resumo_demonstrativo_livro_caixa + '}';
    }

}
