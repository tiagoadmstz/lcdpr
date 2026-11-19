package io.github.tiagoadmstz.lcdpr;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CodingErrorAction;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import io.github.tiagoadmstz.lcdpr.leiaute.Registro;
import io.github.tiagoadmstz.lcdpr.modelo.AberturaIdentificacao;
import io.github.tiagoadmstz.lcdpr.modelo.ContaBancaria;
import io.github.tiagoadmstz.lcdpr.modelo.DadosCadastrais;
import io.github.tiagoadmstz.lcdpr.modelo.DemonstrativoResultado;
import io.github.tiagoadmstz.lcdpr.modelo.IdentificacaoPessoaFisica;
import io.github.tiagoadmstz.lcdpr.modelo.ImovelRural;
import io.github.tiagoadmstz.lcdpr.modelo.Lancamento;
import io.github.tiagoadmstz.lcdpr.modelo.ParametrosTributacao;
import io.github.tiagoadmstz.lcdpr.modelo.ResumoMensal;
import io.github.tiagoadmstz.lcdpr.modelo.Terceiro;

/**
 * Gera o arquivo LCDPR (texto separado por "|", com quebra de linha CRLF) a partir do modelo.
 * O modelo não é alterado; o QTD_LIN do registro 9999 é sempre calculado.
 */
public final class LcdprWriter {

    private LcdprWriter() {
    }

    /**
     * Gera o conteúdo completo do arquivo.
     *
     * @param lcdpr modelo a escrever
     * @return texto do arquivo, com CRLF inclusive na última linha
     * @throws LcdprException se algum bloco obrigatório estiver ausente ou um valor não puder ser formatado
     */
    public static String write(Lcdpr lcdpr) {
        StringWriter saida = new StringWriter();
        write(lcdpr, saida);
        return saida.toString();
    }

    /**
     * Escreve o arquivo no escritor informado; o escritor não é fechado.
     *
     * @param lcdpr modelo a escrever
     * @param escritor destino do texto
     * @throws LcdprException se o modelo for inválido para escrita ou houver falha de E/S
     */
    public static void write(Lcdpr lcdpr, Writer escritor) {
        List<String> linhas = linhas(lcdpr);
        try {
            for (String linha : linhas) {
                escritor.write(linha);
                escritor.write(Formatos.FIM_DE_LINHA);
            }
            escritor.flush();
        } catch (IOException e) {
            throw new LcdprException("Falha ao escrever o LCDPR", e);
        }
    }

    /**
     * Grava o arquivo no caminho informado, substituindo o conteúdo existente.
     *
     * @param lcdpr modelo a escrever
     * @param arquivo caminho do arquivo de destino
     * @param charset codificação do arquivo
     * @throws LcdprException se o modelo for inválido para escrita ou houver falha de E/S
     */
    public static void write(Lcdpr lcdpr, Path arquivo, Charset charset) {
        String conteudo = write(lcdpr);
        try {
            ByteBuffer bytes = charset.newEncoder()
                    .onMalformedInput(CodingErrorAction.REPORT)
                    .onUnmappableCharacter(CodingErrorAction.REPORT)
                    .encode(CharBuffer.wrap(conteudo));
            byte[] dados = new byte[bytes.remaining()];
            bytes.get(dados);
            Files.write(arquivo, dados);
        } catch (IOException e) {
            throw new LcdprException("Falha ao gravar o LCDPR no arquivo", e);
        }
    }

    private static List<String> linhas(Lcdpr lcdpr) {
        AberturaIdentificacao bloco0 = exigir(lcdpr == null ? null : lcdpr.bloco0(), "Bloco 0");
        IdentificacaoPessoaFisica identificacao = exigir(bloco0.identificacao(), "Registro 0000");
        ParametrosTributacao parametros = exigir(bloco0.parametrosTributacao(), "Registro 0010");
        DadosCadastrais dados = exigir(bloco0.dadosCadastrais(), "Registro 0030");

        List<String> linhas = new ArrayList<>();
        linhas.add(linha(Registro.R0000, RegistroCodec.valores(identificacao)));
        linhas.add(linha(Registro.R0010, RegistroCodec.valores(parametros)));
        linhas.add(linha(Registro.R0030, RegistroCodec.valores(dados)));
        for (ImovelRural imovel : bloco0.imoveisRurais()) {
            linhas.add(linha(Registro.R0040, RegistroCodec.valores(imovel)));
            for (Terceiro terceiro : imovel.terceiros()) {
                linhas.add(linha(Registro.R0045, RegistroCodec.valores(terceiro)));
            }
        }
        for (ContaBancaria conta : bloco0.contasBancarias()) {
            linhas.add(linha(Registro.R0050, RegistroCodec.valores(conta)));
        }
        DemonstrativoResultado blocoQ = lcdpr.blocoQ();
        if (blocoQ != null) {
            for (Lancamento lancamento : blocoQ.lancamentos()) {
                linhas.add(linha(Registro.RQ100, RegistroCodec.valores(lancamento)));
            }
            for (ResumoMensal resumo : blocoQ.resumosMensais()) {
                linhas.add(linha(Registro.RQ200, RegistroCodec.valores(resumo)));
            }
        }
        long quantidadeLinhas = linhas.size() + 1L;
        linhas.add(linha(Registro.R9999, RegistroCodec.valores(lcdpr.bloco9(), quantidadeLinhas)));
        return linhas;
    }

    private static String linha(Registro registro, List<String> valores) {
        return registro.codigo() + Formatos.SEPARADOR + String.join(Formatos.SEPARADOR, valores);
    }

    private static <T> T exigir(T valor, String nome) {
        if (valor == null) {
            throw new LcdprException(nome + " ausente");
        }
        return valor;
    }
}
