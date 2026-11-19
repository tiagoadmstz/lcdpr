package io.github.tiagoadmstz.lcdpr;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import io.github.tiagoadmstz.lcdpr.modelo.AberturaIdentificacao;
import io.github.tiagoadmstz.lcdpr.modelo.EncerramentoArquivo;
import io.github.tiagoadmstz.lcdpr.modelo.ImovelRural;

class LcdprWriterTest {

    @TempDir
    Path diretorio;

    @Test
    @DisplayName("Gera a amostra válida igual ao arquivo de referência, byte a byte")
    void shouldWriteSampleByteForByte() throws IOException {
        assertThat(LcdprWriter.write(Amostras.valido())).isEqualTo(amostra());
    }

    @Test
    @DisplayName("Repete cada 0040 do mesmo imóvel seguido do seu 0045, sem colar registros")
    void shouldKeepTerceirosAfterEachRepeatedImovel() {
        ImovelRural imovel = Amostras.valido().bloco0().imoveisRurais().get(0);
        Lcdpr lcdpr = Lcdpr.builder()
                .bloco0(AberturaIdentificacao.builder()
                        .identificacao(Amostras.valido().bloco0().identificacao())
                        .parametrosTributacao(Amostras.valido().bloco0().parametrosTributacao())
                        .dadosCadastrais(Amostras.valido().bloco0().dadosCadastrais())
                        .addImovelRural(imovel)
                        .addImovelRural(imovel)
                        .build())
                .bloco9(EncerramentoArquivo.builder().build())
                .build();

        String texto = LcdprWriter.write(lcdpr);
        List<String> linhas = Arrays.asList(texto.split("\r\n"));

        List<String> codigos = linhas.stream()
                .map(linha -> linha.substring(0, linha.indexOf('|')))
                .collect(Collectors.toList());
        assertThat(codigos).containsExactly("0000", "0010", "0030", "0040", "0045", "0040", "0045", "9999");
    }

    @Test
    @DisplayName("Calcula QTD_LIN pelo total de linhas mesmo com quantidade diferente no modelo")
    void shouldComputeQuantidadeLinhasIgnoringModel() throws IOException {
        Lcdpr modelo = Amostras.valido();
        Lcdpr comQuantidadeErrada = modelo.toBuilder()
                .bloco9(modelo.bloco9().toBuilder().quantidadeLinhas(999L).build())
                .build();

        String texto = LcdprWriter.write(comQuantidadeErrada);

        assertThat(texto).isEqualTo(amostra());
        assertThat(texto).endsWith("|13\r\n");
    }

    @Test
    @DisplayName("Não altera o modelo ao escrever")
    void shouldNotChangeModelWhenWriting() {
        Lcdpr modelo = Amostras.valido();
        Lcdpr antes = Amostras.valido();

        LcdprWriter.write(modelo);

        assertThat(modelo).isEqualTo(antes);
    }

    @Test
    @DisplayName("Rejeita modelo sem bloco 0")
    void shouldRejectModelWithoutBloco0() {
        Lcdpr semBloco0 = Lcdpr.builder()
                .bloco9(EncerramentoArquivo.builder().build())
                .build();

        assertThatThrownBy(() -> LcdprWriter.write(semBloco0))
                .isInstanceOf(LcdprException.class);
    }

    @Test
    @DisplayName("Grava em arquivo com a mesma codificação e os mesmos bytes da amostra")
    void shouldWriteFileWithSameBytesAsSample() throws IOException {
        Path arquivo = diretorio.resolve("lcdpr-gravado.txt");

        LcdprWriter.write(Amostras.valido(), arquivo, StandardCharsets.UTF_8);

        assertThat(Files.readAllBytes(arquivo)).isEqualTo(bytesAmostra());
    }

    private String amostra() throws IOException {
        return new String(bytesAmostra(), StandardCharsets.UTF_8);
    }

    private byte[] bytesAmostra() throws IOException {
        try (InputStream entrada = getClass().getResourceAsStream("/samples/lcdpr-valido.txt")) {
            return entrada.readAllBytes();
        }
    }
}
