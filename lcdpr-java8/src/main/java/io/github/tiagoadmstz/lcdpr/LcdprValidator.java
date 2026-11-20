package io.github.tiagoadmstz.lcdpr;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.function.Supplier;

import io.github.tiagoadmstz.lcdpr.leiaute.Campo;
import io.github.tiagoadmstz.lcdpr.leiaute.Formato;
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
import io.github.tiagoadmstz.lcdpr.tabela.NaturezaSaldo;
import io.github.tiagoadmstz.lcdpr.tabela.SituacaoEspecial;
import io.github.tiagoadmstz.lcdpr.tabela.TipoExploracao;

/**
 * Valida o registro LCDPR contra as regras do leiaute 1.3 e do manual de preenchimento.
 * Nunca lança exceção por conteúdo: devolve a lista de erros, vazia quando o registro é válido.
 */
public final class LcdprValidator {

    private static final String PAIS_BRASIL = "BR";
    private static final String CODIGO_SEM_IMOVEL = "000";
    private static final String CODIGO_CONTA_ESPECIE = "000";
    private static final String CODIGO_CONTA_TRANSITO = "999";
    private static final BigDecimal CEM = new BigDecimal("100");
    private static final int DIGITOS_VALOR = 19;
    private static final int DIGITOS_PERCENTUAL = 5;
    private static final int TAMANHO_CPF = 11;
    private static final int MODULO_DV = 11;
    private static final int AJUSTE_CAEPF = 12;
    private static final int[] PESOS_CPF_PRIMEIRO = {10, 9, 8, 7, 6, 5, 4, 3, 2};
    private static final int[] PESOS_CPF_SEGUNDO = {11, 10, 9, 8, 7, 6, 5, 4, 3, 2};
    private static final int[] PESOS_CNPJ_PRIMEIRO = {5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2};
    private static final int[] PESOS_CNPJ_SEGUNDO = {6, 5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2};

    private LcdprValidator() {
    }

    /**
     * Valida o registro LCDPR. Os erros saem na ordem em que os registros aparecem no arquivo.
     *
     * @param lcdpr registro a validar
     * @return lista imutável de erros; vazia quando o registro é válido
     */
    public static List<ErroValidacao> validate(Lcdpr lcdpr) {
        return validate(lcdpr, false);
    }

    /**
     * Valida o registro LCDPR com opção de validação estrita de CAEPF.
     *
     * @param lcdpr registro a validar
     * @param estritoCaepf se true, valida o dígito verificador do CAEPF no registro 0040
     * @return lista imutável de erros; vazia quando o registro é válido
     */
    public static List<ErroValidacao> validate(Lcdpr lcdpr, boolean estritoCaepf) {
        if (lcdpr == null) {
            return Collections.singletonList(new ErroValidacao(0, null, null, "Arquivo LCDPR ausente"));
        }
        if (lcdpr.bloco0() == null) {
            return Collections.singletonList(new ErroValidacao(0, null, null, "Bloco 0 obrigatório"));
        }
        Verificacao verificacao = new Verificacao(lcdpr, estritoCaepf);
        verificacao.executar();
        return Collections.unmodifiableList(new ArrayList<>(verificacao.erros));
    }

    /**
     * Valida o dígito verificador de um CAEPF (14 dígitos numéricos: CPF do titular, ordem e 2 DV).
     * O algoritmo é o divulgado por fontes da comunidade (módulo 11 com pesos de 9 a 2 a partir da unidade
     * e ajuste final de +12), não por documento oficial da RFB.
     *
     * @param caepf número do CAEPF com 14 dígitos
     * @return true se o dígito verificador for válido
     */
    public static boolean caepfValido(String caepf) {
        if (caepf == null || !caepf.matches("[0-9]{14}")) {
            return false;
        }
        String base = caepf.substring(0, 12);
        int primeiro = digitoCaepf(base);
        int segundo = digitoCaepf(base + primeiro);
        int esperado = (primeiro * 10 + segundo + AJUSTE_CAEPF) % 100;
        return Integer.parseInt(caepf.substring(12)) == esperado;
    }

    /**
     * Verifica se o código do município do IBGE (7 dígitos) é coerente com a UF informada.
     * Os dois primeiros dígitos do código do município correspondem ao código da UF no IBGE.
     *
     * @param codigoMunicipio código do município no IBGE (7 dígitos)
     * @param uf sigla da Unidade Federativa (2 letras)
     * @return true se forem coerentes
     */
    public static boolean municipioCoerenteComUf(String codigoMunicipio, String uf) {
        if (codigoMunicipio == null || uf == null || codigoMunicipio.length() < 2) {
            return false;
        }
        String prefixo = prefixoUf(uf);
        return prefixo != null && codigoMunicipio.startsWith(prefixo);
    }

    private static String prefixoUf(String uf) {
        switch (uf.toUpperCase(Locale.ROOT)) {
            case "RO": return "11";
            case "AC": return "12";
            case "AM": return "13";
            case "RR": return "14";
            case "PA": return "15";
            case "AP": return "16";
            case "TO": return "17";
            case "MA": return "21";
            case "PI": return "22";
            case "CE": return "23";
            case "RN": return "24";
            case "PB": return "25";
            case "PE": return "26";
            case "AL": return "27";
            case "SE": return "28";
            case "BA": return "29";
            case "MG": return "31";
            case "ES": return "32";
            case "RJ": return "33";
            case "SP": return "35";
            case "PR": return "41";
            case "SC": return "42";
            case "RS": return "43";
            case "MS": return "50";
            case "MT": return "51";
            case "GO": return "52";
            case "DF": return "53";
            default: return null;
        }
    }

    private static boolean vazio(String valor) {
        return valor == null || valor.isEmpty();
    }

    private static boolean positivo(BigDecimal valor) {
        return valor != null && valor.signum() > 0;
    }

    private static boolean foraDeZeroCem(BigDecimal valor) {
        return valor.signum() < 0 || valor.compareTo(CEM) > 0;
    }

    private static boolean somenteDigitos(String valor) {
        return valor.matches("[0-9]+");
    }

    private static boolean documentoFormatoValido(String valor) {
        return valor.matches("[0-9]{11}") || valor.matches("[0-9A-Z]{12}[0-9]{2}");
    }

    private static String problemaFormato(Campo campo, String valor) {
        int tamanho = campo.tamanho();
        Formato formato = campo.formato();
        if (formato == Formato.TEXTO) {
            return valor.length() > tamanho ? "com mais de " + tamanho + " caracteres" : null;
        }
        if (formato == Formato.NUMERO) {
            if (!somenteDigitos(valor)) {
                return "deve conter só dígitos";
            }
            return valor.length() > tamanho ? "com mais de " + tamanho + " dígitos" : null;
        }
        if (formato == Formato.NUMERO_EXATO) {
            if (!somenteDigitos(valor)) {
                return "deve conter só dígitos";
            }
            return valor.length() == tamanho ? null : "deve ter " + tamanho + " dígitos";
        }
        if (formato == Formato.CPF_CNPJ) {
            return documentoFormatoValido(valor) ? null : "deve ter 11 (CPF) ou 14 (CNPJ) posições";
        }
        if (formato == Formato.VALOR) {
            if (!somenteDigitos(valor)) {
                return "deve conter só dígitos";
            }
            return valor.length() > DIGITOS_VALOR ? "com mais de " + DIGITOS_VALOR + " dígitos" : null;
        }
        if (formato == Formato.PERCENTUAL) {
            if (!somenteDigitos(valor)) {
                return "deve conter só dígitos";
            }
            return valor.length() > DIGITOS_PERCENTUAL ? "com mais de " + DIGITOS_PERCENTUAL + " dígitos" : null;
        }
        return null;
    }

    private static boolean verificaDv(Registro registro, Campo campo) {
        return (registro == Registro.R0000 && "CPF".equals(campo.nome()))
                || (registro == Registro.R0045 && "ID_CONTRAPARTE".equals(campo.nome()))
                || (registro == Registro.RQ100 && "ID_PARTIC".equals(campo.nome()))
                || (registro == Registro.R9999 && "IDENT_CPF_CNPJ".equals(campo.nome()));
    }

    private static boolean dvValido(String documento) {
        if (documento.chars().distinct().count() == 1) {
            return false;
        }
        return documento.length() == TAMANHO_CPF ? cpfValido(documento) : cnpjValido(documento);
    }

    private static boolean cpfValido(String cpf) {
        int primeiro = digitoVerificador(cpf.substring(0, 9), PESOS_CPF_PRIMEIRO);
        int segundo = digitoVerificador(cpf.substring(0, 10), PESOS_CPF_SEGUNDO);
        return valorCaractere(cpf, 9) == primeiro && valorCaractere(cpf, 10) == segundo;
    }

    private static boolean cnpjValido(String cnpj) {
        int primeiro = digitoVerificador(cnpj.substring(0, 12), PESOS_CNPJ_PRIMEIRO);
        int segundo = digitoVerificador(cnpj.substring(0, 13), PESOS_CNPJ_SEGUNDO);
        return valorCaractere(cnpj, 12) == primeiro && valorCaractere(cnpj, 13) == segundo;
    }

    private static int digitoVerificador(String base, int[] pesos) {
        int soma = 0;
        for (int i = 0; i < pesos.length; i++) {
            soma += valorCaractere(base, i) * pesos[i];
        }
        int resto = soma % MODULO_DV;
        return resto < 2 ? 0 : MODULO_DV - resto;
    }

    private static int digitoCaepf(String base) {
        int soma = 0;
        for (int i = 0; i < base.length(); i++) {
            int peso = 9 - (i % 8);
            soma += valorCaractere(base, base.length() - 1 - i) * peso;
        }
        int resto = soma % MODULO_DV;
        return resto == 10 ? 0 : resto;
    }

    private static int valorCaractere(String texto, int indice) {
        return texto.charAt(indice) - '0';
    }

    private static String descreverSaldo(BigDecimal saldo, NaturezaSaldo natureza) {
        return saldo.toPlainString() + " " + natureza.codigo();
    }

    private static int primeiroForaDeOrdem(List<Lancamento> lancamentos) {
        for (int i = 1; i < lancamentos.size(); i++) {
            if (LcdprCalculator.ORDEM_DATA.compare(lancamentos.get(i - 1).data(), lancamentos.get(i).data()) > 0) {
                return i;
            }
        }
        return -1;
    }

    private static final class Verificacao {

        private final Lcdpr lcdpr;
        private final boolean estritoCaepf;
        private final List<ErroValidacao> erros = new ArrayList<>();
        private final Set<String> codigosImovel = new HashSet<>();
        private final Set<String> codigosConta = new HashSet<>();
        private int proximaLinha = 1;
        private LocalDate dataInicial;
        private LocalDate dataFinal;

        Verificacao(Lcdpr lcdpr, boolean estritoCaepf) {
            this.lcdpr = lcdpr;
            this.estritoCaepf = estritoCaepf;
        }

        void executar() {
            AberturaIdentificacao bloco0 = lcdpr.bloco0();
            verificarIdentificacao(bloco0.identificacao());
            verificarParametros(bloco0.parametrosTributacao());
            verificarDadosCadastrais(bloco0.dadosCadastrais());
            verificarImoveis(bloco0.imoveisRurais());
            verificarContas(bloco0.contasBancarias());
            if (lcdpr.blocoQ() != null) {
                verificarBlocoQ(lcdpr.blocoQ());
            }
            verificarEncerramento(lcdpr.bloco9());
        }

        private void registrar(int linha, String registro, String campo, String mensagem) {
            erros.add(new ErroValidacao(linha, registro, campo, mensagem));
        }

        private void exigirPreenchido(int linha, String registro, String campo, String valor) {
            if (vazio(valor)) {
                registrar(linha, registro, campo, registro + "." + campo + " obrigatório");
            }
        }

        private void campos(Registro registro, int linha, Supplier<List<String>> formatar) {
            List<String> valores;
            try {
                valores = formatar.get();
            } catch (RegistroCodec.ErroCampo e) {
                registrar(linha, registro.codigo(), e.campo(), e.getMessage());
                return;
            } catch (LcdprException e) {
                registrar(linha, registro.codigo(), null, e.getMessage());
                return;
            }
            List<Campo> campos = registro.campos();
            for (int i = 0; i < campos.size(); i++) {
                verificarCampo(registro, linha, campos.get(i), valores.get(i));
            }
        }

        private void verificarCampo(Registro registro, int linha, Campo campo, String valor) {
            String rotulo = registro.codigo() + "." + campo.nome();
            if (valor.isEmpty()) {
                if (campo.obrigatorio()) {
                    registrar(linha, registro.codigo(), campo.nome(), rotulo + " obrigatório");
                }
                return;
            }
            String problema = problemaFormato(campo, valor);
            if (problema != null) {
                registrar(linha, registro.codigo(), campo.nome(), rotulo + " " + problema);
                return;
            }
            if (verificaDv(registro, campo) && !dvValido(valor)) {
                registrar(linha, registro.codigo(), campo.nome(), rotulo + " com dígito verificador inválido");
            }
        }

        private void verificarIdentificacao(IdentificacaoPessoaFisica identificacao) {
            if (identificacao == null) {
                registrar(0, "0000", null, "Registro 0000 obrigatório");
                return;
            }
            int linha = proximaLinha++;
            dataInicial = identificacao.dataInicial();
            dataFinal = identificacao.dataFinal();
            campos(Registro.R0000, linha, () -> RegistroCodec.valores(identificacao));
            if (dataInicial != null && dataFinal != null) {
                if (dataInicial.isAfter(dataFinal)) {
                    registrar(linha, "0000", "DT_INI", "0000.DT_INI posterior a 0000.DT_FIN");
                } else if (dataInicial.getYear() != dataFinal.getYear()) {
                    registrar(linha, "0000", "DT_FIN", "0000.DT_FIN fora do ano de 0000.DT_INI");
                }
            }
            if (identificacao.situacaoEspecial() == SituacaoEspecial.FALECIMENTO
                    && identificacao.dataSituacaoEspecial() == null) {
                registrar(linha, "0000", "DT_SIT_ESP",
                        "0000.DT_SIT_ESP obrigatório com SIT_ESPECIAL 1 (falecimento)");
            }
        }

        private void verificarParametros(ParametrosTributacao parametros) {
            if (parametros == null) {
                registrar(0, "0010", null, "Registro 0010 obrigatório");
                return;
            }
            int linha = proximaLinha++;
            campos(Registro.R0010, linha, () -> RegistroCodec.valores(parametros));
        }

        private void verificarDadosCadastrais(DadosCadastrais dados) {
            if (dados == null) {
                registrar(0, "0030", null, "Registro 0030 obrigatório");
                return;
            }
            int linha = proximaLinha++;
            campos(Registro.R0030, linha, () -> RegistroCodec.valores(dados));
            if (!vazio(dados.codigoMunicipio()) && !vazio(dados.uf())
                    && !municipioCoerenteComUf(dados.codigoMunicipio(), dados.uf())) {
                registrar(linha, "0030", "COD_MUN", "0030.COD_MUN incoerente com a UF informada");
            }
        }

        private void verificarImoveis(List<ImovelRural> imoveis) {
            if (imoveis.isEmpty()) {
                registrar(0, "0040", null, "Registro 0040 obrigatório");
            }
            for (ImovelRural imovel : imoveis) {
                verificarImovel(imovel);
            }
        }

        private void verificarImovel(ImovelRural imovel) {
            int linha = proximaLinha++;
            if (!vazio(imovel.codigoImovel())) {
                codigosImovel.add(imovel.codigoImovel());
            }
            campos(Registro.R0040, linha, () -> RegistroCodec.valores(imovel));
            verificarRegrasImovel(imovel, linha);
            for (Terceiro terceiro : imovel.terceiros()) {
                verificarTerceiro(imovel.codigoImovel(), terceiro, proximaLinha++);
            }
        }

        private void verificarRegrasImovel(ImovelRural imovel, int linha) {
            BigDecimal participacao = imovel.participacao();
            if (participacao != null && foraDeZeroCem(participacao)) {
                registrar(linha, "0040", "PARTICIPACAO", "0040.PARTICIPACAO fora do intervalo de 0 a 100");
            }
            if (PAIS_BRASIL.equals(imovel.pais())) {
                exigirPreenchido(linha, "0040", "UF", imovel.uf());
                exigirPreenchido(linha, "0040", "COD_MUN", imovel.codigoMunicipio());
                exigirPreenchido(linha, "0040", "CEP", imovel.cep());
                if (!vazio(imovel.codigoMunicipio()) && !vazio(imovel.uf())
                        && !municipioCoerenteComUf(imovel.codigoMunicipio(), imovel.uf())) {
                    registrar(linha, "0040", "COD_MUN", "0040.COD_MUN incoerente com a UF informada");
                }
                if (imovel.tipoExploracao() == TipoExploracao.INDIVIDUAL) {
                    exigirPreenchido(linha, "0040", "CAEPF", imovel.caepf());
                }
            }
            if (estritoCaepf && !vazio(imovel.caepf()) && !caepfValido(imovel.caepf())) {
                registrar(linha, "0040", "CAEPF", "0040.CAEPF com dígito verificador inválido");
            }
            boolean participacaoParcial = participacao != null && participacao.compareTo(CEM) < 0;
            boolean exploracaoCompartilhada = imovel.tipoExploracao() != null
                    && imovel.tipoExploracao() != TipoExploracao.INDIVIDUAL;
            if ((participacaoParcial || exploracaoCompartilhada) && imovel.terceiros().isEmpty()) {
                registrar(linha, "0040", null,
                        "Registro 0045 obrigatório: participação menor que 100% ou exploração não individual");
            }
        }

        private void verificarTerceiro(String codigoPai, Terceiro terceiro, int linha) {
            campos(Registro.R0045, linha, () -> RegistroCodec.valores(terceiro));
            if (!vazio(codigoPai) && !vazio(terceiro.codigoImovel()) && !codigoPai.equals(terceiro.codigoImovel())) {
                registrar(linha, "0045", "COD_IMOVEL", "0045.COD_IMOVEL diferente do 0040 pai (" + codigoPai + ")");
            }
            if (terceiro.participacao() != null && foraDeZeroCem(terceiro.participacao())) {
                registrar(linha, "0045", "PERC_CONTRAPARTE", "0045.PERC_CONTRAPARTE fora do intervalo de 0 a 100");
            }
        }

        private void verificarContas(List<ContaBancaria> contas) {
            if (contas.isEmpty()) {
                registrar(0, "0050", null, "Registro 0050 obrigatório");
            }
            for (ContaBancaria conta : contas) {
                verificarConta(conta, proximaLinha++);
            }
        }

        private void verificarConta(ContaBancaria conta, int linha) {
            campos(Registro.R0050, linha, () -> RegistroCodec.valores(conta));
            String codigo = conta.codigoConta();
            if (!vazio(codigo) && !codigosConta.add(codigo)) {
                registrar(linha, "0050", "COD_CONTA", "0050.COD_CONTA repetido");
            }
            if (PAIS_BRASIL.equals(conta.pais())) {
                exigirPreenchido(linha, "0050", "BANCO", conta.banco());
            }
        }

        private void verificarBlocoQ(DemonstrativoResultado blocoQ) {
            DemonstrativoResultado calculado = LcdprCalculator.recalculate(lcdpr).blocoQ();
            List<Lancamento> lancamentos = blocoQ.lancamentos();
            List<Lancamento> porPosicao = esperadosPorPosicao(lancamentos, calculado.lancamentos());
            int foraDeOrdem = primeiroForaDeOrdem(lancamentos);
            for (int i = 0; i < lancamentos.size(); i++) {
                int linha = proximaLinha++;
                verificarLancamento(lancamentos.get(i), linha);
                if (i == foraDeOrdem) {
                    registrar(linha, "Q100", "DATA", "Q100 fora da ordem de data");
                }
                verificarSaldoLancamento(lancamentos.get(i), porPosicao.get(i), linha);
            }
            verificarResumos(blocoQ.resumosMensais(), calculado.resumosMensais());
        }

        /**
         * Para cada lançamento original (pela posição no arquivo), o calculado que ocupa a mesma posição
         * na ordem de data estável. Os calculados já vêm nessa ordem, então o k-ésimo corresponde ao k-ésimo índice.
         */
        private static List<Lancamento> esperadosPorPosicao(List<Lancamento> originais, List<Lancamento> calculados) {
            List<Integer> ordem = new ArrayList<>(originais.size());
            for (int i = 0; i < originais.size(); i++) {
                ordem.add(i);
            }
            ordem.sort((a, b) -> LcdprCalculator.ORDEM_DATA.compare(originais.get(a).data(), originais.get(b).data()));
            Lancamento[] esperados = new Lancamento[originais.size()];
            for (int k = 0; k < ordem.size(); k++) {
                esperados[ordem.get(k)] = calculados.get(k);
            }
            return Arrays.asList(esperados);
        }

        private void verificarLancamento(Lancamento lancamento, int linha) {
            campos(Registro.RQ100, linha, () -> RegistroCodec.valores(lancamento));
            String codigoImovel = lancamento.codigoImovel();
            if (!vazio(codigoImovel) && !CODIGO_SEM_IMOVEL.equals(codigoImovel)
                    && !codigosImovel.contains(codigoImovel)) {
                registrar(linha, "Q100", "COD_IMOVEL", "Q100.COD_IMOVEL não cadastrado no 0040");
            }
            String codigoConta = lancamento.codigoConta();
            if (!vazio(codigoConta) && !CODIGO_CONTA_ESPECIE.equals(codigoConta)
                    && !CODIGO_CONTA_TRANSITO.equals(codigoConta) && !codigosConta.contains(codigoConta)) {
                registrar(linha, "Q100", "COD_CONTA", "Q100.COD_CONTA não cadastrado no 0050");
            }
            if (lancamento.data() != null && foraDoPeriodo(lancamento.data())) {
                registrar(linha, "Q100", "DATA", "Q100.DATA fora do período da escrituração");
            }
            if (positivo(lancamento.valorEntrada()) && positivo(lancamento.valorSaida())) {
                registrar(linha, "Q100", "VL_ENTRADA",
                        "Q100.VL_ENTRADA e Q100.VL_SAIDA preenchidos no mesmo lançamento");
            }
        }

        private boolean foraDoPeriodo(LocalDate data) {
            return dataInicial != null && dataFinal != null && (data.isBefore(dataInicial) || data.isAfter(dataFinal));
        }

        private boolean foraDoPeriodoMes(YearMonth mes) {
            return dataInicial != null && dataFinal != null
                    && (mes.isBefore(YearMonth.from(dataInicial)) || mes.isAfter(YearMonth.from(dataFinal)));
        }

        private void verificarSaldoLancamento(Lancamento lancamento, Lancamento esperado, int linha) {
            if (lancamento.saldoFinal() != null && lancamento.saldoFinal().compareTo(esperado.saldoFinal()) != 0) {
                registrar(linha, "Q100", "SLD_FIN", "Q100.SLD_FIN divergente do calculado: esperado "
                        + descreverSaldo(esperado.saldoFinal(), esperado.naturezaSaldoFinal()));
            }
            if (lancamento.naturezaSaldoFinal() != null
                    && lancamento.naturezaSaldoFinal() != esperado.naturezaSaldoFinal()) {
                registrar(linha, "Q100", "NAT_SLD_FIN", "Q100.NAT_SLD_FIN divergente do calculado: esperado "
                        + esperado.naturezaSaldoFinal().codigo());
            }
        }

        private void verificarResumos(List<ResumoMensal> resumos, List<ResumoMensal> esperados) {
            boolean mesmaQuantidade = resumos.size() == esperados.size();
            if (!mesmaQuantidade) {
                registrar(0, "Q200", null, "Q200 com " + resumos.size() + " mês(es); esperado " + esperados.size());
            }
            for (int i = 0; i < resumos.size(); i++) {
                int linha = proximaLinha++;
                verificarResumo(resumos.get(i), linha);
                if (mesmaQuantidade) {
                    verificarSaldoResumo(resumos.get(i), esperados.get(i), linha);
                }
            }
        }

        private void verificarResumo(ResumoMensal resumo, int linha) {
            campos(Registro.RQ200, linha, () -> RegistroCodec.valores(resumo));
            if (resumo.mes() != null && foraDoPeriodoMes(resumo.mes())) {
                registrar(linha, "Q200", "MES", "Q200.MES fora do período da escrituração");
            }
        }

        private void verificarSaldoResumo(ResumoMensal resumo, ResumoMensal esperado, int linha) {
            if (resumo.mes() != null && !resumo.mes().equals(esperado.mes())) {
                registrar(linha, "Q200", "MES", "Q200.MES divergente do calculado: esperado "
                        + Formatos.mes(esperado.mes()));
            }
            if (resumo.saldoFinal() != null && resumo.saldoFinal().compareTo(esperado.saldoFinal()) != 0) {
                registrar(linha, "Q200", "SLD_FIN", "Q200.SLD_FIN divergente do calculado: esperado "
                        + descreverSaldo(esperado.saldoFinal(), esperado.naturezaSaldoFinal()));
            }
            if (resumo.naturezaSaldoFinal() != null && resumo.naturezaSaldoFinal() != esperado.naturezaSaldoFinal()) {
                registrar(linha, "Q200", "NAT_SLD_FIN", "Q200.NAT_SLD_FIN divergente do calculado: esperado "
                        + esperado.naturezaSaldoFinal().codigo());
            }
        }

        private void verificarEncerramento(EncerramentoArquivo bloco9) {
            int linha = proximaLinha++;
            int total = linha;
            campos(Registro.R9999, linha, () -> RegistroCodec.valores(bloco9, total));
            if (bloco9 != null && bloco9.quantidadeLinhas() != null && bloco9.quantidadeLinhas().longValue() != total) {
                registrar(linha, "9999", "QTD_LIN", "9999.QTD_LIN divergente do calculado: esperado " + total);
            }
        }
    }
}
