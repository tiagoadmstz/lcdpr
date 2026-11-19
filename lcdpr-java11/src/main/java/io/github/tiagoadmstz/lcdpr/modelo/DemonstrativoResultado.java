package io.github.tiagoadmstz.lcdpr.modelo;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Bloco Q: demonstrativo do resultado da atividade rural (registros Q100 e Q200).
 */
public final class DemonstrativoResultado {

    private final List<Lancamento> lancamentos;
    private final List<ResumoMensal> resumosMensais;

    /**
     * Copia as listas (nulas viram vazias).
     */
    public DemonstrativoResultado(
            List<Lancamento> lancamentos,
            List<ResumoMensal> resumosMensais) {
        lancamentos = lancamentos == null ? List.of() : List.copyOf(lancamentos);
        resumosMensais = resumosMensais == null ? List.of() : List.copyOf(resumosMensais);
        this.lancamentos = lancamentos;
        this.resumosMensais = resumosMensais;
    }

    /** Lançamentos do demonstrativo (registros Q100), na ordem do arquivo. */
    public List<Lancamento> lancamentos() {
        return lancamentos;
    }

    /** Resumos mensais (registros Q200), na ordem do arquivo. */
    public List<ResumoMensal> resumosMensais() {
        return resumosMensais;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        DemonstrativoResultado that = (DemonstrativoResultado) o;
        return Objects.equals(lancamentos, that.lancamentos)
                && Objects.equals(resumosMensais, that.resumosMensais);
    }

    @Override
    public int hashCode() {
        return Objects.hash(lancamentos, resumosMensais);
    }

    @Override
    public String toString() {
        return "DemonstrativoResultado[lancamentos=" + lancamentos
                + ", resumosMensais=" + resumosMensais
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
        builder.lancamentos = new ArrayList<>(lancamentos);
        builder.resumosMensais = new ArrayList<>(resumosMensais);
        return builder;
    }

    /** Construtor fluente de DemonstrativoResultado. */
    public static final class Builder {

        private List<Lancamento> lancamentos = new ArrayList<>();
        private List<ResumoMensal> resumosMensais = new ArrayList<>();

        /** Substitui a lista de lançamentos (registros Q100). */
        public Builder lancamentos(List<Lancamento> lancamentos) {
            this.lancamentos = lancamentos == null ? new ArrayList<>() : new ArrayList<>(lancamentos);
            return this;
        }

        /** Acrescenta um lançamento (registro Q100). */
        public Builder addLancamento(Lancamento lancamento) {
            this.lancamentos.add(lancamento);
            return this;
        }

        /** Substitui a lista de resumos mensais (registros Q200). */
        public Builder resumosMensais(List<ResumoMensal> resumosMensais) {
            this.resumosMensais = resumosMensais == null ? new ArrayList<>() : new ArrayList<>(resumosMensais);
            return this;
        }

        /** Acrescenta um resumo mensal (registro Q200). */
        public Builder addResumoMensal(ResumoMensal resumoMensal) {
            this.resumosMensais.add(resumoMensal);
            return this;
        }

        /**
         * Monta o registro sem validar o conteúdo; validar é papel do LcdprValidator.
         *
         * @return DemonstrativoResultado montado
         */
        public DemonstrativoResultado build() {
            return new DemonstrativoResultado(
                    lancamentos,
                    resumosMensais
            );
        }
    }
}
