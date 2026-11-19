package io.github.tiagoadmstz.lcdpr.example.pure;

import static org.assertj.core.api.Assertions.assertThatCode;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class PureJava8ExampleTest {

    @Test
    @DisplayName("Executa o exemplo pure Java 8 sem erros")
    void shouldRunPureJava8ExampleWithoutErrors() {
        assertThatCode(() -> PureJava8Example.main(new String[0])).doesNotThrowAnyException();
    }
}
