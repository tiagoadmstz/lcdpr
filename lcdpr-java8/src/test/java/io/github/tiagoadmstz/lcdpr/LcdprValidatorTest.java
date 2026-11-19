package io.github.tiagoadmstz.lcdpr;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.UnaryOperator;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import io.github.tiagoadmstz.lcdpr.modelo.AberturaIdentificacao;
import io.github.tiagoadmstz.lcdpr.modelo.ContaBancaria;
import io.github.tiagoadmstz.lcdpr.modelo.DadosCadastrais;
import io.github.tiagoadmstz.lcdpr.modelo.DemonstrativoResultado;
import io.github.tiagoadmstz.lcdpr.modelo.EncerramentoArquivo;
import io.github.tiagoadmstz.lcdpr.modelo.IdentificacaoPessoaFisica;
import io.github.tiagoadmstz.lcdpr.modelo.ImovelRural;
import io.github.tiagoadmstz.lcdpr.modelo.Lancamento;
import io.github.tiagoadmstz.lcdpr.modelo.ResumoMensal;
import io.github.tiagoadmstz.lcdpr.tabela.SituacaoEspecial;

class LcdprValidatorTest {

    @Test
    @DisplayName("Exemplo válido não tem erros")
    void shouldReturnNoErrorsForValidSample() {
        assertThat(LcdprValidator.validate(Amostras.valido())).isEmpty();
    }

    @Test
    @DisplayName("Registro nulo gera um único erro")
    void shouldReturnSingleErrorWhenLcdprIsNull() {
        assertThat(LcdprValidator.validate(null))
                .containsExactly(new ErroValidacao(0, null, null, "Arquivo LCDPR ausente"));
    }

    @Test
    @DisplayName("Sem bloco 0 devolve só o erro do bloco 0")
    void shouldReturnOnlyBlocoZeroErrorWhenBlocoZeroIsMissing() {
        Lcdpr semBloco0 = Lcdpr.builder().bloco9(Amostras.valido().bloco9()).build();

        assertThat(LcdprValidator.validate(semBloco0))
                .containsExactly(new ErroValidacao(0, null, null, "Bloco 0 obrigatório"));
    }

    @Test
    @DisplayName("Sem registro 0000 reporta o erro com linha 0")
    void shouldReportMissingRegistro0000WithLineZero() {
        Lcdpr semIdentificacao = comBloco0(bloco0Valido().toBuilder().identificacao(null).build());

        assertThat(LcdprValidator.validate(semIdentificacao))
                .contains(new ErroValidacao(0, "0000", null, "Registro 0000 obrigatório"));
    }

    @Test
    @DisplayName("Sem contas bancárias reporta erro no registro 0050")
    void shouldReportMissingContasBancarias() {
        Lcdpr semContas = comContas(Collections.emptyList());

        assertThat(LcdprValidator.validate(semContas))
                .contains(new ErroValidacao(0, "0050", null, "Registro 0050 obrigatório"));
    }

    @Test
    @DisplayName("Sem imóveis rurais reporta erro no registro 0040")
    void shouldReportMissingImoveisRurais() {
        Lcdpr semImoveis = comImoveis(Collections.emptyList());

        assertThat(LcdprValidator.validate(semImoveis))
                .contains(new ErroValidacao(0, "0040", null, "Registro 0040 obrigatório"));
    }

    @Test
    @DisplayName("E-mail ausente no 0030 gera erro na linha 3")
    void shouldReportMissingEmailInRegistro0030OnLineThree() {
        Lcdpr semEmail = comDados(dadosValidos().toBuilder().email(null).build());

        assertThat(LcdprValidator.validate(semEmail))
                .contains(new ErroValidacao(3, "0030", "EMAIL", "0030.EMAIL obrigatório"));
    }

    @Test
    @DisplayName("Endereço com mais de 150 caracteres gera erro no 0030")
    void shouldReportTextLongerThanLeiauteSize() {
        Lcdpr enderecoLongo = comDados(dadosValidos().toBuilder().endereco(new String(new char[151]).replace('\0', 'A')).build());

        assertThat(LcdprValidator.validate(enderecoLongo))
                .contains(new ErroValidacao(3, "0030", "ENDERECO", "0030.ENDERECO com mais de 150 caracteres"));
    }

    @Test
    @DisplayName("Código do imóvel com um dígito gera erro no 0040")
    void shouldReportCodigoImovelWithoutThreeDigits() {
        ImovelRural imovel = imovel(0).toBuilder().codigoImovel("1").build();
        Lcdpr codigoCurto = comImoveis(Arrays.asList(imovel, imovel(1)));

        assertThat(LcdprValidator.validate(codigoCurto))
                .contains(new ErroValidacao(4, "0040", "COD_IMOVEL", "0040.COD_IMOVEL deve ter 3 dígitos"));
    }

    @Test
    @DisplayName("CPF do 0000 com dígito verificador inválido gera erro")
    void shouldReportCpfWithInvalidCheckDigit() {
        IdentificacaoPessoaFisica identificacao = identificacaoValida().toBuilder().cpf("11111111191").build();

        assertThat(LcdprValidator.validate(comIdentificacao(identificacao)))
                .contains(new ErroValidacao(1, "0000", "CPF", "0000.CPF com dígito verificador inválido"));
    }

    @Test
    @DisplayName("CNPJ alfanumérico com dígito verificador válido é aceito no Q100")
    void shouldAcceptAlphanumericCnpjWithValidCheckDigitInQ100() {
        Lcdpr alfanumerico = comLancamentos(alterar(lancamentosValidos(), 0,
                lancamento -> lancamento.toBuilder().cpfCnpjParticipante("12ABC34501DE35").build()));

        assertThat(LcdprValidator.validate(alfanumerico)).isEmpty();
    }

    @Test
    @DisplayName("CNPJ alfanumérico com dígito verificador inválido gera erro no Q100")
    void shouldReportAlphanumericCnpjWithInvalidCheckDigit() {
        Lcdpr alfanumerico = comLancamentos(alterar(lancamentosValidos(), 0,
                lancamento -> lancamento.toBuilder().cpfCnpjParticipante("12ABC34501DE36").build()));

        assertThat(LcdprValidator.validate(alfanumerico))
                .contains(new ErroValidacao(8, "Q100", "ID_PARTIC", "Q100.ID_PARTIC com dígito verificador inválido"));
    }

    @Test
    @DisplayName("Data inicial posterior à final gera erro no 0000")
    void shouldReportDtIniAfterDtFin() {
        IdentificacaoPessoaFisica identificacao = identificacaoValida().toBuilder()
                .dataInicial(LocalDate.of(2019, 12, 31))
                .dataFinal(LocalDate.of(2019, 1, 1))
                .build();

        assertThat(LcdprValidator.validate(comIdentificacao(identificacao)))
                .contains(new ErroValidacao(1, "0000", "DT_INI", "0000.DT_INI posterior a 0000.DT_FIN"));
    }

    @Test
    @DisplayName("Data final em outro ano gera erro no 0000")
    void shouldReportDtFinOutsideYearOfDtIni() {
        IdentificacaoPessoaFisica identificacao = identificacaoValida().toBuilder()
                .dataFinal(LocalDate.of(2020, 1, 1))
                .build();

        assertThat(LcdprValidator.validate(comIdentificacao(identificacao)))
                .contains(new ErroValidacao(1, "0000", "DT_FIN", "0000.DT_FIN fora do ano de 0000.DT_INI"));
    }

    @Test
    @DisplayName("Falecimento sem data da situação especial gera erro no 0000")
    void shouldReportMissingDtSitEspWhenFalecimento() {
        IdentificacaoPessoaFisica identificacao = identificacaoValida().toBuilder()
                .situacaoEspecial(SituacaoEspecial.FALECIMENTO)
                .build();

        assertThat(LcdprValidator.validate(comIdentificacao(identificacao)))
                .contains(new ErroValidacao(1, "0000", "DT_SIT_ESP",
                        "0000.DT_SIT_ESP obrigatório com SIT_ESPECIAL 1 (falecimento)"));
    }

    @Test
    @DisplayName("Participação de 50% sem terceiros gera erro no 0040")
    void shouldReportMissingTerceirosWhenParticipacaoIsPartial() {
        ImovelRural semTerceiros = imovel(0).toBuilder().terceiros(Collections.emptyList()).build();

        assertThat(LcdprValidator.validate(comImoveis(Arrays.asList(semTerceiros, imovel(1)))))
                .contains(new ErroValidacao(4, "0040", null,
                        "Registro 0045 obrigatório: participação menor que 100% ou exploração não individual"));
    }

    @Test
    @DisplayName("Terceiro com COD_IMOVEL diferente do 0040 pai gera erro no 0045")
    void shouldReportTerceiroWithDifferentCodigoImovel() {
        ImovelRural pai = imovel(0);
        ImovelRural comTerceiroDeOutroImovel = pai.toBuilder()
                .terceiros(Collections.singletonList(pai.terceiros().get(0).toBuilder().codigoImovel("002").build()))
                .build();

        assertThat(LcdprValidator.validate(comImoveis(Arrays.asList(comTerceiroDeOutroImovel, imovel(1)))))
                .contains(new ErroValidacao(5, "0045", "COD_IMOVEL", "0045.COD_IMOVEL diferente do 0040 pai (001)"));
    }

    @Test
    @DisplayName("CAEPF ausente com exploração individual e país BR gera erro no 0040")
    void shouldReportMissingCaepfForIndividualExploitationInBrazil() {
        ImovelRural semCaepf = imovel(1).toBuilder().caepf(null).build();

        assertThat(LcdprValidator.validate(comImoveis(Arrays.asList(imovel(0), semCaepf))))
                .contains(new ErroValidacao(6, "0040", "CAEPF", "0040.CAEPF obrigatório"));
    }

    @Test
    @DisplayName("Código de conta repetido gera erro no segundo 0050")
    void shouldReportRepeatedCodigoConta() {
        ContaBancaria conta = contaValida();
        Lcdpr repetida = comContas(Arrays.asList(conta, conta.toBuilder().nomeBanco("OUTRO BANCO").build()));

        assertThat(LcdprValidator.validate(repetida))
                .contains(new ErroValidacao(8, "0050", "COD_CONTA", "0050.COD_CONTA repetido"));
    }

    @Test
    @DisplayName("Banco ausente com país BR gera erro no 0050")
    void shouldReportMissingBancoWhenPaisIsBrazil() {
        Lcdpr semBanco = comContas(Collections.singletonList(contaValida().toBuilder().banco(null).build()));

        assertThat(LcdprValidator.validate(semBanco))
                .contains(new ErroValidacao(7, "0050", "BANCO", "0050.BANCO obrigatório"));
    }

    @Test
    @DisplayName("Lançamento com conta inexistente gera erro no Q100")
    void shouldReportLancamentoWithUnknownCodigoConta() {
        Lcdpr contaInexistente = comLancamentos(alterar(lancamentosValidos(), 0,
                lancamento -> lancamento.toBuilder().codigoConta("777").build()));

        assertThat(LcdprValidator.validate(contaInexistente))
                .contains(new ErroValidacao(8, "Q100", "COD_CONTA", "Q100.COD_CONTA não cadastrado no 0050"));
    }

    @Test
    @DisplayName("Lançamento com conta 999 (numerário em trânsito) não gera erro")
    void shouldAcceptCodigoContaTransitoAtQ100() {
        Lcdpr contaTransito = comLancamentos(alterar(lancamentosValidos(), 0,
                lancamento -> lancamento.toBuilder().codigoConta("999").build()));

        assertThat(LcdprValidator.validate(contaTransito)).isEmpty();
    }

    @Test
    @DisplayName("Lançamento com data fora do período gera erro no Q100")
    void shouldReportLancamentoOutsidePeriod() {
        Lcdpr foraDoPeriodo = comLancamentos(alterar(lancamentosValidos(), 0,
                lancamento -> lancamento.toBuilder().data(LocalDate.of(2018, 12, 31)).build()));

        assertThat(LcdprValidator.validate(foraDoPeriodo))
                .contains(new ErroValidacao(8, "Q100", "DATA", "Q100.DATA fora do período da escrituração"));
    }

    @Test
    @DisplayName("Entrada e saída preenchidas no mesmo lançamento geram erro no Q100")
    void shouldReportEntradaAndSaidaInSameLancamento() {
        Lcdpr entradaESaida = comLancamentos(alterar(lancamentosValidos(), 0,
                lancamento -> lancamento.toBuilder().valorSaida(new BigDecimal("100.00")).build()));

        assertThat(LcdprValidator.validate(entradaESaida))
                .contains(new ErroValidacao(8, "Q100", "VL_ENTRADA",
                        "Q100.VL_ENTRADA e Q100.VL_SAIDA preenchidos no mesmo lançamento"));
    }

    @Test
    @DisplayName("Resumo mensal fora do período gera erro no Q200")
    void shouldReportResumoMensalOutsidePeriod() {
        Lcdpr mesForaDoPeriodo = comResumos(alterar(resumosValidos(), 0,
                resumo -> resumo.toBuilder().mes(YearMonth.of(2020, 1)).build()));

        assertThat(LcdprValidator.validate(mesForaDoPeriodo))
                .contains(new ErroValidacao(11, "Q200", "MES", "Q200.MES fora do período da escrituração"));
    }

    @Test
    @DisplayName("Saldo final do Q100 diferente do calculado gera erro com o valor esperado")
    void shouldReportSaldoDivergentFromCalculated() {
        Lcdpr saldoAlterado = comLancamentos(alterar(lancamentosValidos(), 0,
                lancamento -> lancamento.toBuilder().saldoFinal(new BigDecimal("4000.00")).build()));

        assertThat(LcdprValidator.validate(saldoAlterado))
                .contains(new ErroValidacao(8, "Q100", "SLD_FIN",
                        "Q100.SLD_FIN divergente do calculado: esperado 10000.00 P"));
    }

    @Test
    @DisplayName("Lançamentos fora de ordem de data geram um erro no primeiro fora de ordem")
    void shouldReportLancamentosOutOfDateOrder() {
        List<Lancamento> invertidos = new ArrayList<>(lancamentosValidos());
        Collections.reverse(invertidos);

        assertThat(LcdprValidator.validate(comLancamentos(invertidos)))
                .contains(new ErroValidacao(9, "Q100", "DATA", "Q100 fora da ordem de data"));
    }

    @Test
    @DisplayName("Quantidade de meses do Q200 diferente do calculado gera erro sem linha")
    void shouldReportQ200WithDifferentMonthCount() {
        Lcdpr umMes = comResumos(Collections.singletonList(resumosValidos().get(0)));

        assertThat(LcdprValidator.validate(umMes))
                .contains(new ErroValidacao(0, "Q200", null, "Q200 com 1 mês(es); esperado 2"));
    }

    @Test
    @DisplayName("Lançamentos fora de ordem de data são conferidos pelo saldo da posição ordenada")
    void shouldCheckBalancesEvenWhenEntriesAreOutOfOrder() {
        List<Lancamento> trocados = new ArrayList<>(lancamentosValidos());
        Collections.swap(trocados, 0, 1);

        assertThat(LcdprValidator.validate(comLancamentos(trocados)))
                .containsExactly(new ErroValidacao(9, "Q100", "DATA", "Q100 fora da ordem de data"));
    }

    @Test
    @DisplayName("CNPJ com os 14 caracteres iguais gera erro de dígito verificador no 9999")
    void shouldRejectCnpjWithAllSameDigits() {
        Lcdpr cnpjRepetido = comBloco9(Amostras.valido().bloco9().toBuilder()
                .cpfCnpjContador("00000000000000")
                .build());

        assertThat(LcdprValidator.validate(cnpjRepetido))
                .contains(new ErroValidacao(13, "9999", "IDENT_CPF_CNPJ",
                        "9999.IDENT_CPF_CNPJ com dígito verificador inválido"));
    }

    @Test
    @DisplayName("CNPJ numérico com dígito verificador inválido gera erro no Q100")
    void shouldRejectNumericCnpjWithInvalidCheckDigit() {
        Lcdpr cnpjInvalido = comLancamentos(alterar(lancamentosValidos(), 1,
                lancamento -> lancamento.toBuilder().cpfCnpjParticipante("11222333000180").build()));

        assertThat(LcdprValidator.validate(cnpjInvalido))
                .contains(new ErroValidacao(9, "Q100", "ID_PARTIC", "Q100.ID_PARTIC com dígito verificador inválido"));
    }

    @Test
    @DisplayName("Lançamento com imóvel não cadastrado gera erro no Q100")
    void shouldReportLancamentoWithUnknownCodigoImovel() {
        Lcdpr imovelInexistente = comLancamentos(alterar(lancamentosValidos(), 0,
                lancamento -> lancamento.toBuilder().codigoImovel("777").build()));

        assertThat(LcdprValidator.validate(imovelInexistente))
                .contains(new ErroValidacao(8, "Q100", "COD_IMOVEL", "Q100.COD_IMOVEL não cadastrado no 0040"));
    }

    @Test
    @DisplayName("Participação do 0040 acima de 100% gera erro")
    void shouldReportParticipacaoOutOfRange() {
        ImovelRural acima = imovel(1).toBuilder().participacao(new BigDecimal("150")).build();

        assertThat(LcdprValidator.validate(comImoveis(Arrays.asList(imovel(0), acima))))
                .contains(new ErroValidacao(6, "0040", "PARTICIPACAO", "0040.PARTICIPACAO fora do intervalo de 0 a 100"));
    }

    @Test
    @DisplayName("Percentual de contraparte do 0045 acima de 100% gera erro")
    void shouldReportPercContraparteOutOfRange() {
        ImovelRural pai = imovel(0);
        ImovelRural comPercentualAlto = pai.toBuilder()
                .terceiros(Collections.singletonList(
                        pai.terceiros().get(0).toBuilder().participacao(new BigDecimal("150")).build()))
                .build();

        assertThat(LcdprValidator.validate(comImoveis(Arrays.asList(comPercentualAlto, imovel(1)))))
                .contains(new ErroValidacao(5, "0045", "PERC_CONTRAPARTE",
                        "0045.PERC_CONTRAPARTE fora do intervalo de 0 a 100"));
    }

    @Test
    @DisplayName("UF ausente em 0040 com país BR gera erro")
    void shouldReportMissingUfWhenPaisIsBrazil() {
        ImovelRural semUf = imovel(1).toBuilder().uf(null).build();

        assertThat(LcdprValidator.validate(comImoveis(Arrays.asList(imovel(0), semUf))))
                .contains(new ErroValidacao(6, "0040", "UF", "0040.UF obrigatório"));
    }

    @Test
    @DisplayName("Saldo final do Q200 diferente do calculado gera erro com o valor esperado")
    void shouldReportResumoSaldoDivergentFromCalculated() {
        Lcdpr saldoAlterado = comResumos(alterar(resumosValidos(), 0,
                resumo -> resumo.toBuilder().saldoFinal(new BigDecimal("4000.00")).build()));

        assertThat(LcdprValidator.validate(saldoAlterado))
                .contains(new ErroValidacao(11, "Q200", "SLD_FIN",
                        "Q200.SLD_FIN divergente do calculado: esperado 5000.00 P"));
    }

    @Test
    @DisplayName("Valor negativo no Q100 gera erro do formato citando Q100.VL_ENTRADA")
    void shouldReportNegativeValueFromCodecWithRecordAndField() {
        Lcdpr negativo = comLancamentos(alterar(lancamentosValidos(), 0,
                lancamento -> lancamento.toBuilder().valorEntrada(new BigDecimal("-1")).build()));

        assertThat(LcdprValidator.validate(negativo))
                .contains(new ErroValidacao(8, "Q100", "VL_ENTRADA",
                        "Q100.VL_ENTRADA: valor negativo não é permitido no LCDPR"));
    }

    @Test
    @DisplayName("Quantidade de linhas diferente do total gera erro no 9999")
    void shouldReportQtdLinDivergentFromTotal() {
        Lcdpr qtdErrada = comBloco9(Amostras.valido().bloco9().toBuilder().quantidadeLinhas(99L).build());

        assertThat(LcdprValidator.validate(qtdErrada))
                .contains(new ErroValidacao(13, "9999", "QTD_LIN", "9999.QTD_LIN divergente do calculado: esperado 13"));
    }

    private static Lcdpr comBloco0(AberturaIdentificacao bloco0) {
        return Amostras.valido().toBuilder().bloco0(bloco0).build();
    }

    private static Lcdpr comBloco9(EncerramentoArquivo bloco9) {
        return Amostras.valido().toBuilder().bloco9(bloco9).build();
    }

    private static Lcdpr comIdentificacao(IdentificacaoPessoaFisica identificacao) {
        return comBloco0(bloco0Valido().toBuilder().identificacao(identificacao).build());
    }

    private static Lcdpr comDados(DadosCadastrais dados) {
        return comBloco0(bloco0Valido().toBuilder().dadosCadastrais(dados).build());
    }

    private static Lcdpr comImoveis(List<ImovelRural> imoveis) {
        return comBloco0(bloco0Valido().toBuilder().imoveisRurais(imoveis).build());
    }

    private static Lcdpr comContas(List<ContaBancaria> contas) {
        return comBloco0(bloco0Valido().toBuilder().contasBancarias(contas).build());
    }

    private static Lcdpr comLancamentos(List<Lancamento> lancamentos) {
        return comBlocoQ(blocoQValido().toBuilder().lancamentos(lancamentos).build());
    }

    private static Lcdpr comResumos(List<ResumoMensal> resumos) {
        return comBlocoQ(blocoQValido().toBuilder().resumosMensais(resumos).build());
    }

    private static Lcdpr comBlocoQ(DemonstrativoResultado blocoQ) {
        return Amostras.valido().toBuilder().blocoQ(blocoQ).build();
    }

    private static AberturaIdentificacao bloco0Valido() {
        return Amostras.valido().bloco0();
    }

    private static DemonstrativoResultado blocoQValido() {
        return Amostras.valido().blocoQ();
    }

    private static IdentificacaoPessoaFisica identificacaoValida() {
        return bloco0Valido().identificacao();
    }

    private static DadosCadastrais dadosValidos() {
        return bloco0Valido().dadosCadastrais();
    }

    private static ImovelRural imovel(int indice) {
        return bloco0Valido().imoveisRurais().get(indice);
    }

    private static ContaBancaria contaValida() {
        return bloco0Valido().contasBancarias().get(0);
    }

    private static List<Lancamento> lancamentosValidos() {
        return blocoQValido().lancamentos();
    }

    private static List<ResumoMensal> resumosValidos() {
        return blocoQValido().resumosMensais();
    }

    private static <T> List<T> alterar(List<T> itens, int indice, UnaryOperator<T> alteracao) {
        List<T> alterados = new ArrayList<>(itens);
        alterados.set(indice, alteracao.apply(itens.get(indice)));
        return alterados;
    }
}
