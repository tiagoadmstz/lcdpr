package io.github.tiagoadmstz.lcdpr.modelo;

import java.util.Objects;

/**
 * Registro 0030: dados cadastrais do contribuinte.
 */
public final class DadosCadastrais {

    private final String endereco;
    private final String numero;
    private final String complemento;
    private final String bairro;
    private final String uf;
    private final String codigoMunicipio;
    private final String cep;
    private final String telefone;
    private final String email;

    /**
     * Construtor com todos os campos, na ordem declarada.
     */
    public DadosCadastrais(
            String endereco,
            String numero,
            String complemento,
            String bairro,
            String uf,
            String codigoMunicipio,
            String cep,
            String telefone,
            String email) {
        this.endereco = endereco;
        this.numero = numero;
        this.complemento = complemento;
        this.bairro = bairro;
        this.uf = uf;
        this.codigoMunicipio = codigoMunicipio;
        this.cep = cep;
        this.telefone = telefone;
        this.email = email;
    }

    /** Registro 0030, campo ENDERECO. */
    public String endereco() {
        return endereco;
    }

    /** Registro 0030, campo NUM. */
    public String numero() {
        return numero;
    }

    /** Registro 0030, campo COMPL. */
    public String complemento() {
        return complemento;
    }

    /** Registro 0030, campo BAIRRO. */
    public String bairro() {
        return bairro;
    }

    /** Registro 0030, campo UF. */
    public String uf() {
        return uf;
    }

    /** Registro 0030, campo COD_MUN. */
    public String codigoMunicipio() {
        return codigoMunicipio;
    }

    /** Registro 0030, campo CEP. */
    public String cep() {
        return cep;
    }

    /** Registro 0030, campo NUM_TEL. */
    public String telefone() {
        return telefone;
    }

    /** Registro 0030, campo EMAIL. */
    public String email() {
        return email;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        DadosCadastrais that = (DadosCadastrais) o;
        return Objects.equals(endereco, that.endereco)
                && Objects.equals(numero, that.numero)
                && Objects.equals(complemento, that.complemento)
                && Objects.equals(bairro, that.bairro)
                && Objects.equals(uf, that.uf)
                && Objects.equals(codigoMunicipio, that.codigoMunicipio)
                && Objects.equals(cep, that.cep)
                && Objects.equals(telefone, that.telefone)
                && Objects.equals(email, that.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(endereco, numero, complemento, bairro, uf, codigoMunicipio, cep, telefone, email);
    }

    @Override
    public String toString() {
        return "DadosCadastrais[endereco=" + endereco
                + ", numero=" + numero
                + ", complemento=" + complemento
                + ", bairro=" + bairro
                + ", uf=" + uf
                + ", codigoMunicipio=" + codigoMunicipio
                + ", cep=" + cep
                + ", telefone=" + telefone
                + ", email=" + email
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
