package br.com.dsc.lcdpr.test;

import br.com.dsc.lcdpr.blocos.AberturaIdentificacao;
import br.com.dsc.lcdpr.blocos.DemonstrativoLivroCaixa;
import br.com.dsc.lcdpr.blocos.EncerramentoArquivo;
import br.com.dsc.lcdpr.components.CadastroTerceiro;
import br.com.dsc.lcdpr.components.ContaBancaria;
import br.com.dsc.lcdpr.components.Contribuinte;
import br.com.dsc.lcdpr.components.DemoLivroCaixa;
import br.com.dsc.lcdpr.components.IdentificacaoPessoaFisica;
import br.com.dsc.lcdpr.components.ImovelRural;
import br.com.dsc.lcdpr.components.ParametrosTributacao;
import br.com.dsc.lcdpr.components.ResumoDemoLivroCaixa;
import br.com.dsc.lcdpr.enumerated.TIPO_CONTRAPARTE;
import br.com.dsc.lcdpr.enumerated.TIPO_DOCUMENTO;
import br.com.dsc.lcdpr.enumerated.TIPO_EXPLORACAO;
import br.com.dsc.lcdpr.enumerated.TIPO_LANCAMENTO;
import br.com.dsc.lcdpr.lcdpr.Lcdpr;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;

import static java.time.format.DateTimeFormatter.ofPattern;

public abstract class DummyData {

    public static Lcdpr generateLcdpr() {
        return Lcdpr.builder()
                .bloco0(generateBloco0())
                .blocoQ(generateBlocoQ())
                .bloco9(generateBloco9())
                .build();
    }

    public static DocumentoLcdpr generateDocumentoLcdpr() {
        DocumentoLcdpr documentoLcdpr = new DocumentoLcdpr();
        documentoLcdpr.setId(1L);
        documentoLcdpr.setBloco0(generateBloco0());
        documentoLcdpr.setBlocoQ(generateBlocoQ());
        documentoLcdpr.setBloco9(generateBloco9());
        return documentoLcdpr;
    }

    public static AberturaIdentificacao generateBloco0() {
        //0000|LCDPR|0013|11111111191|JOSÉ DA SILVA|0|0||01012019|31122019
        IdentificacaoPessoaFisica identificacaoPessoaFisica = IdentificacaoPessoaFisica.builder()
                .cpf("11111111191")
                .nome("JOSÉ DA SILVA")
                .dataInicioPeriodo(LocalDate.parse("01/01/2019", ofPattern("dd/MM/yyyy")))
                .dataFinalPeriodo(LocalDate.parse("01/01/2019", ofPattern("dd/MM/yyyy")))
                .build();
        //0010|1
        ParametrosTributacao parametrosTributacao = ParametrosTributacao.builder().build();
        //0030|RUA TESTE|1234|BLOCO Z SALA 301|BAIRRO LCDPR|DF|5300108|71000000|6133333333|testeLCDPR@LCDPR.com.br
        Contribuinte contribuinte = Contribuinte.builder()
                .endereco("RUA TESTE")
                .numero("1234")
                .complemento("BLOCO Z SALA 301")
                .bairro("BAIRRO LCDPR")
                .uf("DF")
                .codigoMunicipio("5300108")
                .cep("71000000")
                .numeroTelefone(6133333333L)
                .email("testeLCDPR@LCDPR.com.br")
                .build();
        //0040|001|BR|BRL|12345678|12345678901234|12345678901234|Fazenda Tudo Certo|Rodovia BR 999, Km 3000|||Distrito do Meio|DF|5300108|71000000|2|05000
        ImovelRural imovelRural = ImovelRural.builder()
                .codigoImovel("001")
                .cadastroImpostoTerritorialRural("12345678")
                .cadastroAtividadeEconomicaPessoaFisica("12345678901234")
                .inscricaoEstadual("12345678901234")
                .nomeImovel("Fazenda Tudo Certo")
                .endereco("Rodovia BR 999, Km 3000")
                .bairro("Distrito do Meio")
                .uf("DF")
                .codigoMunicipio("5300108")
                .cep("71000000")
                .tipoExploracao(TIPO_EXPLORACAO.CONDOMINIO)
                .participacao("05000")
                .cadastroTerceiros(Arrays.asList(
                        //0045|002|3|12345678912|JOÃO DE SOUSA|00520
                        CadastroTerceiro.builder()
                                .codigoImovel("002")
                                .tipoContraparte(TIPO_CONTRAPARTE.PARCEIRO)
                                .cpfCnpjContraparte("12345678912")
                                .nomeContraparte("JOÃO DE SOUSA")
                                .percentualContraparte("00520")
                                .build()
                ))
                .build();

        //0050|001|BR|999|Banco LCDPR|1234|0000000123456789
        ContaBancaria contaBancaria = ContaBancaria.builder()
                .codigoConta("001")
                .banco("999")
                .nomeBanco("Banco LCDPR")
                .agencia("1234")
                .numeroConta("0000000123456789")
                .build();

        return AberturaIdentificacao.builder()
                .identificacaoPessoaFisica(identificacaoPessoaFisica)
                .parametrosTributacao(parametrosTributacao)
                .dadosCadastraisContribuinte(contribuinte)
                .build()
                .addImovelRural(imovelRural)
                .addImovelRural(imovelRural)
                .addContaBancaria(contaBancaria);
    }

    public static DemonstrativoLivroCaixa generateBlocoQ() {
        //Q100|02012019|001|001|2|3|Venda de 100 sacas de milho|12345678912|1|1000000|0|1100000|P
        DemoLivroCaixa livroCaixa = DemoLivroCaixa.builder()
                .data(LocalDate.parse("02/01/2019", ofPattern("dd/MM/yyyy")))
                .codigoImovel("001")
                .codigoConta("001")
                .numeroDocumento("2")
                .tipoDocumento(TIPO_DOCUMENTO.FATURA)
                .historico("Venda de 100 sacas de milho")
                .cpfCnpjParticipante("12345678912")
                .tipoLancamento(TIPO_LANCAMENTO.RECEITA_PRODUTOS_ENTREGUES)
                .valorEntrada(new BigDecimal(10000.00))
                .saldoFinal(new BigDecimal(11000.00))
                .build();
        //Q100|02012019|001|001|3|1|Pagamento de sementes e outros insumos|12345678000121|2|0|500000|600000|P
        DemoLivroCaixa livroCaixa2 = DemoLivroCaixa.builder()
                .data(LocalDate.parse("02/01/2019", ofPattern("dd/MM/yyyy")))
                .codigoImovel("001")
                .codigoConta("001")
                .numeroDocumento("3")
                .tipoDocumento(TIPO_DOCUMENTO.NOTA_FISCAL)
                .historico("Pagamento de sementes e outros insumos")
                .cpfCnpjParticipante("12345678000121")
                .tipoLancamento(TIPO_LANCAMENTO.DESPESAS_CUSTEIO_INVESTIMENTOS)
                .valorSaida(new BigDecimal(5000.00))
                .saldoFinal(new BigDecimal(6000.00))
                .build();

        //Q200|012019|10000000| 8500000| 1500000|P
        ResumoDemoLivroCaixa resumo = ResumoDemoLivroCaixa.builder()
                .mes("012019")
                .valorEntrada(new BigDecimal(100000.00))
                .valorSaida(new BigDecimal(85000.00))
                .saldoFinal(new BigDecimal(15000.00))
                .build();
        //Q200|022019|7000000| 6000000| 2500000|P
        ResumoDemoLivroCaixa resumo2 = ResumoDemoLivroCaixa.builder()
                .mes("022019")
                .valorEntrada(new BigDecimal(70000.00))
                .valorSaida(new BigDecimal(60000.00))
                .saldoFinal(new BigDecimal(25000.00))
                .build();

        return DemonstrativoLivroCaixa.builder().build()
                .addDemoLivroCaixa(livroCaixa)
                .addDemoLivroCaixa(livroCaixa2)
                .addResumoDemonstrativoLivroCaixa(resumo)
                .addResumoDemonstrativoLivroCaixa(resumo2);
    }

    public static EncerramentoArquivo generateBloco9() {
        //9999|JOSE DE SOUZA|12345678912|AL123456O| testeLCDPR@LCDPR.com.br|6133333333|8007
        return EncerramentoArquivo.builder()
                .nomeContador("JOSE DE SOUZA")
                .cpfCnpjContador("12345678912")
                .numeroConselhoRegionalContabilidade("AL123456O")
                .emailContador("testeLCDPR@LCDPR.com.br")
                .telefoneContador(6133333333L)
                .quantidadeRegistrosArquivo(8007)
                .build();
    }

}
