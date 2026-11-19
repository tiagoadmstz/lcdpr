package io.github.tiagoadmstz.lcdpr.example.pure;

import static org.assertj.core.api.Assertions.assertThatCode;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class PureJavaExampleTest {

    @Test
    @DisplayName("Executa o exemplo pure Java sem erros")
    void shouldRunPureJavaExampleWithoutErrors() {
        assertThatCode(() -> PureJavaExample.main(new String[0])).doesNotThrowAnyException();
    }
}
