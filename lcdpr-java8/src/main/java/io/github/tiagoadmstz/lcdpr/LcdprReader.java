package io.github.tiagoadmstz.lcdpr;

import java.io.IOException;
import java.io.Reader;
import java.io.StringWriter;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CodingErrorAction;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

import io.github.tiagoadmstz.lcdpr.leiaute.Registro;
import io.github.tiagoadmstz.lcdpr.modelo.AberturaIdentificacao;
import io.github.tiagoadmstz.lcdpr.modelo.ContaBancaria;
import io.github.tiagoadmstz.lcdpr.modelo.DadosCadastrais;
import io.github.tiagoadmstz.lcdpr.modelo.DemonstrativoResultado;
import io.github.tiagoadmstz.lcdpr.modelo.EncerramentoArquivo;
import io.github.tiagoadmstz.lcdpr.modelo.IdentificacaoPessoaFisica;
import io.github.tiagoadmstz.lcdpr.modelo.ImovelRural;
import io.github.tiagoadmstz.lcdpr.modelo.Lancamento;
import io.github.tiagoadmstz.lcdpr.modelo.ParametrosTributacao;
import io.github.tiagoadmstz.lcdpr.modelo.ResumoMensal;
import io.github.tiagoadmstz.lcdpr.modelo.Terceiro;

/**
 * Lê o arquivo LCDPR (texto separado por "|", com quebra de linha CRLF ou LF) e monta o modelo.
 * Todo erro sai como {@link LcdprException} com o número da linha quando a causa está numa linha.
 */
public final class LcdprReader {

    private static final String SEPARADOR_REGEX = "\\|";
    private static final String BOM = "﻿";
    private static final Set<Registro> UNICOS = EnumSet.of(
            Registro.R0000, Registro.R0010, Registro.R0030, Registro.R9999);

    private LcdprReader() {
    }

    /**
     * Lê o conteúdo completo do arquivo. Um BOM UTF-8 no início é ignorado.
     *
     * @param conteudo texto do arquivo
     * @return modelo lido; o QTD_LIN do 9999 é o valor do arquivo
     * @throws LcdprException se o conteúdo for nulo ou o arquivo estiver fora do leiaute
     */
    public static Lcdpr read(String conteudo) {
        if (conteudo == null) {
            throw new LcdprException("Conteúdo do LCDPR nulo");
        }
        String texto = conteudo.startsWith(BOM) ? conteudo.substring(BOM.length()) : conteudo;
        List<String> linhas = dividirLinhas(texto);
        if (linhas.isEmpty()) {
            throw new LcdprException("Arquivo LCDPR vazio");
        }
        Leitura leitura = new Leitura();
        for (int i = 0; i < linhas.size(); i++) {
            leitura.processa(linhas.get(i), i + 1);
        }
        return leitura.montar();
    }

    /**
     * Lê o arquivo a partir de um leitor; o leitor não é fechado.
     *
     * @param leitor origem do texto
     * @return modelo lido
     * @throws LcdprException se o leitor for nulo, o arquivo estiver fora do leiaute ou houver falha de E/S
     */
    public static Lcdpr read(Reader leitor) {
        if (leitor == null) {
            throw new LcdprException("Leitor do LCDPR nulo");
        }
        StringWriter conteudo = new StringWriter();
        char[] buffer = new char[8192];
        try {
            int lidos;
            while ((lidos = leitor.read(buffer)) != -1) {
                conteudo.write(buffer, 0, lidos);
            }
        } catch (IOException e) {
            throw new LcdprException("Falha ao ler o LCDPR", e);
        }
        return read(conteudo.toString());
    }

    /**
     * Lê o arquivo do caminho informado.
     *
     * @param arquivo caminho do arquivo
     * @param charset codificação do arquivo
     * @return modelo lido
     * @throws LcdprException se o caminho for nulo, o arquivo estiver fora do leiaute ou houver falha de E/S
     */
    public static Lcdpr read(Path arquivo, Charset charset) {
        if (arquivo == null) {
            throw new LcdprException("Caminho do arquivo LCDPR nulo");
        }
        try {
            String conteudo = charset.newDecoder()
                    .onMalformedInput(CodingErrorAction.REPORT)
                    .onUnmappableCharacter(CodingErrorAction.REPORT)
                    .decode(ByteBuffer.wrap(Files.readAllBytes(arquivo)))
                    .toString();
            return read(conteudo);
        } catch (IOException e) {
            throw new LcdprException("Falha ao ler o arquivo LCDPR", e);
        }
    }

    private static List<String> dividirLinhas(String conteudo) {
        String[] partes = conteudo.split("\n", -1);
        List<String> linhas = new ArrayList<>(partes.length);
        for (String parte : partes) {
            if (parte.endsWith("\r")) {
                linhas.add(parte.substring(0, parte.length() - 1));
            } else {
                linhas.add(parte);
            }
        }
        if (!linhas.isEmpty() && linhas.get(linhas.size() - 1).isEmpty()) {
            linhas.remove(linhas.size() - 1);
        }
        return linhas;
    }

    private static final class Leitura {

        private final Set<Registro> vistos = EnumSet.noneOf(Registro.class);
        private final List<ImovelRural.Builder> imoveis = new ArrayList<>();
        private final List<ContaBancaria> contas = new ArrayList<>();
        private final List<Lancamento> lancamentos = new ArrayList<>();
        private final List<ResumoMensal> resumos = new ArrayList<>();
        private IdentificacaoPessoaFisica identificacao;
        private ParametrosTributacao parametrosTributacao;
        private DadosCadastrais dadosCadastrais;
        private EncerramentoArquivo encerramento;
        private boolean encerrado;

        void processa(String linha, int numero) {
            if (encerrado) {
                throw new LcdprException("Registro após o 9999", numero);
            }
            if (linha.isEmpty()) {
                throw new LcdprException("Linha vazia não é permitida", numero);
            }
            String[] campos = linha.split(SEPARADOR_REGEX, -1);
            Registro registro = registro(campos[0], numero);
            if (numero == 1 && registro != Registro.R0000) {
                throw new LcdprException("O arquivo deve começar pelo registro 0000", numero);
            }
            int recebidos = campos.length - 1;
            int esperados = registro.campos().size();
            if (recebidos != esperados) {
                throw new LcdprException("Registro " + registro.codigo() + " com " + recebidos
                        + " campos; esperado " + esperados, numero);
            }
            if (UNICOS.contains(registro) && !vistos.add(registro)) {
                throw new LcdprException("Registro " + registro.codigo() + " repetido", numero);
            }
            List<String> valores = Arrays.asList(campos).subList(1, campos.length);
            try {
                aplica(registro, valores);
            } catch (LcdprException e) {
                throw new LcdprException(e.getMessage(), numero, e);
            } catch (RuntimeException e) {
                throw new LcdprException("Erro ao ler o registro " + registro.codigo(), numero, e);
            }
        }

        Lcdpr montar() {
            if (encerramento == null) {
                throw new LcdprException("Arquivo LCDPR sem registro 9999");
            }
            List<ImovelRural> imoveisMontados = new ArrayList<>(imoveis.size());
            for (ImovelRural.Builder imovel : imoveis) {
                imoveisMontados.add(imovel.build());
            }
            AberturaIdentificacao bloco0 = AberturaIdentificacao.builder()
                    .identificacao(identificacao)
                    .parametrosTributacao(parametrosTributacao)
                    .dadosCadastrais(dadosCadastrais)
                    .imoveisRurais(imoveisMontados)
                    .contasBancarias(contas)
                    .build();
            DemonstrativoResultado blocoQ = DemonstrativoResultado.builder()
                    .lancamentos(lancamentos)
                    .resumosMensais(resumos)
                    .build();
            return Lcdpr.builder()
                    .bloco0(bloco0)
                    .blocoQ(blocoQ)
                    .bloco9(encerramento)
                    .build();
        }

        private static Registro registro(String codigo, int numero) {
            try {
                return Registro.fromCodigo(codigo);
            } catch (IllegalArgumentException e) {
                throw new LcdprException(e.getMessage(), numero, e);
            }
        }

        private void aplica(Registro registro, List<String> valores) {
            switch (registro) {
                case R0000:
                    identificacao = RegistroCodec.identificacao(valores);
                    break;
                case R0010:
                    parametrosTributacao = RegistroCodec.parametrosTributacao(valores);
                    break;
                case R0030:
                    dadosCadastrais = RegistroCodec.dadosCadastrais(valores);
                    break;
                case R0040:
                    imoveis.add(RegistroCodec.imovelRural(valores).toBuilder());
                    break;
                case R0045:
                    anexaTerceiro(RegistroCodec.terceiro(valores));
                    break;
                case R0050:
                    contas.add(RegistroCodec.contaBancaria(valores));
                    break;
                case RQ100:
                    lancamentos.add(RegistroCodec.lancamento(valores));
                    break;
                case RQ200:
                    resumos.add(RegistroCodec.resumoMensal(valores));
                    break;
                case R9999:
                    encerramento = RegistroCodec.encerramento(valores);
                    encerrado = true;
                    break;
            }
        }

        private void anexaTerceiro(Terceiro terceiro) {
            if (imoveis.isEmpty()) {
                throw new LcdprException("Registro 0045 sem 0040 anterior");
            }
            imoveis.get(imoveis.size() - 1).addTerceiro(terceiro);
        }
    }
}
