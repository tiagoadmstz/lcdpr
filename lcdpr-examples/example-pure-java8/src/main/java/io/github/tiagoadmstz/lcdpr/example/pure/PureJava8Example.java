package io.github.tiagoadmstz.lcdpr.example.pure;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import io.github.tiagoadmstz.lcdpr.ErroValidacao;
import io.github.tiagoadmstz.lcdpr.Lcdpr;
import io.github.tiagoadmstz.lcdpr.LcdprCalculator;
import io.github.tiagoadmstz.lcdpr.LcdprJson;
import io.github.tiagoadmstz.lcdpr.LcdprReader;
import io.github.tiagoadmstz.lcdpr.LcdprValidator;
import io.github.tiagoadmstz.lcdpr.LcdprWriter;
import io.github.tiagoadmstz.lcdpr.modelo.AberturaIdentificacao;
import io.github.tiagoadmstz.lcdpr.modelo.ContaBancaria;
import io.github.tiagoadmstz.lcdpr.modelo.DadosCadastrais;
import io.github.tiagoadmstz.lcdpr.modelo.DemonstrativoResultado;
import io.github.tiagoadmstz.lcdpr.modelo.EncerramentoArquivo;
import io.github.tiagoadmstz.lcdpr.modelo.IdentificacaoPessoaFisica;
import io.github.tiagoadmstz.lcdpr.modelo.ImovelRural;
import io.github.tiagoadmstz.lcdpr.modelo.Lancamento;
import io.github.tiagoadmstz.lcdpr.modelo.ParametrosTributacao;
import io.github.tiagoadmstz.lcdpr.tabela.FormaApuracao;
import io.github.tiagoadmstz.lcdpr.tabela.IndicadorInicioPeriodo;
import io.github.tiagoadmstz.lcdpr.tabela.SituacaoEspecial;
import io.github.tiagoadmstz.lcdpr.tabela.TipoDocumento;
import io.github.tiagoadmstz.lcdpr.tabela.TipoExploracao;
import io.github.tiagoadmstz.lcdpr.tabela.TipoLancamento;

/**
 * Exemplo prático de uso do módulo {@code lcdpr-java8} em Java 8.
 * Demonstra a interoperabilidade da biblioteca em ambientes legados.
 */
public final class PureJava8Example {

    private PureJava8Example() {
    }

    public static void main(String[] args) {
        Lcdpr lcdpr = criarEscrituracaoExemplo();

        Lcdpr calculado = LcdprCalculator.recalculate(lcdpr);

        List<ErroValidacao> erros = LcdprValidator.validate(calculado, true);
        if (!erros.isEmpty()) {
            System.err.println("Erros encontrados na validação:");
            for (ErroValidacao erro : erros) {
                System.err.printf("Linha %d [%s.%s]: %s%n",
                        erro.linha(), erro.registro(), erro.campo(), erro.mensagem());
            }
            return;
        }

        String arquivoPipe = LcdprWriter.write(calculado);
        String json = LcdprJson.toJson(calculado);

        Lcdpr lidoDoTexto = LcdprReader.read(arquivoPipe);

        System.out.println("=== LCDPR 2.0 - Exemplo de Sucesso (Java 8) ===");
        System.out.printf("Produtor: %s (CPF: %s)%n",
                calculado.bloco0().identificacao().nome(),
                calculado.bloco0().identificacao().cpf());
        System.out.printf("Linhas no arquivo pipe: %d%n",
                lidoDoTexto.bloco9().quantidadeLinhas());
        System.out.printf("Tamanho do JSON: %d caracteres%n", json.length());
        System.out.printf("Total de meses apurados no Q200: %d%n",
                calculado.blocoQ().resumosMensais().size());
        System.out.println("Status da validação: OK (0 erros)");
    }

    public static Lcdpr criarEscrituracaoExemplo() {
        IdentificacaoPessoaFisica identificacao = IdentificacaoPessoaFisica.builder()
                .cpf("11144477735")
                .nome("Produtor Rural Legado")
                .indicadorInicioPeriodo(IndicadorInicioPeriodo.REGULAR)
                .situacaoEspecial(SituacaoEspecial.NORMAL)
                .dataInicial(LocalDate.of(2026, 1, 1))
                .dataFinal(LocalDate.of(2026, 12, 31))
                .build();

        ParametrosTributacao parametros = ParametrosTributacao.builder()
                .formaApuracao(FormaApuracao.LIVRO_CAIXA)
                .build();

        DadosCadastrais dadosCadastrais = DadosCadastrais.builder()
                .endereco("Rua das Flores")
                .numero("500")
                .bairro("Centro")
                .uf("MG")
                .codigoMunicipio("3106200")
                .cep("30100000")
                .email("contato@fazendavieira.com.br")
                .build();

        ImovelRural imovel = ImovelRural.builder()
                .codigoImovel("001")
                .nome("Fazenda Recanto Verde")
                .pais("BR")
                .moeda("BRL")
                .cadastroItr("87654321")
                .caepf("29311861000184")
                .endereco("Rodovia MG-010 KM 45")
                .bairro("Zona Rural")
                .uf("MG")
                .codigoMunicipio("3106200")
                .cep("30100000")
                .tipoExploracao(TipoExploracao.INDIVIDUAL)
                .participacao(new BigDecimal("100.00"))
                .build();

        ContaBancaria conta = ContaBancaria.builder()
                .codigoConta("001")
                .pais("BRA")
                .banco("341")
                .nomeBanco("ITAU UNIBANCO")
                .agencia("0500")
                .numeroConta("0000000123456789")
                .build();

        Lancamento lancamento1 = Lancamento.builder()
                .data(LocalDate.of(2026, 2, 20))
                .codigoImovel("001")
                .codigoConta("001")
                .numeroDocumento("NF-801")
                .tipoDocumento(TipoDocumento.NOTA_FISCAL)
                .historico("Receita da venda de milho")
                .cpfCnpjParticipante("11144477735")
                .tipoLancamento(TipoLancamento.RECEITA)
                .valorEntrada(new BigDecimal("80000.00"))
                .valorSaida(BigDecimal.ZERO)
                .build();

        Lancamento lancamento2 = Lancamento.builder()
                .data(LocalDate.of(2026, 5, 12))
                .codigoImovel("001")
                .codigoConta("001")
                .numeroDocumento("REC-102")
                .tipoDocumento(TipoDocumento.RECIBO)
                .historico("Manutencao de tratores e implementos")
                .cpfCnpjParticipante("11222333000181")
                .tipoLancamento(TipoLancamento.DESPESA_CUSTEIO_INVESTIMENTO)
                .valorEntrada(BigDecimal.ZERO)
                .valorSaida(new BigDecimal("12000.00"))
                .build();

        EncerramentoArquivo encerramento = EncerramentoArquivo.builder()
                .nomeContador("Contador de Minas")
                .cpfCnpjContador("11144477735")
                .crcContador("MG654321O")
                .emailContador("fiscal@contador.com.br")
                .build();

        AberturaIdentificacao bloco0 = AberturaIdentificacao.builder()
                .identificacao(identificacao)
                .parametrosTributacao(parametros)
                .dadosCadastrais(dadosCadastrais)
                .imoveisRurais(Collections.singletonList(imovel))
                .contasBancarias(Collections.singletonList(conta))
                .build();

        DemonstrativoResultado blocoQ = DemonstrativoResultado.builder()
                .lancamentos(Arrays.asList(lancamento1, lancamento2))
                .build();

        return Lcdpr.builder()
                .bloco0(bloco0)
                .blocoQ(blocoQ)
                .bloco9(encerramento)
                .build();
    }
}
