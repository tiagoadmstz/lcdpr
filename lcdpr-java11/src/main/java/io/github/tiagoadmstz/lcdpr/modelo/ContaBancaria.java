package io.github.tiagoadmstz.lcdpr.modelo;

import java.util.Objects;

/**
 * Registro 0050: contas bancárias.
 */
public final class ContaBancaria {

    private final String codigoConta;
    private final String pais;
    private final String banco;
    private final String nomeBanco;
    private final String agencia;
    private final String numeroConta;

    /**
     * Construtor com todos os campos, na ordem declarada.
     */
    public ContaBancaria(
            String codigoConta,
            String pais,
            String banco,
            String nomeBanco,
            String agencia,
            String numeroConta) {
        this.codigoConta = codigoConta;
        this.pais = pais;
        this.banco = banco;
        this.nomeBanco = nomeBanco;
        this.agencia = agencia;
        this.numeroConta = numeroConta;
    }

    /** Registro 0050, campo COD_CONTA. */
    public String codigoConta() {
        return codigoConta;
    }

    /** Registro 0050, campo PAIS_CTA. */
    public String pais() {
        return pais;
    }

    /** Registro 0050, campo BANCO. */
    public String banco() {
        return banco;
    }

    /** Registro 0050, campo NOME_BANCO. */
    public String nomeBanco() {
        return nomeBanco;
    }

    /** Registro 0050, campo AGENCIA. */
    public String agencia() {
        return agencia;
    }

    /** Registro 0050, campo NUM_CONTA. */
    public String numeroConta() {
        return numeroConta;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ContaBancaria that = (ContaBancaria) o;
        return Objects.equals(codigoConta, that.codigoConta)
                && Objects.equals(pais, that.pais)
                && Objects.equals(banco, that.banco)
                && Objects.equals(nomeBanco, that.nomeBanco)
                && Objects.equals(agencia, that.agencia)
                && Objects.equals(numeroConta, that.numeroConta);
    }

    @Override
    public int hashCode() {
        return Objects.hash(codigoConta, pais, banco, nomeBanco, agencia, numeroConta);
    }

    @Override
    public String toString() {
        return "ContaBancaria[codigoConta=" + codigoConta
                + ", pais=" + pais
                + ", banco=" + banco
                + ", nomeBanco=" + nomeBanco
                + ", agencia=" + agencia
                + ", numeroConta=" + numeroConta
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
        builder.codigoConta = codigoConta;
        builder.pais = pais;
        builder.banco = banco;
        builder.nomeBanco = nomeBanco;
        builder.agencia = agencia;
        builder.numeroConta = numeroConta;
        return builder;
    }

    /** Construtor fluente de ContaBancaria. */
    public static final class Builder {

        private String codigoConta;
        private String pais = "BR";
        private String banco;
        private String nomeBanco;
        private String agencia;
        private String numeroConta;

        /** Registro 0050, campo COD_CONTA. */
        public Builder codigoConta(String codigoConta) {
            this.codigoConta = codigoConta;
            return this;
        }

        /** Registro 0050, campo PAIS_CTA. */
        public Builder pais(String pais) {
            this.pais = pais;
            return this;
        }

        /** Registro 0050, campo BANCO. */
        public Builder banco(String banco) {
            this.banco = banco;
            return this;
        }

        /** Registro 0050, campo NOME_BANCO. */
        public Builder nomeBanco(String nomeBanco) {
            this.nomeBanco = nomeBanco;
            return this;
        }

        /** Registro 0050, campo AGENCIA. */
        public Builder agencia(String agencia) {
            this.agencia = agencia;
            return this;
        }

        /** Registro 0050, campo NUM_CONTA. */
        public Builder numeroConta(String numeroConta) {
            this.numeroConta = numeroConta;
            return this;
        }

        /**
         * Monta o registro sem validar o conteúdo; validar é papel do LcdprValidator.
         *
         * @return ContaBancaria montado
         */
        public ContaBancaria build() {
            return new ContaBancaria(
                    codigoConta,
                    pais,
                    banco,
                    nomeBanco,
                    agencia,
                    numeroConta
            );
        }
    }
}
