package io.github.tiagoadmstz.lcdpr.modelo;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

import io.github.tiagoadmstz.lcdpr.tabela.TipoContraparte;

/**
 * Registro 0045: cadastro de terceiros que exploram o imóvel em conjunto.
 */
public final class Terceiro {

    private final String codigoImovel;
    private final TipoContraparte tipoContraparte;
    private final String cpfCnpj;
    private final String nome;
    private final BigDecimal participacao;

    /**
     * Normaliza os valores para escala 2.
     */
    public Terceiro(
            String codigoImovel,
            TipoContraparte tipoContraparte,
            String cpfCnpj,
            String nome,
            BigDecimal participacao) {
        participacao = participacao == null ? null : participacao.setScale(2, RoundingMode.HALF_UP);
        this.codigoImovel = codigoImovel;
        this.tipoContraparte = tipoContraparte;
        this.cpfCnpj = cpfCnpj;
        this.nome = nome;
        this.participacao = participacao;
    }

    /** Registro 0045, campo COD_IMOVEL. */
    public String codigoImovel() {
        return codigoImovel;
    }

    /** Registro 0045, campo TIPO_CONTRAPARTE. */
    public TipoContraparte tipoContraparte() {
        return tipoContraparte;
    }

    /** Registro 0045, campo ID_CONTRAPARTE. */
    public String cpfCnpj() {
        return cpfCnpj;
    }

    /** Registro 0045, campo NOME_CONTRAPARTE. */
    public String nome() {
        return nome;
    }

    /** Registro 0045, campo PERC_CONTRAPARTE. */
    public BigDecimal participacao() {
        return participacao;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Terceiro that = (Terceiro) o;
        return Objects.equals(codigoImovel, that.codigoImovel)
                && Objects.equals(tipoContraparte, that.tipoContraparte)
                && Objects.equals(cpfCnpj, that.cpfCnpj)
                && Objects.equals(nome, that.nome)
                && Objects.equals(participacao, that.participacao);
    }

    @Override
    public int hashCode() {
        return Objects.hash(codigoImovel, tipoContraparte, cpfCnpj, nome, participacao);
    }

    @Override
    public String toString() {
        return "Terceiro[codigoImovel=" + codigoImovel
                + ", tipoContraparte=" + tipoContraparte
                + ", cpfCnpj=" + cpfCnpj
                + ", nome=" + nome
                + ", participacao=" + participacao
                + "]";
    }

    /**
     * Cria um builder com os defaults do leiaute.
     *
     * @return builder vazio
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Cria um builder com os valores deste registro.
     *
     * @return novo builder
     */
    public Builder toBuilder() {
        Builder builder = new Builder();
        builder.codigoImovel = codigoImovel;
        builder.tipoContraparte = tipoContraparte;
        builder.cpfCnpj = cpfCnpj;
        builder.nome = nome;
        builder.participacao = participacao;
        return builder;
    }

    /** Construtor fluente de Terceiro. */
    public static final class Builder {

        private String codigoImovel;
        private TipoContraparte tipoContraparte;
        private String cpfCnpj;
        private String nome;
        private BigDecimal participacao;

        /** Registro 0045, campo COD_IMOVEL. */
        public Builder codigoImovel(String codigoImovel) {
            this.codigoImovel = codigoImovel;
            return this;
        }

        /** Registro 0045, campo TIPO_CONTRAPARTE. */
        public Builder tipoContraparte(TipoContraparte tipoContraparte) {
            this.tipoContraparte = tipoContraparte;
            return this;
        }

        /** Registro 0045, campo ID_CONTRAPARTE. */
        public Builder cpfCnpj(String cpfCnpj) {
            this.cpfCnpj = cpfCnpj;
            return this;
        }

        /** Registro 0045, campo NOME_CONTRAPARTE. */
        public Builder nome(String nome) {
            this.nome = nome;
            return this;
        }

        /** Registro 0045, campo PERC_CONTRAPARTE. */
        public Builder participacao(BigDecimal participacao) {
            this.participacao = participacao;
            return this;
        }

        /**
         * Monta o registro sem validar o conteúdo; validar é papel do LcdprValidator.
         *
         * @return Terceiro montado
         */
        public Terceiro build() {
            return new Terceiro(
                    codigoImovel,
                    tipoContraparte,
                    cpfCnpj,
                    nome,
                    participacao
            );
        }
    }
}
