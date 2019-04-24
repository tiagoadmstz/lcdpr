/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.dsc.lcdpr.components;

import br.com.dsc.lcdpr.enumerated.TIPO_DOCUMENTO;
import br.com.dsc.lcdpr.enumerated.TIPO_LANCAMENTO;
import br.com.dsc.lcdpr.interfaces.LcdprHandler;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 *
 * @author Tiago
 */
public class DemoLivroCaixa implements Serializable, LcdprHandler {

    private static final long serialVersionUID = 3804635706937859453L;
    private String reg; //t = 4, o = sim, p = Q100
    private LocalDate data; //t = 8, o = sim
    private String cod_imovel; //t = 3, o = sim
    private String cod_conta; //t = 3, o = sim
    private String numero_documento; //t = sem limite, o = nao
    private TIPO_DOCUMENTO tipo_documento; //t = 1, o = sim
    private String historico; //t = sem limite, o = sim
    private String id_participante; //t = 11/14, o = sim
    private TIPO_LANCAMENTO tipo_lancamento; //t = 1, o = sim
    private String valor_entrada; //t = 19d2, o = nao
    private String valor_saida; //t = 19d2, o = nao
    private String saldo_final; //t = 19d2, o = sim

    public DemoLivroCaixa() {
        reg = "Q100";
    }

    public String getReg() {
        return reg;
    }

    public void setReg(String reg) {
        this.reg = reg;
    }

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public String getCod_imovel() {
        return cod_imovel;
    }

    public void setCod_imovel(String cod_imovel) {
        this.cod_imovel = cod_imovel;
    }

    public String getCod_conta() {
        return cod_conta;
    }

    public void setCod_conta(String cod_conta) {
        this.cod_conta = cod_conta;
    }

    public String getNumero_documento() {
        return numero_documento;
    }

    public void setNumero_documento(String numero_documento) {
        this.numero_documento = numero_documento;
    }

    public TIPO_DOCUMENTO getTipo_documento() {
        return tipo_documento;
    }

    public void setTipo_documento(TIPO_DOCUMENTO tipo_documento) {
        this.tipo_documento = tipo_documento;
    }

    public String getHistorico() {
        return historico;
    }

    public void setHistorico(String historico) {
        this.historico = historico;
    }

    public String getId_participante() {
        return id_participante;
    }

    public void setId_participante(String id_participante) {
        this.id_participante = id_participante;
    }

    public TIPO_LANCAMENTO getTipo_lancamento() {
        return tipo_lancamento;
    }

    public void setTipo_lancamento(TIPO_LANCAMENTO tipo_lancamento) {
        this.tipo_lancamento = tipo_lancamento;
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
        hash = 37 * hash + Objects.hashCode(this.reg);
        hash = 37 * hash + Objects.hashCode(this.data);
        hash = 37 * hash + Objects.hashCode(this.cod_imovel);
        hash = 37 * hash + Objects.hashCode(this.cod_conta);
        hash = 37 * hash + Objects.hashCode(this.numero_documento);
        hash = 37 * hash + Objects.hashCode(this.tipo_documento);
        hash = 37 * hash + Objects.hashCode(this.historico);
        hash = 37 * hash + Objects.hashCode(this.id_participante);
        hash = 37 * hash + Objects.hashCode(this.tipo_lancamento);
        hash = 37 * hash + Objects.hashCode(this.valor_entrada);
        hash = 37 * hash + Objects.hashCode(this.valor_saida);
        hash = 37 * hash + Objects.hashCode(this.saldo_final);
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
        final DemoLivroCaixa other = (DemoLivroCaixa) obj;
        if (!Objects.equals(this.reg, other.reg)) {
            return false;
        }
        if (!Objects.equals(this.numero_documento, other.numero_documento)) {
            return false;
        }
        if (!Objects.equals(this.historico, other.historico)) {
            return false;
        }
        if (!Objects.equals(this.data, other.data)) {
            return false;
        }
        if (!Objects.equals(this.cod_imovel, other.cod_imovel)) {
            return false;
        }
        if (!Objects.equals(this.cod_conta, other.cod_conta)) {
            return false;
        }
        if (this.tipo_documento != other.tipo_documento) {
            return false;
        }
        if (!Objects.equals(this.id_participante, other.id_participante)) {
            return false;
        }
        if (this.tipo_lancamento != other.tipo_lancamento) {
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
        return "DemonstrativoLivroCaixa{" + "reg=" + reg + ", data=" + data + ", cod_imovel=" + cod_imovel + ", cod_conta=" + cod_conta + ", numero_documento=" + numero_documento + ", tipo_documento=" + tipo_documento + ", historico=" + historico + ", id_participante=" + id_participante + ", tipo_lancamento=" + tipo_lancamento + ", valor_entrada=" + valor_entrada + ", valor_saida=" + valor_saida + ", saldo_final=" + saldo_final + '}';
    }

}
