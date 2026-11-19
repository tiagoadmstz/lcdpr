package io.github.tiagoadmstz.lcdpr.modelo;

/**
 * Registro 9999: signatário e encerramento do arquivo.
 */
public record EncerramentoArquivo(
        String nomeContador,
        String cpfCnpjContador,
        String crcContador,
        String emailContador,
        String telefoneContador,
        Long quantidadeLinhas
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
        builder.nomeContador = nomeContador;
        builder.cpfCnpjContador = cpfCnpjContador;
        builder.crcContador = crcContador;
        builder.emailContador = emailContador;
        builder.telefoneContador = telefoneContador;
        builder.quantidadeLinhas = quantidadeLinhas;
        return builder;
    }

    /** Construtor fluente de EncerramentoArquivo. */
    public static final class Builder {

        private String nomeContador;
        private String cpfCnpjContador;
        private String crcContador;
        private String emailContador;
        private String telefoneContador;
        private Long quantidadeLinhas;

        /** Registro 9999, campo IDENT_NOM. */
        public Builder nomeContador(String nomeContador) {
            this.nomeContador = nomeContador;
            return this;
        }

        /** Registro 9999, campo IDENT_CPF_CNPJ. */
        public Builder cpfCnpjContador(String cpfCnpjContador) {
            this.cpfCnpjContador = cpfCnpjContador;
            return this;
        }

        /** Registro 9999, campo IND_CRC. */
        public Builder crcContador(String crcContador) {
            this.crcContador = crcContador;
            return this;
        }

        /** Registro 9999, campo EMAIL. */
        public Builder emailContador(String emailContador) {
            this.emailContador = emailContador;
            return this;
        }

        /** Registro 9999, campo FONE. */
        public Builder telefoneContador(String telefoneContador) {
            this.telefoneContador = telefoneContador;
            return this;
        }

        /** Registro 9999, campo QTD_LIN. */
        public Builder quantidadeLinhas(Long quantidadeLinhas) {
            this.quantidadeLinhas = quantidadeLinhas;
            return this;
        }

        /**
         * Monta o registro sem validar o conteúdo; validar é papel do LcdprValidator.
         *
         * @return EncerramentoArquivo montado
         */
        public EncerramentoArquivo build() {
            return new EncerramentoArquivo(
                    nomeContador,
                    cpfCnpjContador,
                    crcContador,
                    emailContador,
                    telefoneContador,
                    quantidadeLinhas
            );
        }
    }
}
