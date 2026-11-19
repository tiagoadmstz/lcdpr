package io.github.tiagoadmstz.lcdpr.modelo;

import java.time.LocalDate;

import io.github.tiagoadmstz.lcdpr.tabela.IndicadorInicioPeriodo;
import io.github.tiagoadmstz.lcdpr.tabela.SituacaoEspecial;

/**
 * Registro 0000: abertura do arquivo e identificação da pessoa física.
 */
public record IdentificacaoPessoaFisica(
        String codigoVersao,
        String cpf,
        String nome,
        IndicadorInicioPeriodo indicadorInicioPeriodo,
        SituacaoEspecial situacaoEspecial,
        LocalDate dataSituacaoEspecial,
        LocalDate dataInicial,
        LocalDate dataFinal
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
        builder.codigoVersao = codigoVersao;
        builder.cpf = cpf;
        builder.nome = nome;
        builder.indicadorInicioPeriodo = indicadorInicioPeriodo;
        builder.situacaoEspecial = situacaoEspecial;
        builder.dataSituacaoEspecial = dataSituacaoEspecial;
        builder.dataInicial = dataInicial;
        builder.dataFinal = dataFinal;
        return builder;
    }

    /** Construtor fluente de IdentificacaoPessoaFisica. */
    public static final class Builder {

        private String codigoVersao = "0013";
        private String cpf;
        private String nome;
        private IndicadorInicioPeriodo indicadorInicioPeriodo = IndicadorInicioPeriodo.REGULAR;
        private SituacaoEspecial situacaoEspecial = SituacaoEspecial.NORMAL;
        private LocalDate dataSituacaoEspecial;
        private LocalDate dataInicial;
        private LocalDate dataFinal;

        /** Registro 0000, campo COD_VER. */
        public Builder codigoVersao(String codigoVersao) {
            this.codigoVersao = codigoVersao;
            return this;
        }

        /** Registro 0000, campo CPF. */
        public Builder cpf(String cpf) {
            this.cpf = cpf;
            return this;
        }

        /** Registro 0000, campo NOME. */
        public Builder nome(String nome) {
            this.nome = nome;
            return this;
        }

        /** Registro 0000, campo IND_SIT_INI_PER. */
        public Builder indicadorInicioPeriodo(IndicadorInicioPeriodo indicadorInicioPeriodo) {
            this.indicadorInicioPeriodo = indicadorInicioPeriodo;
            return this;
        }

        /** Registro 0000, campo SIT_ESPECIAL. */
        public Builder situacaoEspecial(SituacaoEspecial situacaoEspecial) {
            this.situacaoEspecial = situacaoEspecial;
            return this;
        }

        /** Registro 0000, campo DT_SIT_ESP. */
        public Builder dataSituacaoEspecial(LocalDate dataSituacaoEspecial) {
            this.dataSituacaoEspecial = dataSituacaoEspecial;
            return this;
        }

        /** Registro 0000, campo DT_INI. */
        public Builder dataInicial(LocalDate dataInicial) {
            this.dataInicial = dataInicial;
            return this;
        }

        /** Registro 0000, campo DT_FIN. */
        public Builder dataFinal(LocalDate dataFinal) {
            this.dataFinal = dataFinal;
            return this;
        }

        /**
         * Monta o registro sem validar o conteúdo; validar é papel do LcdprValidator.
         *
         * @return IdentificacaoPessoaFisica montado
         */
        public IdentificacaoPessoaFisica build() {
            return new IdentificacaoPessoaFisica(
                    codigoVersao,
                    cpf,
                    nome,
                    indicadorInicioPeriodo,
                    situacaoEspecial,
                    dataSituacaoEspecial,
                    dataInicial,
                    dataFinal
            );
        }
    }
}
