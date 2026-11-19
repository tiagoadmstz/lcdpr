package io.github.tiagoadmstz.lcdpr;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import io.github.tiagoadmstz.lcdpr.modelo.ImovelRural;

class LcdprJsonTest {

    @Test
    @DisplayName("Gera a amostra válida igual ao arquivo JSON de referência, em uma linha")
    void shouldWriteSampleAsSingleJsonLine() throws IOException {
        assertThat(LcdprJson.toJson(Amostras.valido()) + "\n").isEqualTo(amostra("lcdpr-valido.json"));
    }

    @Test
    @DisplayName("Lê a amostra JSON de referência e obtém o mesmo modelo de Amostras.valido()")
    void shouldReadSampleAsSameModel() throws IOException {
        assertThat(LcdprJson.fromJson(amostra("lcdpr-valido.json"))).isEqualTo(Amostras.valido());
    }

    @Test
    @DisplayName("Ida e volta do modelo em JSON preserva o modelo")
    void shouldRoundTripModel() {
        assertThat(LcdprJson.fromJson(LcdprJson.toJson(Amostras.valido()))).isEqualTo(Amostras.valido());
    }

    @Test
    @DisplayName("Escreve no escritor o mesmo texto de toJson e lê de um leitor o mesmo modelo")
    void shouldWriteToWriterAndReadFromReader() throws IOException {
        StringWriter escritor = new StringWriter();
        LcdprJson.toJson(Amostras.valido(), escritor);

        assertThat(escritor.toString()).isEqualTo(LcdprJson.toJson(Amostras.valido()));
        assertThat(LcdprJson.fromJson(new StringReader(escritor.toString()))).isEqualTo(Amostras.valido());
    }

    @Test
    @DisplayName("Lê a identificação, o demonstrativo e o 9999 do arquivo da versão 1.x")
    void shouldReadLegacyIdentificationAndClosing() throws IOException {
        Lcdpr lcdpr = LcdprJson.fromJson(amostra("lcdpr-legado-1x.json"));

        assertThat(lcdpr.bloco0().identificacao().cpf()).isEqualTo("11111111191");
        assertThat(lcdpr.bloco0().identificacao().nome()).isEqualTo("JOSÉ DA SILVA");
        assertThat(lcdpr.bloco0().dadosCadastrais().telefone()).isEqualTo("6133333333");
        assertThat(lcdpr.blocoQ().resumosMensais()).hasSize(2);
        assertThat(lcdpr.bloco9().telefoneContador()).isEqualTo("6133333333");
        assertThat(lcdpr.bloco9().quantidadeLinhas()).isEqualTo(8007L);
    }

    @Test
    @DisplayName("Lê o imóvel do arquivo da versão 1.x com cadastro ITR e CAEPF e participação de 50%")
    void shouldReadLegacyRuralPropertyWithParticipation() throws IOException {
        ImovelRural imovel = LcdprJson.fromJson(amostra("lcdpr-legado-1x.json")).bloco0().imoveisRurais().get(0);

        assertThat(imovel.cadastroItr()).isEqualTo("12345678");
        assertThat(imovel.caepf()).isEqualTo("12345678901234");
        assertThat(imovel.participacao()).isEqualTo(new BigDecimal("50.00"));
    }

    @Test
    @DisplayName("Ignora os terceiros fora do imóvel no arquivo da versão 1.x")
    void shouldIgnoreLegacyThirdPartiesOutsideProperty() throws IOException {
        ImovelRural imovel = LcdprJson.fromJson(amostra("lcdpr-legado-1x.json")).bloco0().imoveisRurais().get(0);

        assertThat(imovel.terceiros()).isEmpty();
    }

    @Test
    @DisplayName("Lê uma conta e os dois lançamentos do arquivo da versão 1.x")
    void shouldReadLegacyAccountAndEntries() throws IOException {
        Lcdpr lcdpr = LcdprJson.fromJson(amostra("lcdpr-legado-1x.json"));

        assertThat(lcdpr.bloco0().contasBancarias()).hasSize(1);
        assertThat(lcdpr.blocoQ().lancamentos()).hasSize(2);
        assertThat(lcdpr.blocoQ().lancamentos().get(0).valorEntrada()).isEqualTo(new BigDecimal("10000.00"));
    }

    @Test
    @DisplayName("Rejeita JSON malformado com LcdprException")
    void shouldRejectMalformedJson() {
        assertThatThrownBy(() -> LcdprJson.fromJson("{"))
                .isInstanceOf(LcdprException.class);
    }

    @Test
    @DisplayName("Omite a chave bloco_Q quando o demonstrativo é nulo")
    void shouldOmitBlocoQWhenNull() {
        Lcdpr semDemonstrativo = Amostras.valido().toBuilder().blocoQ(null).build();

        assertThat(LcdprJson.toJson(semDemonstrativo)).doesNotContain("\"bloco_Q\"");
    }

    private static String amostra(String nome) throws IOException {
        try (InputStream entrada = LcdprJsonTest.class.getResourceAsStream("/samples/" + nome)) {
            return new String(entrada.readAllBytes(), StandardCharsets.UTF_8);
        }
    }
}
