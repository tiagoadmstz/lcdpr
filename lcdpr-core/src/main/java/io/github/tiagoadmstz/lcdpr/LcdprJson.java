package io.github.tiagoadmstz.lcdpr;

import java.io.IOException;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.function.Function;

import tools.jackson.core.JacksonException;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.json.JsonMapper;
import tools.jackson.databind.node.ArrayNode;
import tools.jackson.databind.node.ObjectNode;

import io.github.tiagoadmstz.lcdpr.leiaute.Campo;
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
 * Conversão entre o modelo e JSON, no formato compatível com o JSON da versão 1.x.
 * Cada registro é um objeto com "reg" e, na ordem do leiaute, os campos em minúsculas com o texto formatado.
 */
public final class LcdprJson {

    private static final JsonMapper MAPPER = JsonMapper.builder().build();
    private static final String REG = "reg";
    private static final String BLOCO_0 = "bloco_0";
    private static final String IDENTIFICACAO = "identificacao_pessoa_fisica";
    private static final String PARAMETROS = "parametros_tributacao";
    private static final String DADOS_CADASTRAIS = "dados_cadastrais_contribuinte";
    private static final String IMOVEIS = "imoveis_rurais";
    private static final String EXPLORACAO = "exploracao_imoveis_rurais";
    private static final String CONTAS = "contas_bancarias";
    private static final String BLOCO_Q = "bloco_Q";
    private static final String LANCAMENTOS = "demonstrativo_livro_caixa";
    private static final String RESUMOS = "resumo_demonstrativo_livro_caixa";
    private static final String BLOCO_9 = "bloco_9";

    private LcdprJson() {
    }

    /**
     * Gera o JSON do modelo em uma linha, sem quebras de linha.
     *
     * @param lcdpr modelo a converter
     * @return texto JSON
     * @throws LcdprException se o bloco 0 ou algum de seus registros obrigatórios estiver ausente
     */
    public static String toJson(Lcdpr lcdpr) {
        if (lcdpr == null || lcdpr.bloco0() == null) {
            throw new LcdprException("Bloco 0 ausente");
        }
        AberturaIdentificacao bloco0 = lcdpr.bloco0();
        if (bloco0.identificacao() == null) {
            throw new LcdprException("Registro 0000 ausente");
        }
        if (bloco0.parametrosTributacao() == null) {
            throw new LcdprException("Registro 0010 ausente");
        }
        if (bloco0.dadosCadastrais() == null) {
            throw new LcdprException("Registro 0030 ausente");
        }

        ObjectNode raiz = MAPPER.createObjectNode();
        ObjectNode bloco0Json = raiz.putObject(BLOCO_0);
        bloco0Json.set(IDENTIFICACAO, registro(Registro.R0000, RegistroCodec.valores(bloco0.identificacao())));
        bloco0Json.set(PARAMETROS, registro(Registro.R0010, RegistroCodec.valores(bloco0.parametrosTributacao())));
        bloco0Json.set(DADOS_CADASTRAIS, registro(Registro.R0030, RegistroCodec.valores(bloco0.dadosCadastrais())));
        ArrayNode imoveis = bloco0Json.putArray(IMOVEIS);
        for (ImovelRural imovel : bloco0.imoveisRurais()) {
            ObjectNode imovelJson = registro(Registro.R0040, RegistroCodec.valores(imovel));
            ArrayNode exploracao = imovelJson.putArray(EXPLORACAO);
            for (Terceiro terceiro : imovel.terceiros()) {
                exploracao.add(registro(Registro.R0045, RegistroCodec.valores(terceiro)));
            }
            imoveis.add(imovelJson);
        }
        ArrayNode contas = bloco0Json.putArray(CONTAS);
        for (ContaBancaria conta : bloco0.contasBancarias()) {
            contas.add(registro(Registro.R0050, RegistroCodec.valores(conta)));
        }

        DemonstrativoResultado blocoQ = lcdpr.blocoQ();
        if (blocoQ != null) {
            ObjectNode blocoQJson = raiz.putObject(BLOCO_Q);
            ArrayNode lancamentos = blocoQJson.putArray(LANCAMENTOS);
            for (Lancamento lancamento : blocoQ.lancamentos()) {
                lancamentos.add(registro(Registro.RQ100, RegistroCodec.valores(lancamento)));
            }
            ArrayNode resumos = blocoQJson.putArray(RESUMOS);
            for (ResumoMensal resumo : blocoQ.resumosMensais()) {
                resumos.add(registro(Registro.RQ200, RegistroCodec.valores(resumo)));
            }
        }

        EncerramentoArquivo bloco9 = lcdpr.bloco9();
        if (bloco9 != null) {
            raiz.set(BLOCO_9, registro(Registro.R9999, RegistroCodec.valoresModelo(bloco9)));
        }
        try {
            return MAPPER.writeValueAsString(raiz);
        } catch (JacksonException e) {
            throw new LcdprException("Falha ao gerar o JSON do LCDPR", e);
        }
    }

    /**
     * Escreve o JSON do modelo no escritor informado; o escritor não é fechado.
     *
     * @param lcdpr modelo a converter
     * @param escritor destino do texto
     * @throws LcdprException se o modelo for inválido para conversão ou houver falha de E/S
     */
    public static void toJson(Lcdpr lcdpr, Writer escritor) {
        String json = toJson(lcdpr);
        try {
            escritor.write(json);
            escritor.flush();
        } catch (IOException e) {
            throw new LcdprException("Falha ao escrever o JSON do LCDPR", e);
        }
    }

    /**
     * Lê o modelo a partir do JSON. Chaves sem diferenciar maiúsculas; chaves desconhecidas são ignoradas.
     *
     * @param json texto JSON
     * @return modelo lido
     * @throws LcdprException se o JSON for inválido ou algum registro estiver fora do leiaute
     */
    public static Lcdpr fromJson(String json) {
        JsonNode raiz;
        try {
            raiz = MAPPER.readTree(json);
        } catch (JacksonException e) {
            throw new LcdprException("JSON do LCDPR inválido", e);
        }
        if (raiz == null || !raiz.isObject()) {
            throw new LcdprException("JSON do LCDPR deve ser um objeto");
        }

        JsonNode bloco0Json = objetoObrigatorio(raiz, BLOCO_0);
        AberturaIdentificacao.Builder bloco0 = AberturaIdentificacao.builder()
                .identificacao(lerObjeto(Registro.R0000,
                        objetoObrigatorio(bloco0Json, IDENTIFICACAO), RegistroCodec::identificacao))
                .parametrosTributacao(lerObjeto(Registro.R0010,
                        objetoObrigatorio(bloco0Json, PARAMETROS), RegistroCodec::parametrosTributacao))
                .dadosCadastrais(lerObjeto(Registro.R0030,
                        objetoObrigatorio(bloco0Json, DADOS_CADASTRAIS), RegistroCodec::dadosCadastrais));
        for (JsonNode imovelJson : elementos(bloco0Json, IMOVEIS)) {
            ImovelRural.Builder imovel = lerObjeto(Registro.R0040, imovelJson, RegistroCodec::imovelRural).toBuilder();
            for (JsonNode terceiroJson : elementos(imovelJson, EXPLORACAO)) {
                imovel.addTerceiro(lerObjeto(Registro.R0045, terceiroJson, RegistroCodec::terceiro));
            }
            bloco0.addImovelRural(imovel.build());
        }
        for (JsonNode contaJson : elementos(bloco0Json, CONTAS)) {
            bloco0.addContaBancaria(lerObjeto(Registro.R0050, contaJson, RegistroCodec::contaBancaria));
        }

        DemonstrativoResultado blocoQ = null;
        JsonNode blocoQJson = objetoOpcional(raiz, BLOCO_Q);
        if (blocoQJson != null) {
            DemonstrativoResultado.Builder demonstrativo = DemonstrativoResultado.builder();
            for (JsonNode lancamentoJson : elementos(blocoQJson, LANCAMENTOS)) {
                demonstrativo.addLancamento(lerObjeto(Registro.RQ100, lancamentoJson, RegistroCodec::lancamento));
            }
            for (JsonNode resumoJson : elementos(blocoQJson, RESUMOS)) {
                demonstrativo.addResumoMensal(lerObjeto(Registro.RQ200, resumoJson, RegistroCodec::resumoMensal));
            }
            blocoQ = demonstrativo.build();
        }

        EncerramentoArquivo bloco9 = null;
        JsonNode bloco9Json = objetoOpcional(raiz, BLOCO_9);
        if (bloco9Json != null) {
            bloco9 = lerObjeto(Registro.R9999, bloco9Json, RegistroCodec::encerramento);
        }

        return Lcdpr.builder()
                .bloco0(bloco0.build())
                .blocoQ(blocoQ)
                .bloco9(bloco9)
                .build();
    }

    /**
     * Lê o modelo a partir do leitor de texto JSON; o leitor não é fechado.
     *
     * @param leitor origem do texto JSON
     * @return modelo lido
     * @throws LcdprException se o JSON for inválido, o leiaute não for respeitado ou houver falha de E/S
     */
    public static Lcdpr fromJson(Reader leitor) {
        StringWriter conteudo = new StringWriter();
        try {
            leitor.transferTo(conteudo);
        } catch (IOException e) {
            throw new LcdprException("Falha ao ler o JSON do LCDPR", e);
        }
        return fromJson(conteudo.toString());
    }

    private static ObjectNode registro(Registro registro, List<String> valores) {
        ObjectNode objeto = MAPPER.createObjectNode();
        objeto.put(REG, registro.codigo());
        List<Campo> campos = registro.campos();
        for (int i = 0; i < campos.size(); i++) {
            String valor = valores.get(i);
            if (!valor.isEmpty()) {
                objeto.put(campos.get(i).nome().toLowerCase(Locale.ROOT), valor);
            }
        }
        return objeto;
    }

    private static <T> T lerObjeto(Registro registro, JsonNode objeto, Function<List<String>, T> codec) {
        if (!objeto.isObject()) {
            throw new LcdprException("Registro " + registro.codigo() + " deve ser um objeto JSON");
        }
        Map<String, JsonNode> chaves = new HashMap<>();
        for (Map.Entry<String, JsonNode> entrada : objeto.properties()) {
            String chave = entrada.getKey().toLowerCase(Locale.ROOT);
            if (chaves.put(chave, entrada.getValue()) != null) {
                throw new LcdprException("Registro " + registro.codigo() + " com chave repetida: " + chave);
            }
        }
        List<String> valores = new ArrayList<>(registro.campos().size());
        for (Campo campo : registro.campos()) {
            JsonNode valor = chaves.get(campo.nome().toLowerCase(Locale.ROOT));
            if (valor == null || valor.isNull()) {
                valores.add("");
            } else if (valor.isString() || valor.isNumber()) {
                valores.add(valor.asString());
            } else {
                throw new LcdprException("Registro " + registro.codigo() + "." + campo.nome()
                        + " deve ser texto ou número");
            }
        }
        if (registro == Registro.R0000) {
            valores.set(0, RegistroCodec.NOME_ESC);
        }
        try {
            return codec.apply(valores);
        } catch (LcdprException e) {
            throw new LcdprException("Registro " + registro.codigo() + ": " + e.getMessage(), e);
        }
    }

    private static JsonNode objetoOpcional(JsonNode pai, String chave) {
        JsonNode objeto = pai.get(chave);
        if (objeto == null || objeto.isNull()) {
            return null;
        }
        if (!objeto.isObject()) {
            throw new LcdprException("JSON: " + chave + " deve ser um objeto");
        }
        return objeto;
    }

    private static JsonNode objetoObrigatorio(JsonNode pai, String chave) {
        JsonNode objeto = objetoOpcional(pai, chave);
        if (objeto == null) {
            throw new LcdprException("JSON sem o objeto " + chave);
        }
        return objeto;
    }

    private static List<JsonNode> elementos(JsonNode pai, String chave) {
        JsonNode lista = pai.get(chave);
        if (lista == null || lista.isNull()) {
            return List.of();
        }
        if (!lista.isArray()) {
            throw new LcdprException("JSON: " + chave + " deve ser uma lista");
        }
        List<JsonNode> elementos = new ArrayList<>(lista.size());
        for (int i = 0; i < lista.size(); i++) {
            elementos.add(lista.get(i));
        }
        return elementos;
    }
}
