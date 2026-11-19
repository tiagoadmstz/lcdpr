package io.github.tiagoadmstz.lcdpr.modelo;

import java.util.Objects;

import io.github.tiagoadmstz.lcdpr.tabela.FormaApuracao;

/**
 * Registro 0010: parâmetros de tributação.
 */
public final class ParametrosTributacao {

    private final FormaApuracao formaApuracao;

    /**
     * Construtor com todos os campos, na ordem declarada.
     */
    public ParametrosTributacao(
            FormaApuracao formaApuracao) {
        this.formaApuracao = formaApuracao;
    }

    /** Registro 0010, campo FORMA_APUR. */
    public FormaApuracao formaApuracao() {
        return formaApuracao;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ParametrosTributacao that = (ParametrosTributacao) o;
        return Objects.equals(formaApuracao, that.formaApuracao);
    }

    @Override
    public int hashCode() {
        return Objects.hash(formaApuracao);
    }

    @Override
    public String toString() {
        return "ParametrosTributacao[formaApuracao=" + formaApuracao
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
