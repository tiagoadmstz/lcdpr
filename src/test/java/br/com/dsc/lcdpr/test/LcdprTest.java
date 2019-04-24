/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.dsc.lcdpr.test;

import br.com.dsc.lcdpr.blocos.AberturaIdentificacao;
import br.com.dsc.lcdpr.blocos.DemonstrativoLivroCaixa;
import br.com.dsc.lcdpr.blocos.EncerramentoArquivo;
import br.com.dsc.lcdpr.components.ContaBancaria;
import br.com.dsc.lcdpr.components.Contribuinte;
import br.com.dsc.lcdpr.components.DemoLivroCaixa;
import br.com.dsc.lcdpr.components.ExploracaoMedianteContrato;
import br.com.dsc.lcdpr.components.IdentificacaoPessoaFisica;
import br.com.dsc.lcdpr.components.ImovelRural;
import br.com.dsc.lcdpr.components.ParametrosTributacao;
import br.com.dsc.lcdpr.components.ResumoDemoLivroCaixa;
import br.com.dsc.lcdpr.enumerated.FORMA_APURACAO;
import br.com.dsc.lcdpr.enumerated.INICIO_PERIODO;
import br.com.dsc.lcdpr.enumerated.SITUACAO_ESPECIAL;
import br.com.dsc.lcdpr.enumerated.TIPO_CONTRAPARTE;
import br.com.dsc.lcdpr.enumerated.TIPO_DOCUMENTO;
import br.com.dsc.lcdpr.enumerated.TIPO_EXPLORACAO;
import br.com.dsc.lcdpr.enumerated.TIPO_LANCAMENTO;
import br.com.dsc.lcdpr.lcdpr.LCDPR;
import java.time.LocalDate;
import java.util.Arrays;
import org.junit.Test;

/**
 *
 * @author Tiago
 */
public class LcdprTest {

    @Test
    public void preenchimentoTest() {
        LCDPR lcdpr = generatedLCDPR();
        System.err.println(lcdpr.generatedPipeText());
    }

    public static LCDPR generatedLCDPR() {
        LCDPR lcdpr = new LCDPR();
        lcdpr.setBloco0(generatedBloco0());
        lcdpr.setBlocoq(generatedBlocoQ());
        lcdpr.setBloco9(generatedBloco9());
        return lcdpr;
    }

    public static AberturaIdentificacao generatedBloco0() {
        //0000|LCDPR|0001|11111111191|JOSÉ DA SILVA|0|0||01012018|31122018
        IdentificacaoPessoaFisica Ipf = new IdentificacaoPessoaFisica();
        Ipf.setCpf(11111111191l);
        Ipf.setNome("JOSÉ DA SILVA");
        Ipf.setInd_sit_ini_per(INICIO_PERIODO.REGULAR);
        Ipf.setSituacao_especial(SITUACAO_ESPECIAL.NORMAL);
        Ipf.setDataInicioPeriodo(LocalDate.of(2018, 1, 1));
        Ipf.setDataFinalPeriodo(LocalDate.of(2018, 12, 31));
        //0010|11112222333344445555666677778888999900001|1
        ParametrosTributacao parametrosTributacao = new ParametrosTributacao();
        parametrosTributacao.setHashIrrfAnterior("11112222333344445555666677778888999900001");
        parametrosTributacao.setForma_apuracao(FORMA_APURACAO.LIVRO_CAIXA);
        //0030|RUA TESTE|1234|BLOCO Z SALA 301|BAIRRO LCDPR|DF|5300108|71000000|6133333333|testeLCDPR@LCDPR.com.br
        Contribuinte contribuinte = new Contribuinte();
        contribuinte.setEndereco("RUA TESTE");
        contribuinte.setNumero("1234");
        contribuinte.setComplemento("BLOCO Z SALA 301");
        contribuinte.setBairro("BAIRRO LCDPR");
        contribuinte.setUf("DF");
        contribuinte.setCodigo_municipio("5300108");
        contribuinte.setCep("71000000");
        contribuinte.setNumero_telefone(6133333333l);
        contribuinte.setEmail("testeLCDPR@LCDPR.com.br");
        //0040|001|BRA|BRL|12345678|123456789012|12345678901234|Fazenda Tudo Certo|Rodovia BR 999, Km 3000|||Distrito do Meio|DF|5300108|71000000|2|0500
        ImovelRural imovelRural = new ImovelRural();
        imovelRural.setCod_imovel("001");
        imovelRural.setMoeda("BRL");
        imovelRural.setCad_ITR("12345678");
        imovelRural.setAtividade_economica_pf("123456789012");
        imovelRural.setInscricao_estadual("12345678901234");
        imovelRural.setNome_imovel("Fazenda Tudo Certo");
        imovelRural.setEndereco("Rodovia BR 999, Km 3000");
        imovelRural.setBairro("Distrito do Meio");
        imovelRural.setUf("DF");
        imovelRural.setCod_municipio("5300108");
        imovelRural.setCep("71000000");
        imovelRural.setTipo_exploracao(TIPO_EXPLORACAO.CONDOMINIO);
        imovelRural.setParticipacao("0500");
        //0045|002|3|12345678912|JOÃO DE SOUSA|052
        ExploracaoMedianteContrato exploracao = new ExploracaoMedianteContrato();
        exploracao.setCod_imovel("002");
        exploracao.setTipo_contraparte(TIPO_CONTRAPARTE.PARCEIRO);
        exploracao.setCpf_contraparte("12345678912");
        exploracao.setNome_contraparte("JOÃO DE SOUSA");
        exploracao.setPercentual_contraparte("052");
        //0050|001|BRA|999|Banco LCDPR|1234|0000000123456789
        ContaBancaria contaBancaria = new ContaBancaria();
        contaBancaria.setCod_conta("001");
        contaBancaria.setPais_cta("BRA");
        contaBancaria.setBanco("999");
        contaBancaria.setNome_banco("Banco LCDPR");
        contaBancaria.setAgencia("1234");
        contaBancaria.setNumero_conta("0000000123456789");

        AberturaIdentificacao abertura = new AberturaIdentificacao();
        abertura.setIdentificacao_pessoa_fisica(Ipf);
        abertura.setParametros_tributacao(parametrosTributacao);
        abertura.setDados_cadastrais_contribuinte(contribuinte);
        abertura.setImoveis_rurais(Arrays.asList(imovelRural));
        abertura.setExploracao_imoveis_rurais(Arrays.asList(exploracao));
        abertura.setContas_bancarias(Arrays.asList(contaBancaria));
        return abertura;
    }

    public static DemonstrativoLivroCaixa generatedBlocoQ() {
        //Q100|02012018|001|001|321|1|Venda de 100 sacas de milho|12345678000112|1|1000000|000|1100000|P
        DemoLivroCaixa livroCaixa = new DemoLivroCaixa();
        livroCaixa.setData(LocalDate.of(2018, 1, 2));
        livroCaixa.setCod_imovel("001");
        livroCaixa.setCod_conta("001");
        livroCaixa.setNumero_documento("321");
        livroCaixa.setTipo_documento(TIPO_DOCUMENTO.NOTA_FISCAL);
        livroCaixa.setHistorico("Venda de 100 sacas de milho");
        livroCaixa.setId_participante("12345678000112");
        livroCaixa.setTipo_lancamento(TIPO_LANCAMENTO.RECEITA_ATV_RURAL);
        livroCaixa.setValor_entrada("1000000");
        livroCaixa.setValor_saida("000");
        livroCaixa.setSaldo_final("1100000");
        //Q200|BRA|012018||10000000| 8500000| 1500000|P
        ResumoDemoLivroCaixa resumo = new ResumoDemoLivroCaixa();
        resumo.setPais("BRA");
        resumo.setMes("012018");
        resumo.setValor_entrada("10000000");
        resumo.setValor_saida("8500000");
        resumo.setSaldo_final("1500000");

        DemonstrativoLivroCaixa demonstrativo = new DemonstrativoLivroCaixa();
        demonstrativo.setDemonstrativo_livro_caixa(Arrays.asList(livroCaixa));
        demonstrativo.setResumo_demonstrativo_livro_caixa(Arrays.asList(resumo));
        return demonstrativo;
    }

    public static EncerramentoArquivo generatedBloco9() {
        //9999|8007
        EncerramentoArquivo encerramento = new EncerramentoArquivo();
        encerramento.setQuantidade_linhas(8007);
        return encerramento;
    }
}
