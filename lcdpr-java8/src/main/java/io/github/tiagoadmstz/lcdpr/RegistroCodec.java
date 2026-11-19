package io.github.tiagoadmstz.lcdpr;

import static io.github.tiagoadmstz.lcdpr.Formatos.codigo;
import static io.github.tiagoadmstz.lcdpr.Formatos.data;
import static io.github.tiagoadmstz.lcdpr.Formatos.lerData;
import static io.github.tiagoadmstz.lcdpr.Formatos.lerEnum;
import static io.github.tiagoadmstz.lcdpr.Formatos.lerMes;
import static io.github.tiagoadmstz.lcdpr.Formatos.lerPercentual;
import static io.github.tiagoadmstz.lcdpr.Formatos.lerTexto;
import static io.github.tiagoadmstz.lcdpr.Formatos.lerValor;
import static io.github.tiagoadmstz.lcdpr.Formatos.mes;
import static io.github.tiagoadmstz.lcdpr.Formatos.percentual;
import static io.github.tiagoadmstz.lcdpr.Formatos.texto;
import static io.github.tiagoadmstz.lcdpr.Formatos.valor;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import io.github.tiagoadmstz.lcdpr.modelo.ContaBancaria;
import io.github.tiagoadmstz.lcdpr.modelo.DadosCadastrais;
import io.github.tiagoadmstz.lcdpr.modelo.EncerramentoArquivo;
import io.github.tiagoadmstz.lcdpr.modelo.IdentificacaoPessoaFisica;
import io.github.tiagoadmstz.lcdpr.modelo.ImovelRural;
import io.github.tiagoadmstz.lcdpr.modelo.Lancamento;
import io.github.tiagoadmstz.lcdpr.modelo.ParametrosTributacao;
import io.github.tiagoadmstz.lcdpr.modelo.ResumoMensal;
import io.github.tiagoadmstz.lcdpr.modelo.Terceiro;
import io.github.tiagoadmstz.lcdpr.tabela.FormaApuracao;
import io.github.tiagoadmstz.lcdpr.tabela.IndicadorInicioPeriodo;
import io.github.tiagoadmstz.lcdpr.tabela.NaturezaSaldo;
import io.github.tiagoadmstz.lcdpr.tabela.SituacaoEspecial;
import io.github.tiagoadmstz.lcdpr.tabela.TipoContraparte;
import io.github.tiagoadmstz.lcdpr.tabela.TipoDocumento;
import io.github.tiagoadmstz.lcdpr.tabela.TipoExploracao;
import io.github.tiagoadmstz.lcdpr.tabela.TipoLancamento;

/**
 * Conversão entre o modelo e a lista de valores formatados de cada registro, na ordem do leiaute (sem o REG).
 * A lista de leitura tem exatamente os valores do registro, verificados pelo LcdprReader.
 */
final class RegistroCodec {

    static final String NOME_ESC = "LCDPR";

    private RegistroCodec() {
    }

    static List<String> valores(IdentificacaoPessoaFisica identificacao) {
        return Collections.unmodifiableList(Arrays.asList(
                NOME_ESC,
                texto(identificacao.codigoVersao()),
                texto(identificacao.cpf()),
                texto(identificacao.nome()),
                codigo(identificacao.indicadorInicioPeriodo(), IndicadorInicioPeriodo::codigo),
                codigo(identificacao.situacaoEspecial(), SituacaoEspecial::codigo),
                data(identificacao.dataSituacaoEspecial()),
                data(identificacao.dataInicial()),
                data(identificacao.dataFinal())));
    }

    static List<String> valores(ParametrosTributacao parametros) {
        return Collections.singletonList(codigo(parametros.formaApuracao(), FormaApuracao::codigo));
    }

    static List<String> valores(DadosCadastrais dados) {
        return Collections.unmodifiableList(Arrays.asList(
                texto(dados.endereco()),
                texto(dados.numero()),
                texto(dados.complemento()),
                texto(dados.bairro()),
                texto(dados.uf()),
                texto(dados.codigoMunicipio()),
                texto(dados.cep()),
                texto(dados.telefone()),
                texto(dados.email())));
    }

    static List<String> valores(ImovelRural imovel) {
        return Collections.unmodifiableList(Arrays.asList(
                texto(imovel.codigoImovel()),
                texto(imovel.pais()),
                texto(imovel.moeda()),
                texto(imovel.cadastroItr()),
                texto(imovel.caepf()),
                texto(imovel.inscricaoEstadual()),
                texto(imovel.nome()),
                texto(imovel.endereco()),
                texto(imovel.numero()),
                texto(imovel.complemento()),
                texto(imovel.bairro()),
                texto(imovel.uf()),
                texto(imovel.codigoMunicipio()),
                texto(imovel.cep()),
                codigo(imovel.tipoExploracao(), TipoExploracao::codigo),
                percentual(imovel.participacao())));
    }

    static List<String> valores(Terceiro terceiro) {
        return Collections.unmodifiableList(Arrays.asList(
                texto(terceiro.codigoImovel()),
                codigo(terceiro.tipoContraparte(), TipoContraparte::codigo),
                texto(terceiro.cpfCnpj()),
                texto(terceiro.nome()),
                percentual(terceiro.participacao())));
    }

    static List<String> valores(ContaBancaria conta) {
        return Collections.unmodifiableList(Arrays.asList(
                texto(conta.codigoConta()),
                texto(conta.pais()),
                texto(conta.banco()),
                texto(conta.nomeBanco()),
                texto(conta.agencia()),
                texto(conta.numeroConta())));
    }

    static List<String> valores(Lancamento lancamento) {
        return Collections.unmodifiableList(Arrays.asList(
                data(lancamento.data()),
                texto(lancamento.codigoImovel()),
                texto(lancamento.codigoConta()),
                texto(lancamento.numeroDocumento()),
                codigo(lancamento.tipoDocumento(), TipoDocumento::codigo),
                texto(lancamento.historico()),
                texto(lancamento.cpfCnpjParticipante()),
                codigo(lancamento.tipoLancamento(), TipoLancamento::codigo),
                valor(lancamento.valorEntrada()),
                valor(lancamento.valorSaida()),
                valor(lancamento.saldoFinal()),
                codigo(lancamento.naturezaSaldoFinal(), NaturezaSaldo::codigo)));
    }

    static List<String> valores(ResumoMensal resumo) {
        return Collections.unmodifiableList(Arrays.asList(
                mes(resumo.mes()),
                valor(resumo.valorEntrada()),
                valor(resumo.valorSaida()),
                valor(resumo.saldoFinal()),
                codigo(resumo.naturezaSaldoFinal(), NaturezaSaldo::codigo)));
    }

    static List<String> valores(EncerramentoArquivo encerramento, long quantidadeLinhas) {
        EncerramentoArquivo bloco9 = encerramento != null ? encerramento : EncerramentoArquivo.builder().build();
        return valoresEncerramento(bloco9, String.valueOf(quantidadeLinhas));
    }

    static List<String> valoresModelo(EncerramentoArquivo encerramento) {
        String quantidadeLinhas = encerramento.quantidadeLinhas() == null
                ? ""
                : encerramento.quantidadeLinhas().toString();
        return valoresEncerramento(encerramento, quantidadeLinhas);
    }

    private static List<String> valoresEncerramento(EncerramentoArquivo bloco9, String quantidadeLinhas) {
        return Collections.unmodifiableList(Arrays.asList(
                texto(bloco9.nomeContador()),
                texto(bloco9.cpfCnpjContador()),
                texto(bloco9.crcContador()),
                texto(bloco9.emailContador()),
                texto(bloco9.telefoneContador()),
                quantidadeLinhas));
    }

    static IdentificacaoPessoaFisica identificacao(List<String> valores) {
        if (!NOME_ESC.equals(valores.get(0))) {
            throw new LcdprException("0000.NOME_ESC deve ser LCDPR");
        }
        return IdentificacaoPessoaFisica.builder()
                .codigoVersao(lerTexto(valores.get(1)))
                .cpf(lerTexto(valores.get(2)))
                .nome(lerTexto(valores.get(3)))
                .indicadorInicioPeriodo(lerEnum(valores.get(4), IndicadorInicioPeriodo::fromCodigo))
                .situacaoEspecial(lerEnum(valores.get(5), SituacaoEspecial::fromCodigo))
                .dataSituacaoEspecial(lerData(valores.get(6)))
                .dataInicial(lerData(valores.get(7)))
                .dataFinal(lerData(valores.get(8)))
                .build();
    }

    static ParametrosTributacao parametrosTributacao(List<String> valores) {
        return ParametrosTributacao.builder()
                .formaApuracao(lerEnum(valores.get(0), FormaApuracao::fromCodigo))
                .build();
    }

    static DadosCadastrais dadosCadastrais(List<String> valores) {
        return DadosCadastrais.builder()
                .endereco(lerTexto(valores.get(0)))
                .numero(lerTexto(valores.get(1)))
                .complemento(lerTexto(valores.get(2)))
                .bairro(lerTexto(valores.get(3)))
                .uf(lerTexto(valores.get(4)))
                .codigoMunicipio(lerTexto(valores.get(5)))
                .cep(lerTexto(valores.get(6)))
                .telefone(lerTexto(valores.get(7)))
                .email(lerTexto(valores.get(8)))
                .build();
    }

    static ImovelRural imovelRural(List<String> valores) {
        return ImovelRural.builder()
                .codigoImovel(lerTexto(valores.get(0)))
                .pais(lerTexto(valores.get(1)))
                .moeda(lerTexto(valores.get(2)))
                .cadastroItr(lerTexto(valores.get(3)))
                .caepf(lerTexto(valores.get(4)))
                .inscricaoEstadual(lerTexto(valores.get(5)))
                .nome(lerTexto(valores.get(6)))
                .endereco(lerTexto(valores.get(7)))
                .numero(lerTexto(valores.get(8)))
                .complemento(lerTexto(valores.get(9)))
                .bairro(lerTexto(valores.get(10)))
                .uf(lerTexto(valores.get(11)))
                .codigoMunicipio(lerTexto(valores.get(12)))
                .cep(lerTexto(valores.get(13)))
                .tipoExploracao(lerEnum(valores.get(14), TipoExploracao::fromCodigo))
                .participacao(lerPercentual(valores.get(15)))
                .build();
    }

    static Terceiro terceiro(List<String> valores) {
        return Terceiro.builder()
                .codigoImovel(lerTexto(valores.get(0)))
                .tipoContraparte(lerEnum(valores.get(1), TipoContraparte::fromCodigo))
                .cpfCnpj(lerTexto(valores.get(2)))
                .nome(lerTexto(valores.get(3)))
                .participacao(lerPercentual(valores.get(4)))
                .build();
    }

    static ContaBancaria contaBancaria(List<String> valores) {
        return ContaBancaria.builder()
                .codigoConta(lerTexto(valores.get(0)))
                .pais(lerTexto(valores.get(1)))
                .banco(lerTexto(valores.get(2)))
                .nomeBanco(lerTexto(valores.get(3)))
                .agencia(lerTexto(valores.get(4)))
                .numeroConta(lerTexto(valores.get(5)))
                .build();
    }

    static Lancamento lancamento(List<String> valores) {
        return Lancamento.builder()
                .data(lerData(valores.get(0)))
                .codigoImovel(lerTexto(valores.get(1)))
                .codigoConta(lerTexto(valores.get(2)))
                .numeroDocumento(lerTexto(valores.get(3)))
                .tipoDocumento(lerEnum(valores.get(4), TipoDocumento::fromCodigo))
                .historico(lerTexto(valores.get(5)))
                .cpfCnpjParticipante(lerTexto(valores.get(6)))
                .tipoLancamento(lerEnum(valores.get(7), TipoLancamento::fromCodigo))
                .valorEntrada(lerValor(valores.get(8)))
                .valorSaida(lerValor(valores.get(9)))
                .saldoFinal(lerValor(valores.get(10)))
                .naturezaSaldoFinal(lerEnum(valores.get(11), NaturezaSaldo::fromCodigo))
                .build();
    }

    static ResumoMensal resumoMensal(List<String> valores) {
        return ResumoMensal.builder()
                .mes(lerMes(valores.get(0)))
                .valorEntrada(lerValor(valores.get(1)))
                .valorSaida(lerValor(valores.get(2)))
                .saldoFinal(lerValor(valores.get(3)))
                .naturezaSaldoFinal(lerEnum(valores.get(4), NaturezaSaldo::fromCodigo))
                .build();
    }

    static EncerramentoArquivo encerramento(List<String> valores) {
        return EncerramentoArquivo.builder()
                .nomeContador(lerTexto(valores.get(0)))
                .cpfCnpjContador(lerTexto(valores.get(1)))
                .crcContador(lerTexto(valores.get(2)))
                .emailContador(lerTexto(valores.get(3)))
                .telefoneContador(lerTexto(valores.get(4)))
                .quantidadeLinhas(lerQuantidadeLinhas(valores.get(5)))
                .build();
    }

    private static Long lerQuantidadeLinhas(String campo) {
        if (campo.isEmpty()) {
            return null;
        }
        if (!campo.matches("[0-9]+")) {
            throw new LcdprException("9999.QTD_LIN deve conter só dígitos");
        }
        try {
            return Long.parseLong(campo);
        } catch (NumberFormatException e) {
            throw new LcdprException("9999.QTD_LIN fora do intervalo numérico", e);
        }
    }
}
