package io.github.tiagoadmstz.lcdpr;

import io.github.tiagoadmstz.lcdpr.modelo.AberturaIdentificacao;
import io.github.tiagoadmstz.lcdpr.modelo.DemonstrativoResultado;
import io.github.tiagoadmstz.lcdpr.modelo.EncerramentoArquivo;

/**
 * Livro Caixa Digital do Produtor Rural: bloco 0, bloco Q e bloco 9 do arquivo.
 */
public record Lcdpr(
        AberturaIdentificacao bloco0,
        DemonstrativoResultado blocoQ,
        EncerramentoArquivo bloco9
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
        builder.bloco0 = bloco0;
        builder.blocoQ = blocoQ;
        builder.bloco9 = bloco9;
        return builder;
    }

    /** Construtor fluente de Lcdpr. */
    public static final class Builder {

        private AberturaIdentificacao bloco0;
        private DemonstrativoResultado blocoQ;
        private EncerramentoArquivo bloco9;

        /** Bloco 0 (registros 0000 a 0050). */
        public Builder bloco0(AberturaIdentificacao bloco0) {
            this.bloco0 = bloco0;
            return this;
        }

        /** Bloco Q (registros Q100 e Q200). */
        public Builder blocoQ(DemonstrativoResultado blocoQ) {
            this.blocoQ = blocoQ;
            return this;
        }

        /** Registro 9999. */
        public Builder bloco9(EncerramentoArquivo bloco9) {
            this.bloco9 = bloco9;
            return this;
        }

        /**
         * Monta o registro sem validar o conteúdo; validar é papel do LcdprValidator.
         *
         * @return Lcdpr montado
         */
        public Lcdpr build() {
            return new Lcdpr(
                    bloco0,
                    blocoQ,
                    bloco9
            );
        }
    }
}
