package io.github.tiagoadmstz.lcdpr;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;

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
import io.github.tiagoadmstz.lcdpr.tabela.FormaApuracao;
import io.github.tiagoadmstz.lcdpr.tabela.IndicadorInicioPeriodo;
import io.github.tiagoadmstz.lcdpr.tabela.NaturezaSaldo;
import io.github.tiagoadmstz.lcdpr.tabela.SituacaoEspecial;
import io.github.tiagoadmstz.lcdpr.tabela.TipoContraparte;
import io.github.tiagoadmstz.lcdpr.tabela.TipoDocumento;
import io.github.tiagoadmstz.lcdpr.tabela.TipoExploracao;
import io.github.tiagoadmstz.lcdpr.tabela.TipoLancamento;

final class Amostras {

    private Amostras() {
    }

    static Lcdpr valido() {
        AberturaIdentificacao bloco0 = AberturaIdentificacao.builder()
                .identificacao(IdentificacaoPessoaFisica.builder()
                        .codigoVersao("0013")
                        .cpf("11144477735")
                        .nome("JOSÉ DA SILVA")
                        .indicadorInicioPeriodo(IndicadorInicioPeriodo.REGULAR)
                        .situacaoEspecial(SituacaoEspecial.NORMAL)
                        .dataInicial(LocalDate.of(2019, 1, 1))
                        .dataFinal(LocalDate.of(2019, 12, 31))
                        .build())
                .parametrosTributacao(ParametrosTributacao.builder()
                        .formaApuracao(FormaApuracao.LIVRO_CAIXA)
                        .build())
                .dadosCadastrais(DadosCadastrais.builder()
                        .endereco("RUA TESTE")
                        .numero("1234")
                        .complemento("BLOCO Z SALA 301")
                        .bairro("BAIRRO LCDPR")
                        .uf("DF")
                        .codigoMunicipio("5300108")
                        .cep("71000000")
                        .telefone("6133333333")
                        .email("testeLCDPR@LCDPR.com.br")
                        .build())
                .addImovelRural(ImovelRural.builder()
                        .codigoImovel("001")
                        .pais("BR")
                        .moeda("BRL")
                        .cadastroItr("12345678")
                        .caepf("12345678901234")
                        .inscricaoEstadual("12345678901234")
                        .nome("Fazenda Tudo Certo")
                        .endereco("Rodovia BR 999, Km 3000")
                        .bairro("Distrito do Meio")
                        .uf("DF")
                        .codigoMunicipio("5300108")
                        .cep("71000000")
                        .tipoExploracao(TipoExploracao.CONDOMINIO)
                        .participacao(new BigDecimal("50.00"))
                        .addTerceiro(Terceiro.builder()
                                .codigoImovel("001")
                                .tipoContraparte(TipoContraparte.CONDOMINO)
                                .cpfCnpj("12345678909")
                                .nome("JOÃO DE SOUSA")
                                .participacao(new BigDecimal("50.00"))
                                .build())
                        .build())
                .addImovelRural(ImovelRural.builder()
                        .codigoImovel("002")
                        .pais("BR")
                        .moeda("BRL")
                        .cadastroItr("87654321")
                        .caepf("12345678901234")
                        .nome("Sítio Boa Vista")
                        .endereco("Estrada Vicinal 10")
                        .complemento("KM 2")
                        .bairro("Zona Rural")
                        .uf("DF")
                        .codigoMunicipio("5300108")
                        .cep("71000000")
                        .tipoExploracao(TipoExploracao.INDIVIDUAL)
                        .participacao(new BigDecimal("100.00"))
                        .build())
                .addContaBancaria(ContaBancaria.builder()
                        .codigoConta("001")
                        .pais("BR")
                        .banco("999")
                        .nomeBanco("Banco LCDPR")
                        .agencia("1234")
                        .numeroConta("0000000123456789")
                        .build())
                .build();

        DemonstrativoResultado blocoQ = DemonstrativoResultado.builder()
                .addLancamento(Lancamento.builder()
                        .data(LocalDate.of(2019, 1, 2))
                        .codigoImovel("001")
                        .codigoConta("001")
                        .numeroDocumento("2")
                        .tipoDocumento(TipoDocumento.RECIBO)
                        .historico("Venda de 100 sacas de milho")
                        .cpfCnpjParticipante("12345678909")
                        .tipoLancamento(TipoLancamento.RECEITA)
                        .valorEntrada(new BigDecimal("10000.00"))
                        .valorSaida(new BigDecimal("0.00"))
                        .saldoFinal(new BigDecimal("10000.00"))
                        .naturezaSaldoFinal(NaturezaSaldo.POSITIVO)
                        .build())
                .addLancamento(Lancamento.builder()
                        .data(LocalDate.of(2019, 1, 15))
                        .codigoImovel("001")
                        .codigoConta("001")
                        .numeroDocumento("3")
                        .tipoDocumento(TipoDocumento.NOTA_FISCAL)
                        .historico("Pagamento de sementes e outros insumos")
                        .cpfCnpjParticipante("11222333000181")
                        .tipoLancamento(TipoLancamento.DESPESA_CUSTEIO_INVESTIMENTO)
                        .valorEntrada(new BigDecimal("0.00"))
                        .valorSaida(new BigDecimal("5000.00"))
                        .saldoFinal(new BigDecimal("5000.00"))
                        .naturezaSaldoFinal(NaturezaSaldo.POSITIVO)
                        .build())
                .addLancamento(Lancamento.builder()
                        .data(LocalDate.of(2019, 2, 10))
                        .codigoImovel("002")
                        .codigoConta("000")
                        .numeroDocumento("4")
                        .tipoDocumento(TipoDocumento.RECIBO)
                        .historico("Pagamento de diarista")
                        .cpfCnpjParticipante("12345678909")
                        .tipoLancamento(TipoLancamento.DESPESA_CUSTEIO_INVESTIMENTO)
                        .valorEntrada(new BigDecimal("0.00"))
                        .valorSaida(new BigDecimal("8000.00"))
                        .saldoFinal(new BigDecimal("3000.00"))
                        .naturezaSaldoFinal(NaturezaSaldo.NEGATIVO)
                        .build())
                .addResumoMensal(ResumoMensal.builder()
                        .mes(YearMonth.of(2019, 1))
                        .valorEntrada(new BigDecimal("10000.00"))
                        .valorSaida(new BigDecimal("5000.00"))
                        .saldoFinal(new BigDecimal("5000.00"))
                        .naturezaSaldoFinal(NaturezaSaldo.POSITIVO)
                        .build())
                .addResumoMensal(ResumoMensal.builder()
                        .mes(YearMonth.of(2019, 2))
                        .valorEntrada(new BigDecimal("0.00"))
                        .valorSaida(new BigDecimal("8000.00"))
                        .saldoFinal(new BigDecimal("3000.00"))
                        .naturezaSaldoFinal(NaturezaSaldo.NEGATIVO)
                        .build())
                .build();

        EncerramentoArquivo bloco9 = EncerramentoArquivo.builder()
                .nomeContador("JOSE DE SOUZA")
                .cpfCnpjContador("12345678909")
                .crcContador("AL123456O")
                .emailContador("testeLCDPR@LCDPR.com.br")
                .telefoneContador("6133333333")
                .quantidadeLinhas(13L)
                .build();

        return Lcdpr.builder()
                .bloco0(bloco0)
                .blocoQ(blocoQ)
                .bloco9(bloco9)
                .build();
    }
}
