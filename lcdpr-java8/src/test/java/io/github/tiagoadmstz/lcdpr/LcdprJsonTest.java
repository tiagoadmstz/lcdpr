package io.github.tiagoadmstz.lcdpr;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import io.github.tiagoadmstz.lcdpr.modelo.ImovelRural;
import io.github.tiagoadmstz.lcdpr.tabela.TipoContraparte;

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
    @DisplayName("Anexa ao imóvel de mesmo código os terceiros que a versão 1.x guardava fora dele")
    void shouldAttachLegacyThirdPartiesToRuralProperty() throws IOException {
        ImovelRural imovel = LcdprJson.fromJson(amostra("lcdpr-legado-1x.json")).bloco0().imoveisRurais().get(0);

        assertThat(imovel.codigoImovel()).isEqualTo("001");
        assertThat(imovel.terceiros()).hasSize(1);
        assertThat(imovel.terceiros().get(0).cpfCnpj()).isEqualTo("12345678912");
        assertThat(imovel.terceiros().get(0).nome()).isEqualTo("JOÃO DE SOUSA");
        assertThat(imovel.terceiros().get(0).participacao()).isEqualTo(new BigDecimal("5.20"));
        assertThat(imovel.terceiros().get(0).tipoContraparte()).isEqualTo(TipoContraparte.PARCEIRO);
    }

    @Test
    @DisplayName("Rejeita terceiro da versão 1.x sem imóvel de mesmo código")
    void shouldRejectLegacyThirdPartyWithoutRuralProperty() {
        String json = "{\"bloco_0\":{"
                + "\"identificacao_pessoa_fisica\":{\"reg\":\"0000\"},"
                + "\"parametros_tributacao\":{\"reg\":\"0010\"},"
                + "\"dados_cadastrais_contribuinte\":{\"reg\":\"0030\"},"
                + "\"exploracao_imoveis_rurais\":[{\"reg\":\"0045\",\"cod_imovel\":\"999\"}]}}";

        assertThatThrownBy(() -> LcdprJson.fromJson(json))
                .isInstanceOf(LcdprException.class)
                .hasMessageContaining("COD_IMOVEL 999 sem 0040 correspondente");
    }

    @Test
    @DisplayName("Rejeita conteúdo depois do objeto JSON")
    void shouldRejectTrailingContentAfterJson() {
        String json = LcdprJson.toJson(Amostras.valido()) + " lixo";

        assertThatThrownBy(() -> LcdprJson.fromJson(json))
                .isInstanceOf(LcdprException.class);
    }

    @Test
    @DisplayName("Rejeita JSON nulo com LcdprException")
    void shouldRejectNullJson() {
        assertThatThrownBy(() -> LcdprJson.fromJson((String) null))
                .isInstanceOf(LcdprException.class)
                .hasMessageContaining("nulo");
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
            return lerTexto(entrada);
        }
    }

    private static String lerTexto(InputStream entrada) throws IOException {
        ByteArrayOutputStream saida = new ByteArrayOutputStream();
        byte[] buffer = new byte[8192];
        int lidos;
        while ((lidos = entrada.read(buffer)) != -1) {
            saida.write(buffer, 0, lidos);
        }
        return new String(saida.toByteArray(), StandardCharsets.UTF_8);
    }
}
