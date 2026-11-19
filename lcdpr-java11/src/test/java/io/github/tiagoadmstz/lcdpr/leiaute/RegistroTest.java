package io.github.tiagoadmstz.lcdpr.leiaute;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class RegistroTest {

    @Test
    @DisplayName("Registro 0040 tem 16 campos")
    void shouldListSixteenFieldsInR0040() {
        assertThat(Registro.R0040.campos()).hasSize(16);
    }

    @Test
    @DisplayName("Primeiro campo do Registro 0040 é COD_IMOVEL")
    void shouldStartR0040WithCodImovel() {
        assertThat(Registro.R0040.campos().get(0).nome()).isEqualTo("COD_IMOVEL");
    }

    @Test
    @DisplayName("Registro Q100 tem 12 campos")
    void shouldListTwelveFieldsInRQ100() {
        assertThat(Registro.RQ100.campos()).hasSize(12);
    }

    @Test
    @DisplayName("Resolve o código Q100 para o registro Q100")
    void shouldResolveQ100Code() {
        assertThat(Registro.fromCodigo("Q100")).isEqualTo(Registro.RQ100);
    }

    @Test
    @DisplayName("Rejeita código de registro desconhecido")
    void shouldRejectUnknownRegistroCode() {
        assertThatThrownBy(() -> Registro.fromCodigo("X"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Registro desconhecido: X");
    }
}
