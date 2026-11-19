package io.github.tiagoadmstz.lcdpr;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import io.github.tiagoadmstz.lcdpr.modelo.ImovelRural;
import io.github.tiagoadmstz.lcdpr.modelo.Lancamento;
import io.github.tiagoadmstz.lcdpr.tabela.TipoContraparte;

class LcdprReaderTest {

    private static final String CABECALHO = "0000|LCDPR|0013|11144477735|JOSÉ DA SILVA|0|0||01012019|31122019";

    @Test
    @DisplayName("Lê a amostra válida e obtém o mesmo modelo de Amostras.valido()")
    void shouldReadSampleAsSameModel() throws IOException {
        assertThat(LcdprReader.read(amostra())).isEqualTo(Amostras.valido());
    }

    @Test
    @DisplayName("Ida e volta da amostra gera o mesmo arquivo, byte a byte")
    void shouldRoundTripSampleByteForByte() throws IOException {
        assertThat(LcdprWriter.write(LcdprReader.read(amostra()))).isEqualTo(amostra());
    }

    @Test
    @DisplayName("Lê dois 0040 com cada um o seu 0045")
    void shouldReadTwoImoveisWithTheirOwnTerceiros() {
        String texto = arquivo(
                CABECALHO,
                "0040|001|BR|BRL|12345678|||Imóvel A|Rua A|1||Centro|DF|5300108|71000000|2|05000",
                "0045|001|1|12345678909|CONDOMINO UM|05000",
                "0040|002|BR|BRL|87654321|12345678901234||Sítio B|Estrada B|||Zona Rural|DF|5300108|71000000|2|05000",
                "0045|002|2|11222333000181|ARRENDADOR DOIS|05000",
                "9999||||||6");

        List<ImovelRural> imoveis = LcdprReader.read(texto).bloco0().imoveisRurais();

        assertThat(imoveis).hasSize(2);
        assertThat(imoveis.get(0).codigoImovel()).isEqualTo("001");
        assertThat(imoveis.get(0).terceiros()).hasSize(1);
        assertThat(imoveis.get(0).terceiros().get(0).nome()).isEqualTo("CONDOMINO UM");
        assertThat(imoveis.get(1).codigoImovel()).isEqualTo("002");
        assertThat(imoveis.get(1).terceiros()).hasSize(1);
        assertThat(imoveis.get(1).terceiros().get(0).nome()).isEqualTo("ARRENDADOR DOIS");
        assertThat(imoveis.get(1).terceiros().get(0).tipoContraparte()).isEqualTo(TipoContraparte.ARRENDADOR);
    }

    @Test
    @DisplayName("Lê o histórico com 'Q100 e 9999' como texto do Q100, sem criar outro registro")
    void shouldReadHistoryContainingRegistroCodesAsText() {
        String texto = arquivo(
                CABECALHO,
                "Q100|02012019|001|001|2|3|Q100 e 9999|12345678909|1|1000000|000|1000000|P",
                "9999||||||3");

        List<Lancamento> lancamentos = LcdprReader.read(texto).blocoQ().lancamentos();

        assertThat(lancamentos).hasSize(1);
        assertThat(lancamentos.get(0).historico()).isEqualTo("Q100 e 9999");
    }

    @Test
    @DisplayName("Informa a linha e a quantidade de campos quando um 0040 tem campos a menos")
    void shouldReportLineWhenRegistroHasFewerFields() {
        String texto = arquivo(
                CABECALHO,
                "0040|001|BR|BRL|12345678|||Imóvel A|Rua A|1||Centro|DF|5300108|71000000|2",
                "9999||||||2");

        assertThatThrownBy(() -> LcdprReader.read(texto))
                .isInstanceOfSatisfying(LcdprException.class, e -> {
                    assertThat(e.linha()).isEqualTo(2);
                    assertThat(e.getMessage()).contains("esperado");
                });
    }

    @Test
    @DisplayName("Rejeita 0045 antes de qualquer 0040")
    void shouldRejectTerceiroBeforeAnyImovel() {
        String texto = arquivo(
                CABECALHO,
                "0045|001|1|12345678909|CONDOMINO UM|05000",
                "9999||||||3");

        assertThatThrownBy(() -> LcdprReader.read(texto))
                .isInstanceOfSatisfying(LcdprException.class, e -> assertThat(e.linha()).isEqualTo(2));
    }

    @Test
    @DisplayName("Rejeita registro com REG desconhecido")
    void shouldRejectUnknownRegistro() {
        String texto = arquivo(
                CABECALHO,
                "X999|1",
                "9999||||||3");

        assertThatThrownBy(() -> LcdprReader.read(texto))
                .isInstanceOfSatisfying(LcdprException.class, e -> assertThat(e.linha()).isEqualTo(2));
    }

    @Test
    @DisplayName("Lê arquivo com quebra de linha somente LF igual à amostra")
    void shouldReadLfOnlyFileAsSample() throws IOException {
        assertThat(LcdprReader.read(amostra().replace("\r\n", "\n"))).isEqualTo(Amostras.valido());
    }

    @Test
    @DisplayName("Ignora o BOM UTF-8 no início do arquivo")
    void shouldIgnoreUtf8Bom() throws IOException {
        assertThat(LcdprReader.read("﻿" + amostra())).isEqualTo(Amostras.valido());
    }

    @Test
    @DisplayName("Rejeita conteúdo nulo com LcdprException, em String, Reader e Path")
    void shouldRejectNullInput() {
        assertThatThrownBy(() -> LcdprReader.read((String) null))
                .isInstanceOf(LcdprException.class)
                .hasMessageContaining("nulo");
        assertThatThrownBy(() -> LcdprReader.read((Reader) null))
                .isInstanceOf(LcdprException.class)
                .hasMessageContaining("nulo");
        assertThatThrownBy(() -> LcdprReader.read(null, StandardCharsets.UTF_8))
                .isInstanceOf(LcdprException.class)
                .hasMessageContaining("nulo");
    }

    @Test
    @DisplayName("Informa o registro, o campo e a linha quando a data do Q100 é inválida")
    void shouldNameRecordAndFieldInReadError() {
        String texto = arquivo(
                CABECALHO,
                "Q100|31022019|001|001|2|3|Historico|12345678909|1|1000000|000|1000000|P",
                "9999||||||3");

        assertThatThrownBy(() -> LcdprReader.read(texto))
                .isInstanceOfSatisfying(LcdprException.class, e -> {
                    assertThat(e.linha()).isEqualTo(2);
                    assertThat(e.getMessage()).contains("Q100.DATA").endsWith("(linha 2)");
                });
    }

    @Test
    @DisplayName("Lê VL_SAIDA vazio como nulo e VL_ENTRADA zero como 0,00")
    void shouldReadEmptyValueAsNullAndZeroAsZeroWithTwoDecimals() {
        String texto = arquivo(
                CABECALHO,
                "Q100|02012019|001|001|2|3|Historico|12345678909|1|0||0|P",
                "9999||||||3");

        Lancamento lancamento = LcdprReader.read(texto).blocoQ().lancamentos().get(0);

        assertThat(lancamento.valorSaida()).isNull();
        assertThat(lancamento.valorEntrada()).isEqualTo(new BigDecimal("0.00"));
    }

    private static String arquivo(String... linhas) {
        return String.join("\r\n", linhas) + "\r\n";
    }

    private String amostra() throws IOException {
        try (InputStream entrada = getClass().getResourceAsStream("/samples/lcdpr-valido.txt")) {
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
