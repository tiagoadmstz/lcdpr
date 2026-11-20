package io.github.tiagoadmstz.lcdpr.example.pure;

import java.math.BigDecimal;
import java.time.LocalDate;
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
 * Exemplo prático de uso do módulo {@code lcdpr-core} em Java 17+.
 * Demonstra a criação de registros, cálculo de saldos, validação e serialização pipe e JSON.
 */
public final class PureJavaExample {

    private PureJavaExample() {
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

        System.out.println("=== LCDPR 2.0 - Exemplo de Sucesso (Java 17) ===");
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
                .nome("Produtor Rural Exemplo")
                .indicadorInicioPeriodo(IndicadorInicioPeriodo.REGULAR)
                .situacaoEspecial(SituacaoEspecial.NORMAL)
                .dataInicial(LocalDate.of(2026, 1, 1))
                .dataFinal(LocalDate.of(2026, 12, 31))
                .build();

        ParametrosTributacao parametros = ParametrosTributacao.builder()
                .formaApuracao(FormaApuracao.LIVRO_CAIXA)
                .build();

        DadosCadastrais dadosCadastrais = DadosCadastrais.builder()
                .endereco("Avenida Brasil")
                .numero("1500")
                .bairro("Centro")
                .uf("SP")
                .codigoMunicipio("3550308")
                .cep("01000000")
                .email("contato@fazendaexemplo.com.br")
                .build();

        ImovelRural imovel = ImovelRural.builder()
                .codigoImovel("001")
                .nome("Fazenda Esperança")
                .pais("BR")
                .moeda("BRL")
                .cadastroItr("12345678")
                .caepf("29311861000184")
                .endereco("Estrada Municipal KM 10")
                .bairro("Zona Rural")
                .uf("SP")
                .codigoMunicipio("3550308")
                .cep("01000000")
                .tipoExploracao(TipoExploracao.INDIVIDUAL)
                .participacao(new BigDecimal("100.00"))
                .build();

        ContaBancaria conta = ContaBancaria.builder()
                .codigoConta("001")
                .pais("BRA")
                .banco("001")
                .nomeBanco("BANCO DO BRASIL")
                .agencia("1234")
                .numeroConta("0000000001234567")
                .build();

        Lancamento lancamento1 = Lancamento.builder()
                .data(LocalDate.of(2026, 3, 15))
                .codigoImovel("001")
                .codigoConta("001")
                .numeroDocumento("NF-1001")
                .tipoDocumento(TipoDocumento.NOTA_FISCAL)
                .historico("Venda de soja colheita de verão")
                .cpfCnpjParticipante("11144477735")
                .tipoLancamento(TipoLancamento.RECEITA)
                .valorEntrada(new BigDecimal("150000.00"))
                .valorSaida(BigDecimal.ZERO)
                .build();

        Lancamento lancamento2 = Lancamento.builder()
                .data(LocalDate.of(2026, 4, 10))
                .codigoImovel("001")
                .codigoConta("001")
                .numeroDocumento("FAT-5002")
                .tipoDocumento(TipoDocumento.FATURA)
                .historico("Aquisição de insumos agrícolas e adubos")
                .cpfCnpjParticipante("11222333000181")
                .tipoLancamento(TipoLancamento.DESPESA_CUSTEIO_INVESTIMENTO)
                .valorEntrada(BigDecimal.ZERO)
                .valorSaida(new BigDecimal("45000.00"))
                .build();

        EncerramentoArquivo encerramento = EncerramentoArquivo.builder()
                .nomeContador("Contador Responsável")
                .cpfCnpjContador("11144477735")
                .crcContador("SP123456O")
                .emailContador("contador@fiscal.com.br")
                .build();

        AberturaIdentificacao bloco0 = AberturaIdentificacao.builder()
                .identificacao(identificacao)
                .parametrosTributacao(parametros)
                .dadosCadastrais(dadosCadastrais)
                .imoveisRurais(List.of(imovel))
                .contasBancarias(List.of(conta))
                .build();

        DemonstrativoResultado blocoQ = DemonstrativoResultado.builder()
                .lancamentos(List.of(lancamento1, lancamento2))
                .build();

        return Lcdpr.builder()
                .bloco0(bloco0)
                .blocoQ(blocoQ)
                .bloco9(encerramento)
                .build();
    }
}
