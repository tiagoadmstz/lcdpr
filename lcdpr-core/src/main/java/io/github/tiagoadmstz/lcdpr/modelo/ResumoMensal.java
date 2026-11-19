package io.github.tiagoadmstz.lcdpr.modelo;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.YearMonth;

import io.github.tiagoadmstz.lcdpr.tabela.NaturezaSaldo;

/**
 * Registro Q200: resumo mensal do demonstrativo.
 */
public record ResumoMensal(
        YearMonth mes,
        BigDecimal valorEntrada,
        BigDecimal valorSaida,
        BigDecimal saldoFinal,
        NaturezaSaldo naturezaSaldoFinal
) {

    /** Normaliza os valores para escala 2. */
    public ResumoMensal {
        valorEntrada = valorEntrada == null ? null : valorEntrada.setScale(2, RoundingMode.HALF_UP);
        valorSaida = valorSaida == null ? null : valorSaida.setScale(2, RoundingMode.HALF_UP);
        saldoFinal = saldoFinal == null ? null : saldoFinal.setScale(2, RoundingMode.HALF_UP);
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
        builder.mes = mes;
        builder.valorEntrada = valorEntrada;
        builder.valorSaida = valorSaida;
        builder.saldoFinal = saldoFinal;
        builder.naturezaSaldoFinal = naturezaSaldoFinal;
        return builder;
    }

    /** Construtor fluente de ResumoMensal. */
    public static final class Builder {

        private YearMonth mes;
        private BigDecimal valorEntrada = BigDecimal.ZERO.setScale(2);
        private BigDecimal valorSaida = BigDecimal.ZERO.setScale(2);
        private BigDecimal saldoFinal = BigDecimal.ZERO.setScale(2);
        private NaturezaSaldo naturezaSaldoFinal = NaturezaSaldo.POSITIVO;

        /** Registro Q200, campo MES. */
        public Builder mes(YearMonth mes) {
            this.mes = mes;
            return this;
        }

        /** Registro Q200, campo VL_ENTRADA. */
        public Builder valorEntrada(BigDecimal valorEntrada) {
            this.valorEntrada = valorEntrada;
            return this;
        }

        /** Registro Q200, campo VL_SAIDA. */
        public Builder valorSaida(BigDecimal valorSaida) {
            this.valorSaida = valorSaida;
            return this;
        }

        /** Registro Q200, campo SLD_FIN. */
        public Builder saldoFinal(BigDecimal saldoFinal) {
            this.saldoFinal = saldoFinal;
            return this;
        }

        /** Registro Q200, campo NAT_SLD_FIN. */
        public Builder naturezaSaldoFinal(NaturezaSaldo naturezaSaldoFinal) {
            this.naturezaSaldoFinal = naturezaSaldoFinal;
            return this;
        }

        /**
         * Monta o registro sem validar o conteúdo; validar é papel do LcdprValidator.
         *
         * @return ResumoMensal montado
         */
        public ResumoMensal build() {
            return new ResumoMensal(
                    mes,
                    valorEntrada,
                    valorSaida,
                    saldoFinal,
                    naturezaSaldoFinal
            );
        }
    }
}
