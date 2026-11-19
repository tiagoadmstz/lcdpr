package io.github.tiagoadmstz.lcdpr.modelo;

import java.math.BigDecimal;
import java.math.RoundingMode;

import io.github.tiagoadmstz.lcdpr.tabela.TipoContraparte;

/**
 * Registro 0045: cadastro de terceiros que exploram o imóvel em conjunto.
 */
public record Terceiro(
        String codigoImovel,
        TipoContraparte tipoContraparte,
        String cpfCnpj,
        String nome,
        BigDecimal participacao
) {

    /** Normaliza os valores para escala 2. */
    public Terceiro {
        participacao = participacao == null ? null : participacao.setScale(2, RoundingMode.HALF_UP);
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
