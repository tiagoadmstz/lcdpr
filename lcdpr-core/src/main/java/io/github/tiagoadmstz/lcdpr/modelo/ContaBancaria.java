package io.github.tiagoadmstz.lcdpr.modelo;

/**
 * Registro 0050: contas bancárias.
 */
public record ContaBancaria(
        String codigoConta,
        String pais,
        String banco,
        String nomeBanco,
        String agencia,
        String numeroConta
) {

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
