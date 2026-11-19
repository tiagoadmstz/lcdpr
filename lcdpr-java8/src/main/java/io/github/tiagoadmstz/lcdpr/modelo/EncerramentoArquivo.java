package io.github.tiagoadmstz.lcdpr.modelo;

import java.util.Objects;

/**
 * Registro 9999: signatário e encerramento do arquivo.
 */
public final class EncerramentoArquivo {

    private final String nomeContador;
    private final String cpfCnpjContador;
    private final String crcContador;
    private final String emailContador;
    private final String telefoneContador;
    private final Long quantidadeLinhas;

    /**
     * Construtor com todos os campos, na ordem declarada.
     */
    public EncerramentoArquivo(
            String nomeContador,
            String cpfCnpjContador,
            String crcContador,
            String emailContador,
            String telefoneContador,
            Long quantidadeLinhas) {
        this.nomeContador = nomeContador;
        this.cpfCnpjContador = cpfCnpjContador;
        this.crcContador = crcContador;
        this.emailContador = emailContador;
        this.telefoneContador = telefoneContador;
        this.quantidadeLinhas = quantidadeLinhas;
    }

    /** Registro 9999, campo IDENT_NOM. */
    public String nomeContador() {
        return nomeContador;
    }

    /** Registro 9999, campo IDENT_CPF_CNPJ. */
    public String cpfCnpjContador() {
        return cpfCnpjContador;
    }

    /** Registro 9999, campo IND_CRC. */
    public String crcContador() {
        return crcContador;
    }

    /** Registro 9999, campo EMAIL. */
    public String emailContador() {
        return emailContador;
    }

    /** Registro 9999, campo FONE. */
    public String telefoneContador() {
        return telefoneContador;
    }

    /** Registro 9999, campo QTD_LIN. */
    public Long quantidadeLinhas() {
        return quantidadeLinhas;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        EncerramentoArquivo that = (EncerramentoArquivo) o;
        return Objects.equals(nomeContador, that.nomeContador)
                && Objects.equals(cpfCnpjContador, that.cpfCnpjContador)
                && Objects.equals(crcContador, that.crcContador)
                && Objects.equals(emailContador, that.emailContador)
                && Objects.equals(telefoneContador, that.telefoneContador)
                && Objects.equals(quantidadeLinhas, that.quantidadeLinhas);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nomeContador, cpfCnpjContador, crcContador, emailContador, telefoneContador, quantidadeLinhas);
    }

    @Override
    public String toString() {
        return "EncerramentoArquivo[nomeContador=" + nomeContador
                + ", cpfCnpjContador=" + cpfCnpjContador
                + ", crcContador=" + crcContador
                + ", emailContador=" + emailContador
                + ", telefoneContador=" + telefoneContador
                + ", quantidadeLinhas=" + quantidadeLinhas
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
