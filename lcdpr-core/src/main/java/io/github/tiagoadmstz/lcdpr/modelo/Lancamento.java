package io.github.tiagoadmstz.lcdpr.modelo;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

import io.github.tiagoadmstz.lcdpr.tabela.NaturezaSaldo;
import io.github.tiagoadmstz.lcdpr.tabela.TipoDocumento;
import io.github.tiagoadmstz.lcdpr.tabela.TipoLancamento;

/**
 * Registro Q100: lançamento do demonstrativo do resultado da atividade rural.
 */
public record Lancamento(
        LocalDate data,
        String codigoImovel,
        String codigoConta,
        String numeroDocumento,
        TipoDocumento tipoDocumento,
        String historico,
        String cpfCnpjParticipante,
        TipoLancamento tipoLancamento,
        BigDecimal valorEntrada,
        BigDecimal valorSaida,
        BigDecimal saldoFinal,
        NaturezaSaldo naturezaSaldoFinal
) {

    /** Normaliza os valores para escala 2. */
    public Lancamento {
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
        builder.data = data;
        builder.codigoImovel = codigoImovel;
        builder.codigoConta = codigoConta;
        builder.numeroDocumento = numeroDocumento;
        builder.tipoDocumento = tipoDocumento;
        builder.historico = historico;
        builder.cpfCnpjParticipante = cpfCnpjParticipante;
        builder.tipoLancamento = tipoLancamento;
        builder.valorEntrada = valorEntrada;
        builder.valorSaida = valorSaida;
        builder.saldoFinal = saldoFinal;
        builder.naturezaSaldoFinal = naturezaSaldoFinal;
        return builder;
    }

    /** Construtor fluente de Lancamento. */
    public static final class Builder {

        private LocalDate data;
        private String codigoImovel;
        private String codigoConta;
        private String numeroDocumento;
        private TipoDocumento tipoDocumento;
        private String historico;
        private String cpfCnpjParticipante;
        private TipoLancamento tipoLancamento;
        private BigDecimal valorEntrada = BigDecimal.ZERO.setScale(2);
        private BigDecimal valorSaida = BigDecimal.ZERO.setScale(2);
        private BigDecimal saldoFinal = BigDecimal.ZERO.setScale(2);
        private NaturezaSaldo naturezaSaldoFinal = NaturezaSaldo.POSITIVO;

        /** Registro Q100, campo DATA. */
        public Builder data(LocalDate data) {
            this.data = data;
            return this;
        }

        /** Registro Q100, campo COD_IMOVEL. */
        public Builder codigoImovel(String codigoImovel) {
            this.codigoImovel = codigoImovel;
            return this;
        }

        /** Registro Q100, campo COD_CONTA. */
        public Builder codigoConta(String codigoConta) {
            this.codigoConta = codigoConta;
            return this;
        }

        /** Registro Q100, campo NUM_DOC. */
        public Builder numeroDocumento(String numeroDocumento) {
            this.numeroDocumento = numeroDocumento;
            return this;
        }

        /** Registro Q100, campo TIPO_DOC. */
        public Builder tipoDocumento(TipoDocumento tipoDocumento) {
            this.tipoDocumento = tipoDocumento;
            return this;
        }

        /** Registro Q100, campo HIST. */
        public Builder historico(String historico) {
            this.historico = historico;
            return this;
        }

        /** Registro Q100, campo ID_PARTIC. */
        public Builder cpfCnpjParticipante(String cpfCnpjParticipante) {
            this.cpfCnpjParticipante = cpfCnpjParticipante;
            return this;
        }

        /** Registro Q100, campo TIPO_LANC. */
        public Builder tipoLancamento(TipoLancamento tipoLancamento) {
            this.tipoLancamento = tipoLancamento;
            return this;
        }

        /** Registro Q100, campo VL_ENTRADA. */
        public Builder valorEntrada(BigDecimal valorEntrada) {
            this.valorEntrada = valorEntrada;
            return this;
        }

        /** Registro Q100, campo VL_SAIDA. */
        public Builder valorSaida(BigDecimal valorSaida) {
            this.valorSaida = valorSaida;
            return this;
        }

        /** Registro Q100, campo SLD_FIN. */
        public Builder saldoFinal(BigDecimal saldoFinal) {
            this.saldoFinal = saldoFinal;
            return this;
        }

        /** Registro Q100, campo NAT_SLD_FIN. */
        public Builder naturezaSaldoFinal(NaturezaSaldo naturezaSaldoFinal) {
            this.naturezaSaldoFinal = naturezaSaldoFinal;
            return this;
        }

        /**
         * Monta o registro sem validar o conteúdo; validar é papel do LcdprValidator.
         *
         * @return Lancamento montado
         */
        public Lancamento build() {
            return new Lancamento(
                    data,
                    codigoImovel,
                    codigoConta,
                    numeroDocumento,
                    tipoDocumento,
                    historico,
                    cpfCnpjParticipante,
                    tipoLancamento,
                    valorEntrada,
                    valorSaida,
                    saldoFinal,
                    naturezaSaldoFinal
            );
        }
    }
}
