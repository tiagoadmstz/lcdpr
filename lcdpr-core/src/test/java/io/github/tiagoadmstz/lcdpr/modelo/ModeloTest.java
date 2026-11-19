package io.github.tiagoadmstz.lcdpr.modelo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import io.github.tiagoadmstz.lcdpr.tabela.NaturezaSaldo;
import io.github.tiagoadmstz.lcdpr.tabela.TipoContraparte;
import io.github.tiagoadmstz.lcdpr.tabela.TipoExploracao;
import java.math.BigDecimal;
import java.time.YearMonth;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ModeloTest {

    @Test
    @DisplayName("Builder de Lancamento aplica valorEntrada 0.00 e natureza POSITIVO por padrão")
    void shouldApplyLancamentoBuilderDefaults() {
        Lancamento lancamento = Lancamento.builder().build();

        assertThat(lancamento.valorEntrada()).isEqualTo(new BigDecimal("0.00"));
        assertThat(lancamento.naturezaSaldoFinal()).isEqualTo(NaturezaSaldo.POSITIVO);
    }

    @Test
    @DisplayName("Construtor normaliza BigDecimal de escala 0 para escala 2")
    void shouldNormalizeBigDecimalToScaleTwo() {
        ResumoMensal resumo = new ResumoMensal(YearMonth.of(2024, 1), new BigDecimal("10"), null, null, null);

        assertThat(resumo.valorEntrada()).isEqualTo(new BigDecimal("10.00"));
    }

    @Test
    @DisplayName("Lista de terceiros do ImovelRural não aceita alteração")
    void shouldKeepImovelRuralTerceirosImmutable() {
        ImovelRural imovel = ImovelRural.builder().codigoImovel("001").build();
        Terceiro terceiro = new Terceiro("001", TipoContraparte.PARCEIRO, "11144477735", "Parceiro Fictício",
                new BigDecimal("50"));

        assertThatThrownBy(() -> imovel.terceiros().add(terceiro))
                .isInstanceOf(UnsupportedOperationException.class);
    }

    @Test
    @DisplayName("toBuilder seguido de build produz registro igual ao original")
    void shouldBuildEqualRecordFromToBuilder() {
        ImovelRural original = ImovelRural.builder()
                .codigoImovel("001")
                .nome("Sítio Fictício")
                .tipoExploracao(TipoExploracao.PARCERIA)
                .participacao(new BigDecimal("50"))
                .addTerceiro(new Terceiro("001", TipoContraparte.PARCEIRO, "11144477735", "Parceiro Fictício",
                        new BigDecimal("50")))
                .build();

        assertThat(original.toBuilder().build()).isEqualTo(original);
    }
}
