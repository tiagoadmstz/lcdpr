package io.github.tiagoadmstz.lcdpr;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import io.github.tiagoadmstz.lcdpr.modelo.DemonstrativoResultado;
import io.github.tiagoadmstz.lcdpr.modelo.Lancamento;
import io.github.tiagoadmstz.lcdpr.tabela.NaturezaSaldo;

class LcdprCalculatorTest {

    @Test
    @DisplayName("Soma a entrada no saldo do lançamento")
    void shouldSumEntryIntoBalance() {
        Lcdpr lcdpr = comLancamentos(List.of(lancamento(LocalDate.of(2019, 1, 2), "10000", null)));

        Lancamento calculado = LcdprCalculator.recalculate(lcdpr).blocoQ().lancamentos().get(0);

        assertThat(calculado.saldoFinal()).isEqualByComparingTo("10000.00");
        assertThat(calculado.naturezaSaldoFinal()).isEqualTo(NaturezaSaldo.POSITIVO);
    }

    @Test
    @DisplayName("Saldo negativo vira valor absoluto com natureza N")
    void shouldStoreAbsoluteValueWithNegativeNatureWhenBalanceIsNegative() {
        Lcdpr lcdpr = comLancamentos(List.of(lancamento(LocalDate.of(2019, 1, 2), null, "5000")));

        Lancamento calculado = LcdprCalculator.recalculate(lcdpr).blocoQ().lancamentos().get(0);

        assertThat(calculado.saldoFinal()).isEqualByComparingTo("5000.00");
        assertThat(calculado.naturezaSaldoFinal()).isEqualTo(NaturezaSaldo.NEGATIVO);
    }

    @Test
    @DisplayName("Q200 acumula o saldo entre os meses: janeiro 5000,00 P e fevereiro 3000,00 N")
    void shouldAccumulateMonthlySummariesAcrossMonths() {
        Lcdpr lcdpr = comLancamentos(List.of(
                lancamento(LocalDate.of(2019, 1, 2), "10000", null),
                lancamento(LocalDate.of(2019, 1, 15), null, "5000"),
                lancamento(LocalDate.of(2019, 2, 10), null, "8000")));

        DemonstrativoResultado blocoQ = LcdprCalculator.recalculate(lcdpr).blocoQ();

        assertThat(blocoQ.resumosMensais()).hasSize(2);
        assertThat(blocoQ.resumosMensais().get(0).mes()).isEqualTo(YearMonth.of(2019, 1));
        assertThat(blocoQ.resumosMensais().get(0).valorEntrada()).isEqualByComparingTo("10000.00");
        assertThat(blocoQ.resumosMensais().get(0).valorSaida()).isEqualByComparingTo("5000.00");
        assertThat(blocoQ.resumosMensais().get(0).saldoFinal()).isEqualByComparingTo("5000.00");
        assertThat(blocoQ.resumosMensais().get(0).naturezaSaldoFinal()).isEqualTo(NaturezaSaldo.POSITIVO);
        assertThat(blocoQ.resumosMensais().get(1).mes()).isEqualTo(YearMonth.of(2019, 2));
        assertThat(blocoQ.resumosMensais().get(1).valorEntrada()).isEqualByComparingTo("0.00");
        assertThat(blocoQ.resumosMensais().get(1).valorSaida()).isEqualByComparingTo("8000.00");
        assertThat(blocoQ.resumosMensais().get(1).saldoFinal()).isEqualByComparingTo("3000.00");
        assertThat(blocoQ.resumosMensais().get(1).naturezaSaldoFinal()).isEqualTo(NaturezaSaldo.NEGATIVO);
    }

    @Test
    @DisplayName("Ordena os lançamentos fora de ordem por data antes de acumular o saldo")
    void shouldSortOutOfOrderLancamentosByDate() {
        Lcdpr lcdpr = comLancamentos(List.of(
                lancamento(LocalDate.of(2019, 2, 10), null, "8000"),
                lancamento(LocalDate.of(2019, 1, 2), "10000", null)));

        List<Lancamento> calculados = LcdprCalculator.recalculate(lcdpr).blocoQ().lancamentos();

        assertThat(calculados.get(0).data()).isEqualTo(LocalDate.of(2019, 1, 2));
        assertThat(calculados.get(0).saldoFinal()).isEqualByComparingTo("10000.00");
        assertThat(calculados.get(1).data()).isEqualTo(LocalDate.of(2019, 2, 10));
        assertThat(calculados.get(1).saldoFinal()).isEqualByComparingTo("2000.00");
        assertThat(calculados.get(1).naturezaSaldoFinal()).isEqualTo(NaturezaSaldo.POSITIVO);
    }

    @Test
    @DisplayName("Não altera o registro de entrada")
    void shouldNotChangeInputRecord() {
        Lcdpr original = comLancamentos(List.of(
                lancamento(LocalDate.of(2019, 2, 10), null, "8000"),
                lancamento(LocalDate.of(2019, 1, 2), "10000", null)));
        Lcdpr copia = original.toBuilder().build();

        LcdprCalculator.recalculate(original);

        assertThat(original).isEqualTo(copia);
    }

    @Test
    @DisplayName("Recalcular o exemplo válido devolve o mesmo registro")
    void shouldReturnSameContentForValidSample() {
        assertThat(LcdprCalculator.recalculate(Amostras.valido())).isEqualTo(Amostras.valido());
    }

    @Test
    @DisplayName("Sem bloco Q devolve o próprio registro")
    void shouldReturnSameObjectWhenThereIsNoBlocoQ() {
        Lcdpr semBlocoQ = Amostras.valido().toBuilder().blocoQ(null).build();

        assertThat(LcdprCalculator.recalculate(semBlocoQ)).isSameAs(semBlocoQ);
    }

    private static Lcdpr comLancamentos(List<Lancamento> lancamentos) {
        DemonstrativoResultado blocoQ = DemonstrativoResultado.builder()
                .lancamentos(lancamentos)
                .build();
        return Amostras.valido().toBuilder().blocoQ(blocoQ).build();
    }

    private static Lancamento lancamento(LocalDate data, String entrada, String saida) {
        return Lancamento.builder()
                .data(data)
                .codigoImovel("001")
                .codigoConta("001")
                .valorEntrada(entrada == null ? null : new BigDecimal(entrada))
                .valorSaida(saida == null ? null : new BigDecimal(saida))
                .build();
    }
}
