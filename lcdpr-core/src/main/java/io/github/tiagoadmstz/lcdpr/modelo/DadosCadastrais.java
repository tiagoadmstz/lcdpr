package io.github.tiagoadmstz.lcdpr.modelo;

/**
 * Registro 0030: dados cadastrais do contribuinte.
 */
public record DadosCadastrais(
        String endereco,
        String numero,
        String complemento,
        String bairro,
        String uf,
        String codigoMunicipio,
        String cep,
        String telefone,
        String email
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
        builder.endereco = endereco;
        builder.numero = numero;
        builder.complemento = complemento;
        builder.bairro = bairro;
        builder.uf = uf;
        builder.codigoMunicipio = codigoMunicipio;
        builder.cep = cep;
        builder.telefone = telefone;
        builder.email = email;
        return builder;
    }

    /** Construtor fluente de DadosCadastrais. */
    public static final class Builder {

        private String endereco;
        private String numero;
        private String complemento;
        private String bairro;
        private String uf;
        private String codigoMunicipio;
        private String cep;
        private String telefone;
        private String email;

        /** Registro 0030, campo ENDERECO. */
        public Builder endereco(String endereco) {
            this.endereco = endereco;
            return this;
        }

        /** Registro 0030, campo NUM. */
        public Builder numero(String numero) {
            this.numero = numero;
            return this;
        }

        /** Registro 0030, campo COMPL. */
        public Builder complemento(String complemento) {
            this.complemento = complemento;
            return this;
        }

        /** Registro 0030, campo BAIRRO. */
        public Builder bairro(String bairro) {
            this.bairro = bairro;
            return this;
        }

        /** Registro 0030, campo UF. */
        public Builder uf(String uf) {
            this.uf = uf;
            return this;
        }

        /** Registro 0030, campo COD_MUN. */
        public Builder codigoMunicipio(String codigoMunicipio) {
            this.codigoMunicipio = codigoMunicipio;
            return this;
        }

        /** Registro 0030, campo CEP. */
        public Builder cep(String cep) {
            this.cep = cep;
            return this;
        }

        /** Registro 0030, campo NUM_TEL. */
        public Builder telefone(String telefone) {
            this.telefone = telefone;
            return this;
        }

        /** Registro 0030, campo EMAIL. */
        public Builder email(String email) {
            this.email = email;
            return this;
        }

        /**
         * Monta o registro sem validar o conteúdo; validar é papel do LcdprValidator.
         *
         * @return DadosCadastrais montado
         */
        public DadosCadastrais build() {
            return new DadosCadastrais(
                    endereco,
                    numero,
                    complemento,
                    bairro,
                    uf,
                    codigoMunicipio,
                    cep,
                    telefone,
                    email
            );
        }
    }
}
