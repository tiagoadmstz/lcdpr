package io.github.tiagoadmstz.lcdpr.modelo;

import io.github.tiagoadmstz.lcdpr.tabela.FormaApuracao;

/**
 * Registro 0010: parâmetros de tributação.
 */
public record ParametrosTributacao(
        FormaApuracao formaApuracao
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
        builder.formaApuracao = formaApuracao;
        return builder;
    }

    /** Construtor fluente de ParametrosTributacao. */
    public static final class Builder {

        private FormaApuracao formaApuracao = FormaApuracao.LIVRO_CAIXA;

        /** Registro 0010, campo FORMA_APUR. */
        public Builder formaApuracao(FormaApuracao formaApuracao) {
            this.formaApuracao = formaApuracao;
            return this;
        }

        /**
         * Monta o registro sem validar o conteúdo; validar é papel do LcdprValidator.
         *
         * @return ParametrosTributacao montado
         */
        public ParametrosTributacao build() {
            return new ParametrosTributacao(
                    formaApuracao
            );
        }
    }
}
