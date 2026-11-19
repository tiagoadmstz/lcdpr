package io.github.tiagoadmstz.lcdpr;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.Function;

import io.github.tiagoadmstz.lcdpr.modelo.DemonstrativoResultado;
import io.github.tiagoadmstz.lcdpr.modelo.Lancamento;
import io.github.tiagoadmstz.lcdpr.modelo.ResumoMensal;
import io.github.tiagoadmstz.lcdpr.tabela.NaturezaSaldo;

/**
 * Recalcula os saldos do bloco Q: saldo acumulado dos lançamentos (Q100) e resumo mensal (Q200).
 * O registro de entrada não é alterado.
 */
public final class LcdprCalculator {

    static final Comparator<LocalDate> ORDEM_DATA = Comparator.nullsLast(Comparator.naturalOrder());

    private LcdprCalculator() {
    }

    /**
     * Recalcula os saldos do bloco Q. Os lançamentos saem em ordem de data (sem data no fim), com saldo
     * acumulado em valor absoluto e natureza P ou N; o Q200 tem um resumo por mês com lançamento.
     *
     * @param lcdpr registro com os lançamentos
     * @return novo registro com o bloco Q recalculado, ou o próprio objeto quando não há bloco Q
     */
    public static Lcdpr recalculate(Lcdpr lcdpr) {
        if (lcdpr == null || lcdpr.blocoQ() == null) {
            return lcdpr;
        }
        List<Lancamento> lancamentos = calcularLancamentos(lcdpr.blocoQ().lancamentos());
        DemonstrativoResultado blocoQ = DemonstrativoResultado.builder()
                .lancamentos(lancamentos)
                .resumosMensais(calcularResumos(lancamentos))
                .build();
        return lcdpr.toBuilder().blocoQ(blocoQ).build();
    }

    private static List<Lancamento> calcularLancamentos(List<Lancamento> originais) {
        List<Lancamento> ordenados = new ArrayList<>(originais);
        ordenados.sort(Comparator.comparing(Lancamento::data, ORDEM_DATA));
        List<Lancamento> calculados = new ArrayList<>(ordenados.size());
        BigDecimal saldo = BigDecimal.ZERO;
        for (Lancamento lancamento : ordenados) {
            saldo = saldo.add(zeroSeNulo(lancamento.valorEntrada())).subtract(zeroSeNulo(lancamento.valorSaida()));
            calculados.add(lancamento.toBuilder()
                    .saldoFinal(saldo.abs())
                    .naturezaSaldoFinal(natureza(saldo))
                    .build());
        }
        return calculados;
    }

    private static List<ResumoMensal> calcularResumos(List<Lancamento> lancamentos) {
        Map<YearMonth, List<Lancamento>> porMes = new TreeMap<>();
        for (Lancamento lancamento : lancamentos) {
            if (lancamento.data() != null) {
                porMes.computeIfAbsent(YearMonth.from(lancamento.data()), mes -> new ArrayList<>()).add(lancamento);
            }
        }
        List<ResumoMensal> resumos = new ArrayList<>(porMes.size());
        BigDecimal saldo = BigDecimal.ZERO;
        for (Map.Entry<YearMonth, List<Lancamento>> mes : porMes.entrySet()) {
            BigDecimal entrada = somar(mes.getValue(), Lancamento::valorEntrada);
            BigDecimal saida = somar(mes.getValue(), Lancamento::valorSaida);
            saldo = saldo.add(entrada).subtract(saida);
            resumos.add(ResumoMensal.builder()
                    .mes(mes.getKey())
                    .valorEntrada(entrada)
                    .valorSaida(saida)
                    .saldoFinal(saldo.abs())
                    .naturezaSaldoFinal(natureza(saldo))
                    .build());
        }
        return resumos;
    }

    private static BigDecimal somar(List<Lancamento> lancamentos, Function<Lancamento, BigDecimal> campo) {
        BigDecimal total = BigDecimal.ZERO;
        for (Lancamento lancamento : lancamentos) {
            total = total.add(zeroSeNulo(campo.apply(lancamento)));
        }
        return total;
    }

    private static BigDecimal zeroSeNulo(BigDecimal valor) {
        return valor == null ? BigDecimal.ZERO : valor;
    }

    private static NaturezaSaldo natureza(BigDecimal saldo) {
        return saldo.signum() >= 0 ? NaturezaSaldo.POSITIVO : NaturezaSaldo.NEGATIVO;
    }
}
