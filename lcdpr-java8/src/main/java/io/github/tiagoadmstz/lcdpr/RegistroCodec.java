package io.github.tiagoadmstz.lcdpr;

import static io.github.tiagoadmstz.lcdpr.Formatos.codigo;
import static io.github.tiagoadmstz.lcdpr.Formatos.data;
import static io.github.tiagoadmstz.lcdpr.Formatos.lerEnum;
import static io.github.tiagoadmstz.lcdpr.Formatos.mes;
import static io.github.tiagoadmstz.lcdpr.Formatos.percentual;
import static io.github.tiagoadmstz.lcdpr.Formatos.texto;
import static io.github.tiagoadmstz.lcdpr.Formatos.valor;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

import io.github.tiagoadmstz.lcdpr.leiaute.Campo;
import io.github.tiagoadmstz.lcdpr.leiaute.Registro;
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
 * Todo erro de formato sai como {@link ErroCampo}, com a mensagem no formato "REG.CAMPO: ...".
 */
final class RegistroCodec {

    static final String NOME_ESC = "LCDPR";

    private RegistroCodec() {
    }

    static List<String> valores(IdentificacaoPessoaFisica identificacao) {
        return new Valores(Registro.R0000)
                .campo(() -> NOME_ESC)
                .campo(() -> texto(identificacao.codigoVersao()))
                .campo(() -> texto(identificacao.cpf()))
                .campo(() -> texto(identificacao.nome()))
                .campo(() -> codigo(identificacao.indicadorInicioPeriodo(), IndicadorInicioPeriodo::codigo))
                .campo(() -> codigo(identificacao.situacaoEspecial(), SituacaoEspecial::codigo))
                .campo(() -> data(identificacao.dataSituacaoEspecial()))
                .campo(() -> data(identificacao.dataInicial()))
                .campo(() -> data(identificacao.dataFinal()))
                .lista();
    }

    static List<String> valores(ParametrosTributacao parametros) {
        return new Valores(Registro.R0010)
                .campo(() -> codigo(parametros.formaApuracao(), FormaApuracao::codigo))
                .lista();
    }

    static List<String> valores(DadosCadastrais dados) {
        return new Valores(Registro.R0030)
                .campo(() -> texto(dados.endereco()))
                .campo(() -> texto(dados.numero()))
                .campo(() -> texto(dados.complemento()))
                .campo(() -> texto(dados.bairro()))
                .campo(() -> texto(dados.uf()))
                .campo(() -> texto(dados.codigoMunicipio()))
                .campo(() -> texto(dados.cep()))
                .campo(() -> texto(dados.telefone()))
                .campo(() -> texto(dados.email()))
                .lista();
    }

    static List<String> valores(ImovelRural imovel) {
        return new Valores(Registro.R0040)
                .campo(() -> texto(imovel.codigoImovel()))
                .campo(() -> texto(imovel.pais()))
                .campo(() -> texto(imovel.moeda()))
                .campo(() -> texto(imovel.cadastroItr()))
                .campo(() -> texto(imovel.caepf()))
                .campo(() -> texto(imovel.inscricaoEstadual()))
                .campo(() -> texto(imovel.nome()))
                .campo(() -> texto(imovel.endereco()))
                .campo(() -> texto(imovel.numero()))
                .campo(() -> texto(imovel.complemento()))
                .campo(() -> texto(imovel.bairro()))
                .campo(() -> texto(imovel.uf()))
                .campo(() -> texto(imovel.codigoMunicipio()))
                .campo(() -> texto(imovel.cep()))
                .campo(() -> codigo(imovel.tipoExploracao(), TipoExploracao::codigo))
                .campo(() -> percentual(imovel.participacao()))
                .lista();
    }

    static List<String> valores(Terceiro terceiro) {
        return new Valores(Registro.R0045)
                .campo(() -> texto(terceiro.codigoImovel()))
                .campo(() -> codigo(terceiro.tipoContraparte(), TipoContraparte::codigo))
                .campo(() -> texto(terceiro.cpfCnpj()))
                .campo(() -> texto(terceiro.nome()))
                .campo(() -> percentual(terceiro.participacao()))
                .lista();
    }

    static List<String> valores(ContaBancaria conta) {
        return new Valores(Registro.R0050)
                .campo(() -> texto(conta.codigoConta()))
                .campo(() -> texto(conta.pais()))
                .campo(() -> texto(conta.banco()))
                .campo(() -> texto(conta.nomeBanco()))
                .campo(() -> texto(conta.agencia()))
                .campo(() -> texto(conta.numeroConta()))
                .lista();
    }

    static List<String> valores(Lancamento lancamento) {
        return new Valores(Registro.RQ100)
                .campo(() -> data(lancamento.data()))
                .campo(() -> texto(lancamento.codigoImovel()))
                .campo(() -> texto(lancamento.codigoConta()))
                .campo(() -> texto(lancamento.numeroDocumento()))
                .campo(() -> codigo(lancamento.tipoDocumento(), TipoDocumento::codigo))
                .campo(() -> texto(lancamento.historico()))
                .campo(() -> texto(lancamento.cpfCnpjParticipante()))
                .campo(() -> codigo(lancamento.tipoLancamento(), TipoLancamento::codigo))
                .campo(() -> valor(lancamento.valorEntrada()))
                .campo(() -> valor(lancamento.valorSaida()))
                .campo(() -> valor(lancamento.saldoFinal()))
                .campo(() -> codigo(lancamento.naturezaSaldoFinal(), NaturezaSaldo::codigo))
                .lista();
    }

    static List<String> valores(ResumoMensal resumo) {
        return new Valores(Registro.RQ200)
                .campo(() -> mes(resumo.mes()))
                .campo(() -> valor(resumo.valorEntrada()))
                .campo(() -> valor(resumo.valorSaida()))
                .campo(() -> valor(resumo.saldoFinal()))
                .campo(() -> codigo(resumo.naturezaSaldoFinal(), NaturezaSaldo::codigo))
                .lista();
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
        return new Valores(Registro.R9999)
                .campo(() -> texto(bloco9.nomeContador()))
                .campo(() -> texto(bloco9.cpfCnpjContador()))
                .campo(() -> texto(bloco9.crcContador()))
                .campo(() -> texto(bloco9.emailContador()))
                .campo(() -> texto(bloco9.telefoneContador()))
                .campo(() -> quantidadeLinhas)
                .lista();
    }

    static IdentificacaoPessoaFisica identificacao(List<String> valores) {
        if (!NOME_ESC.equals(valores.get(0))) {
            throw new LcdprException("0000.NOME_ESC deve ser LCDPR");
        }
        return IdentificacaoPessoaFisica.builder()
                .codigoVersao(lerCampo(Registro.R0000, valores, 1, Formatos::lerTexto))
                .cpf(lerCampo(Registro.R0000, valores, 2, Formatos::lerTexto))
                .nome(lerCampo(Registro.R0000, valores, 3, Formatos::lerTexto))
                .indicadorInicioPeriodo(lerCampo(Registro.R0000, valores, 4,
                        campo -> lerEnum(campo, IndicadorInicioPeriodo::fromCodigo)))
                .situacaoEspecial(lerCampo(Registro.R0000, valores, 5,
                        campo -> lerEnum(campo, SituacaoEspecial::fromCodigo)))
                .dataSituacaoEspecial(lerCampo(Registro.R0000, valores, 6, Formatos::lerData))
                .dataInicial(lerCampo(Registro.R0000, valores, 7, Formatos::lerData))
                .dataFinal(lerCampo(Registro.R0000, valores, 8, Formatos::lerData))
                .build();
    }

    static ParametrosTributacao parametrosTributacao(List<String> valores) {
        return ParametrosTributacao.builder()
                .formaApuracao(lerCampo(Registro.R0010, valores, 0,
                        campo -> lerEnum(campo, FormaApuracao::fromCodigo)))
                .build();
    }

    static DadosCadastrais dadosCadastrais(List<String> valores) {
        return DadosCadastrais.builder()
                .endereco(lerCampo(Registro.R0030, valores, 0, Formatos::lerTexto))
                .numero(lerCampo(Registro.R0030, valores, 1, Formatos::lerTexto))
                .complemento(lerCampo(Registro.R0030, valores, 2, Formatos::lerTexto))
                .bairro(lerCampo(Registro.R0030, valores, 3, Formatos::lerTexto))
                .uf(lerCampo(Registro.R0030, valores, 4, Formatos::lerTexto))
                .codigoMunicipio(lerCampo(Registro.R0030, valores, 5, Formatos::lerTexto))
                .cep(lerCampo(Registro.R0030, valores, 6, Formatos::lerTexto))
                .telefone(lerCampo(Registro.R0030, valores, 7, Formatos::lerTexto))
                .email(lerCampo(Registro.R0030, valores, 8, Formatos::lerTexto))
                .build();
    }

    static ImovelRural imovelRural(List<String> valores) {
        return ImovelRural.builder()
                .codigoImovel(lerCampo(Registro.R0040, valores, 0, Formatos::lerTexto))
                .pais(lerCampo(Registro.R0040, valores, 1, Formatos::lerTexto))
                .moeda(lerCampo(Registro.R0040, valores, 2, Formatos::lerTexto))
                .cadastroItr(lerCampo(Registro.R0040, valores, 3, Formatos::lerTexto))
                .caepf(lerCampo(Registro.R0040, valores, 4, Formatos::lerTexto))
                .inscricaoEstadual(lerCampo(Registro.R0040, valores, 5, Formatos::lerTexto))
                .nome(lerCampo(Registro.R0040, valores, 6, Formatos::lerTexto))
                .endereco(lerCampo(Registro.R0040, valores, 7, Formatos::lerTexto))
                .numero(lerCampo(Registro.R0040, valores, 8, Formatos::lerTexto))
                .complemento(lerCampo(Registro.R0040, valores, 9, Formatos::lerTexto))
                .bairro(lerCampo(Registro.R0040, valores, 10, Formatos::lerTexto))
                .uf(lerCampo(Registro.R0040, valores, 11, Formatos::lerTexto))
                .codigoMunicipio(lerCampo(Registro.R0040, valores, 12, Formatos::lerTexto))
                .cep(lerCampo(Registro.R0040, valores, 13, Formatos::lerTexto))
                .tipoExploracao(lerCampo(Registro.R0040, valores, 14,
                        campo -> lerEnum(campo, TipoExploracao::fromCodigo)))
                .participacao(lerCampo(Registro.R0040, valores, 15, Formatos::lerPercentual))
                .build();
    }

    static Terceiro terceiro(List<String> valores) {
        return Terceiro.builder()
                .codigoImovel(lerCampo(Registro.R0045, valores, 0, Formatos::lerTexto))
                .tipoContraparte(lerCampo(Registro.R0045, valores, 1,
                        campo -> lerEnum(campo, TipoContraparte::fromCodigo)))
                .cpfCnpj(lerCampo(Registro.R0045, valores, 2, Formatos::lerTexto))
                .nome(lerCampo(Registro.R0045, valores, 3, Formatos::lerTexto))
                .participacao(lerCampo(Registro.R0045, valores, 4, Formatos::lerPercentual))
                .build();
    }

    static ContaBancaria contaBancaria(List<String> valores) {
        return ContaBancaria.builder()
                .codigoConta(lerCampo(Registro.R0050, valores, 0, Formatos::lerTexto))
                .pais(lerCampo(Registro.R0050, valores, 1, Formatos::lerTexto))
                .banco(lerCampo(Registro.R0050, valores, 2, Formatos::lerTexto))
                .nomeBanco(lerCampo(Registro.R0050, valores, 3, Formatos::lerTexto))
                .agencia(lerCampo(Registro.R0050, valores, 4, Formatos::lerTexto))
                .numeroConta(lerCampo(Registro.R0050, valores, 5, Formatos::lerTexto))
                .build();
    }

    static Lancamento lancamento(List<String> valores) {
        return Lancamento.builder()
                .data(lerCampo(Registro.RQ100, valores, 0, Formatos::lerData))
                .codigoImovel(lerCampo(Registro.RQ100, valores, 1, Formatos::lerTexto))
                .codigoConta(lerCampo(Registro.RQ100, valores, 2, Formatos::lerTexto))
                .numeroDocumento(lerCampo(Registro.RQ100, valores, 3, Formatos::lerTexto))
                .tipoDocumento(lerCampo(Registro.RQ100, valores, 4,
                        campo -> lerEnum(campo, TipoDocumento::fromCodigo)))
                .historico(lerCampo(Registro.RQ100, valores, 5, Formatos::lerTexto))
                .cpfCnpjParticipante(lerCampo(Registro.RQ100, valores, 6, Formatos::lerTexto))
                .tipoLancamento(lerCampo(Registro.RQ100, valores, 7,
                        campo -> lerEnum(campo, TipoLancamento::fromCodigo)))
                .valorEntrada(lerCampo(Registro.RQ100, valores, 8, Formatos::lerValor))
                .valorSaida(lerCampo(Registro.RQ100, valores, 9, Formatos::lerValor))
                .saldoFinal(lerCampo(Registro.RQ100, valores, 10, Formatos::lerValor))
                .naturezaSaldoFinal(lerCampo(Registro.RQ100, valores, 11,
                        campo -> lerEnum(campo, NaturezaSaldo::fromCodigo)))
                .build();
    }

    static ResumoMensal resumoMensal(List<String> valores) {
        return ResumoMensal.builder()
                .mes(lerCampo(Registro.RQ200, valores, 0, Formatos::lerMes))
                .valorEntrada(lerCampo(Registro.RQ200, valores, 1, Formatos::lerValor))
                .valorSaida(lerCampo(Registro.RQ200, valores, 2, Formatos::lerValor))
                .saldoFinal(lerCampo(Registro.RQ200, valores, 3, Formatos::lerValor))
                .naturezaSaldoFinal(lerCampo(Registro.RQ200, valores, 4,
                        campo -> lerEnum(campo, NaturezaSaldo::fromCodigo)))
                .build();
    }

    static EncerramentoArquivo encerramento(List<String> valores) {
        return EncerramentoArquivo.builder()
                .nomeContador(lerCampo(Registro.R9999, valores, 0, Formatos::lerTexto))
                .cpfCnpjContador(lerCampo(Registro.R9999, valores, 1, Formatos::lerTexto))
                .crcContador(lerCampo(Registro.R9999, valores, 2, Formatos::lerTexto))
                .emailContador(lerCampo(Registro.R9999, valores, 3, Formatos::lerTexto))
                .telefoneContador(lerCampo(Registro.R9999, valores, 4, Formatos::lerTexto))
                .quantidadeLinhas(lerCampo(Registro.R9999, valores, 5, RegistroCodec::lerQuantidadeLinhas))
                .build();
    }

    private static Long lerQuantidadeLinhas(String campo) {
        if (campo.isEmpty()) {
            return null;
        }
        if (!campo.matches("[0-9]+")) {
            throw new LcdprException("deve conter só dígitos");
        }
        try {
            return Long.parseLong(campo);
        } catch (NumberFormatException e) {
            throw new LcdprException("fora do intervalo numérico", e);
        }
    }

    private static <T> T lerCampo(Registro registro, List<String> valores, int indice,
            Function<String, T> interpretacao) {
        try {
            return interpretacao.apply(valores.get(indice));
        } catch (LcdprException e) {
            throw erro(registro, indice, e);
        }
    }

    private static ErroCampo erro(Registro registro, int indice, LcdprException causa) {
        Campo campo = registro.campos().get(indice);
        return new ErroCampo(campo.nome(), registro.codigo() + "." + campo.nome() + ": " + causa.getMessage(), causa);
    }

    /** Erro de formato de um campo; o campo indica o nome do leiaute para o validador. */
    static final class ErroCampo extends LcdprException {

        private static final long serialVersionUID = 1L;

        private final String campo;

        ErroCampo(String campo, String mensagem, Throwable causa) {
            super(mensagem, causa);
            this.campo = campo;
        }

        /** Nome do campo no leiaute, como "VL_ENTRADA". */
        String campo() {
            return campo;
        }
    }

    /** Lista de valores formatados de um registro, preenchida campo a campo na ordem do leiaute. */
    private static final class Valores {

        private final Registro registro;
        private final List<String> lista = new ArrayList<>();

        Valores(Registro registro) {
            this.registro = registro;
        }

        Valores campo(Supplier<String> formatacao) {
            try {
                lista.add(formatacao.get());
            } catch (LcdprException e) {
                throw erro(registro, lista.size(), e);
            }
            return this;
        }

        List<String> lista() {
            return lista;
        }
    }
}
