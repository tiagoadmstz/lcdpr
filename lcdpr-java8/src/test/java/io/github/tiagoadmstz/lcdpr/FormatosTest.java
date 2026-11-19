package io.github.tiagoadmstz.lcdpr;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class FormatosTest {

    @Test
    @DisplayName("Formata 10000,00 como 1000000 no campo VALOR")
    void shouldFormatValueOfTenThousandWithoutSeparator() {
        assertThat(Formatos.valor(new BigDecimal("10000"))).isEqualTo("1000000");
    }

    @Test
    @DisplayName("Formata 1.129.998,99 como 112999899 no campo VALOR")
    void shouldFormatValueWithCentsAsDigitsOnly() {
        assertThat(Formatos.valor(new BigDecimal("1129998.99"))).isEqualTo("112999899");
    }

    @Test
    @DisplayName("Formata zero como 000 no campo VALOR")
    void shouldFormatZeroValueWithThreeDigits() {
        assertThat(Formatos.valor(BigDecimal.ZERO)).isEqualTo("000");
    }

    @Test
    @DisplayName("Formata 0,01 como 001 no campo VALOR")
    void shouldFormatOneCentWithLeadingZeros() {
        assertThat(Formatos.valor(new BigDecimal("0.01"))).isEqualTo("001");
    }

    @Test
    @DisplayName("Formata 17% como 01700 no campo PERCENTUAL")
    void shouldFormatSeventeenPercentWithFiveDigits() {
        assertThat(Formatos.percentual(new BigDecimal("17"))).isEqualTo("01700");
    }

    @Test
    @DisplayName("Formata 5,2% como 00520 no campo PERCENTUAL")
    void shouldFormatFivePointTwoPercentWithFiveDigits() {
        assertThat(Formatos.percentual(new BigDecimal("5.2"))).isEqualTo("00520");
    }

    @Test
    @DisplayName("Formata 50% como 05000 no campo PERCENTUAL")
    void shouldFormatFiftyPercentWithFiveDigits() {
        assertThat(Formatos.percentual(new BigDecimal("50"))).isEqualTo("05000");
    }

    @Test
    @DisplayName("Lê 0 como 0,00 no campo VALOR")
    void shouldReadZeroAsZeroWithTwoDecimals() {
        assertThat(Formatos.lerValor("0")).isEqualTo(new BigDecimal("0.00"));
    }

    @Test
    @DisplayName("Lê campo vazio como nulo no campo VALOR")
    void shouldReadEmptyValueAsNull() {
        assertThat(Formatos.lerValor("")).isNull();
    }

    @Test
    @DisplayName("Rejeita valor com letra na leitura")
    void shouldRejectValueWithNonDigitOnRead() {
        assertThatThrownBy(() -> Formatos.lerValor("12a"))
                .isInstanceOf(LcdprException.class);
    }

    @Test
    @DisplayName("Rejeita valor negativo na escrita")
    void shouldRejectNegativeValueOnWrite() {
        assertThatThrownBy(() -> Formatos.valor(new BigDecimal("-1.00")))
                .isInstanceOf(LcdprException.class);
    }

    @Test
    @DisplayName("Rejeita texto com separador na escrita")
    void shouldRejectTextWithSeparator() {
        assertThatThrownBy(() -> Formatos.texto("Venda | milho"))
                .isInstanceOf(LcdprException.class);
    }

    @Test
    @DisplayName("Rejeita data inexistente na leitura estrita")
    void shouldRejectNonexistentDateOnRead() {
        assertThatThrownBy(() -> Formatos.lerData("31022019"))
                .isInstanceOf(LcdprException.class);
    }
}
