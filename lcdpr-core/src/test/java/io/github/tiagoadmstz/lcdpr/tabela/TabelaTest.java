package io.github.tiagoadmstz.lcdpr.tabela;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class TabelaTest {

    @Test
    @DisplayName("Resolve o código 0 para REGULAR em IndicadorInicioPeriodo")
    void shouldResolveValidCodeForIndicadorInicioPeriodo() {
        assertThat(IndicadorInicioPeriodo.fromCodigo("0")).isEqualTo(IndicadorInicioPeriodo.REGULAR);
    }

    @Test
    @DisplayName("Rejeita código fora da tabela em IndicadorInicioPeriodo")
    void shouldRejectInvalidCodeForIndicadorInicioPeriodo() {
        assertThatThrownBy(() -> IndicadorInicioPeriodo.fromCodigo("3"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Código inválido para IndicadorInicioPeriodo: 3");
    }

    @Test
    @DisplayName("Resolve o código 3 para SAIDA_DEFINITIVA_PAIS em SituacaoEspecial")
    void shouldResolveValidCodeForSituacaoEspecial() {
        assertThat(SituacaoEspecial.fromCodigo("3")).isEqualTo(SituacaoEspecial.SAIDA_DEFINITIVA_PAIS);
    }

    @Test
    @DisplayName("Rejeita código fora da tabela em SituacaoEspecial")
    void shouldRejectInvalidCodeForSituacaoEspecial() {
        assertThatThrownBy(() -> SituacaoEspecial.fromCodigo("4"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Código inválido para SituacaoEspecial: 4");
    }

    @Test
    @DisplayName("Resolve o código 2 para VINTE_POR_CENTO_RECEITA_BRUTA em FormaApuracao")
    void shouldResolveValidCodeForFormaApuracao() {
        assertThat(FormaApuracao.fromCodigo("2")).isEqualTo(FormaApuracao.VINTE_POR_CENTO_RECEITA_BRUTA);
    }

    @Test
    @DisplayName("Rejeita código fora da tabela em FormaApuracao")
    void shouldRejectInvalidCodeForFormaApuracao() {
        assertThatThrownBy(() -> FormaApuracao.fromCodigo("0"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Código inválido para FormaApuracao: 0");
    }

    @Test
    @DisplayName("Resolve o código 5 para COMODATO em TipoExploracao")
    void shouldResolveValidCodeForTipoExploracao() {
        assertThat(TipoExploracao.fromCodigo("5")).isEqualTo(TipoExploracao.COMODATO);
    }

    @Test
    @DisplayName("Rejeita código fora da tabela em TipoExploracao")
    void shouldRejectInvalidCodeForTipoExploracao() {
        assertThatThrownBy(() -> TipoExploracao.fromCodigo("7"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Código inválido para TipoExploracao: 7");
    }

    @Test
    @DisplayName("Resolve o código 4 para COMODANTE em TipoContraparte")
    void shouldResolveValidCodeForTipoContraparte() {
        assertThat(TipoContraparte.fromCodigo("4")).isEqualTo(TipoContraparte.COMODANTE);
    }

    @Test
    @DisplayName("Rejeita código fora da tabela em TipoContraparte")
    void shouldRejectInvalidCodeForTipoContraparte() {
        assertThatThrownBy(() -> TipoContraparte.fromCodigo("6"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Código inválido para TipoContraparte: 6");
    }

    @Test
    @DisplayName("Resolve o código 3 para RECIBO em TipoDocumento")
    void shouldResolveValidCodeForTipoDocumento() {
        assertThat(TipoDocumento.fromCodigo("3")).isEqualTo(TipoDocumento.RECIBO);
    }

    @Test
    @DisplayName("Rejeita código fora da tabela em TipoDocumento")
    void shouldRejectInvalidCodeForTipoDocumento() {
        assertThatThrownBy(() -> TipoDocumento.fromCodigo("9"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Código inválido para TipoDocumento: 9");
    }

    @Test
    @DisplayName("Resolve o código 3 para RECEITA_ADIANTAMENTO em TipoLancamento")
    void shouldResolveValidCodeForTipoLancamento() {
        assertThat(TipoLancamento.fromCodigo("3")).isEqualTo(TipoLancamento.RECEITA_ADIANTAMENTO);
    }

    @Test
    @DisplayName("Rejeita código fora da tabela em TipoLancamento")
    void shouldRejectInvalidCodeForTipoLancamento() {
        assertThatThrownBy(() -> TipoLancamento.fromCodigo("4"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Código inválido para TipoLancamento: 4");
    }

    @Test
    @DisplayName("Resolve o código N para NEGATIVO em NaturezaSaldo")
    void shouldResolveValidCodeForNaturezaSaldo() {
        assertThat(NaturezaSaldo.fromCodigo("N")).isEqualTo(NaturezaSaldo.NEGATIVO);
    }

    @Test
    @DisplayName("Rejeita código fora da tabela em NaturezaSaldo")
    void shouldRejectInvalidCodeForNaturezaSaldo() {
        assertThatThrownBy(() -> NaturezaSaldo.fromCodigo("X"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Código inválido para NaturezaSaldo: X");
    }
}
