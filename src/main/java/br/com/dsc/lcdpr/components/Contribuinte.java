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
public class Contribuinte implements Serializable, LcdprHandler {

    private static final long serialVersionUID = 8869191708501012066L;
    private String reg; //t = 4, o = sim, p = 0030
    private String endereco; //t = 150, o = sim
    private String numero; //t = 6, o = sim
    private String complemento; //t = 50, o = não
    private String bairro; //t = 50, o = sim
    private String uf; //t = 2, o = sim
    private String codigo_municipio; //t = 7, o = sim
    private String cep; //t = 8, o = sim
    private Long numero_telefone; //t = 15, o = não
    private String email; //t = 115, o = sim

    public Contribuinte() {
        reg = "0030";
    }

    public String getReg() {
        return reg;
    }

    public void setReg(String reg) {
        this.reg = reg;
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

    public String getCodigo_municipio() {
        return codigo_municipio;
    }

    public void setCodigo_municipio(String codigo_municipio) {
        this.codigo_municipio = codigo_municipio;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public Long getNumero_telefone() {
        return numero_telefone;
    }

    public void setNumero_telefone(Long numero_telefone) {
        this.numero_telefone = numero_telefone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 67 * hash + Objects.hashCode(this.reg);
        hash = 67 * hash + Objects.hashCode(this.endereco);
        hash = 67 * hash + Objects.hashCode(this.numero);
        hash = 67 * hash + Objects.hashCode(this.complemento);
        hash = 67 * hash + Objects.hashCode(this.bairro);
        hash = 67 * hash + Objects.hashCode(this.uf);
        hash = 67 * hash + Objects.hashCode(this.codigo_municipio);
        hash = 67 * hash + Objects.hashCode(this.cep);
        hash = 67 * hash + Objects.hashCode(this.numero_telefone);
        hash = 67 * hash + Objects.hashCode(this.email);
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
        final Contribuinte other = (Contribuinte) obj;
        if (!Objects.equals(this.reg, other.reg)) {
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
        if (!Objects.equals(this.codigo_municipio, other.codigo_municipio)) {
            return false;
        }
        if (!Objects.equals(this.cep, other.cep)) {
            return false;
        }
        if (!Objects.equals(this.email, other.email)) {
            return false;
        }
        if (!Objects.equals(this.numero_telefone, other.numero_telefone)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Contribuinte{" + "reg=" + reg + ", endereco=" + endereco + ", numero=" + numero + ", complemento=" + complemento + ", bairro=" + bairro + ", uf=" + uf + ", codigo_municipio=" + codigo_municipio + ", cep=" + cep + ", numero_telefone=" + numero_telefone + ", email=" + email + '}';
    }

}
