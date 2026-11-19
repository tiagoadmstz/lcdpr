package io.github.tiagoadmstz.lcdpr.leiaute;

import java.util.List;

/**
 * Registros do leiaute 1.3, com o código do REG e os campos na ordem do arquivo (sem o REG).
 */
public enum Registro {

    /** Registro 0000: abertura do arquivo e identificação da pessoa física. */
    R0000("0000", List.of(
            new Campo("NOME_ESC", Formato.TEXTO, 5, true),
            new Campo("COD_VER", Formato.TEXTO, 4, true),
            new Campo("CPF", Formato.NUMERO_EXATO, 11, true),
            new Campo("NOME", Formato.TEXTO, 255, true),
            new Campo("IND_SIT_INI_PER", Formato.NUMERO_EXATO, 1, true),
            new Campo("SIT_ESPECIAL", Formato.NUMERO_EXATO, 1, true),
            new Campo("DT_SIT_ESP", Formato.DATA, 8, false),
            new Campo("DT_INI", Formato.DATA, 8, true),
            new Campo("DT_FIN", Formato.DATA, 8, true))),

    /** Registro 0010: parâmetros de tributação. */
    R0010("0010", List.of(
            new Campo("FORMA_APUR", Formato.NUMERO_EXATO, 1, true))),

    /** Registro 0030: dados cadastrais do contribuinte. */
    R0030("0030", List.of(
            new Campo("ENDERECO", Formato.TEXTO, 150, true),
            new Campo("NUM", Formato.TEXTO, 6, true),
            new Campo("COMPL", Formato.TEXTO, 50, false),
            new Campo("BAIRRO", Formato.TEXTO, 50, true),
            new Campo("UF", Formato.TEXTO, 2, true),
            new Campo("COD_MUN", Formato.TEXTO, 7, true),
            new Campo("CEP", Formato.TEXTO, 8, true),
            new Campo("NUM_TEL", Formato.NUMERO, 15, false),
            new Campo("EMAIL", Formato.TEXTO, 115, true))),

    /** Registro 0040: cadastro dos imóveis rurais. */
    R0040("0040", List.of(
            new Campo("COD_IMOVEL", Formato.NUMERO_EXATO, 3, true),
            new Campo("PAIS", Formato.TEXTO, 2, true),
            new Campo("MOEDA", Formato.TEXTO, 3, true),
            new Campo("CAD_ITR", Formato.NUMERO_EXATO, 8, false),
            new Campo("CAEPF", Formato.NUMERO_EXATO, 14, false),
            new Campo("INSCR_ESTADUAL", Formato.NUMERO, 14, false),
            new Campo("NOME_IMOVEL", Formato.TEXTO, 50, true),
            new Campo("ENDERECO", Formato.TEXTO, 150, true),
            new Campo("NUM", Formato.TEXTO, 6, false),
            new Campo("COMPL", Formato.TEXTO, 50, false),
            new Campo("BAIRRO", Formato.TEXTO, 50, true),
            new Campo("UF", Formato.TEXTO, 2, false),
            new Campo("COD_MUN", Formato.TEXTO, 7, false),
            new Campo("CEP", Formato.TEXTO, 8, false),
            new Campo("TIPO_EXPLORACAO", Formato.NUMERO_EXATO, 1, true),
            new Campo("PARTICIPACAO", Formato.PERCENTUAL, 5, true))),

    /** Registro 0045: cadastro de terceiros que exploram o imóvel em conjunto. */
    R0045("0045", List.of(
            new Campo("COD_IMOVEL", Formato.NUMERO_EXATO, 3, true),
            new Campo("TIPO_CONTRAPARTE", Formato.NUMERO_EXATO, 1, true),
            new Campo("ID_CONTRAPARTE", Formato.CPF_CNPJ, 14, true),
            new Campo("NOME_CONTRAPARTE", Formato.TEXTO, 50, true),
            new Campo("PERC_CONTRAPARTE", Formato.PERCENTUAL, 5, true))),

    /** Registro 0050: contas bancárias. */
    R0050("0050", List.of(
            new Campo("COD_CONTA", Formato.NUMERO_EXATO, 3, true),
            new Campo("PAIS_CTA", Formato.TEXTO, 3, true),
            new Campo("BANCO", Formato.NUMERO_EXATO, 3, false),
            new Campo("NOME_BANCO", Formato.TEXTO, 30, true),
            new Campo("AGENCIA", Formato.NUMERO_EXATO, 4, true),
            new Campo("NUM_CONTA", Formato.NUMERO_EXATO, 16, true))),

    /** Registro Q100: lançamento do demonstrativo do resultado da atividade rural. */
    RQ100("Q100", List.of(
            new Campo("DATA", Formato.DATA, 8, true),
            new Campo("COD_IMOVEL", Formato.NUMERO_EXATO, 3, true),
            new Campo("COD_CONTA", Formato.NUMERO_EXATO, 3, true),
            new Campo("NUM_DOC", Formato.TEXTO, 255, false),
            new Campo("TIPO_DOC", Formato.NUMERO_EXATO, 1, true),
            new Campo("HIST", Formato.TEXTO, 255, true),
            new Campo("ID_PARTIC", Formato.CPF_CNPJ, 14, true),
            new Campo("TIPO_LANC", Formato.NUMERO_EXATO, 1, true),
            new Campo("VL_ENTRADA", Formato.VALOR, 19, true),
            new Campo("VL_SAIDA", Formato.VALOR, 19, true),
            new Campo("SLD_FIN", Formato.VALOR, 19, true),
            new Campo("NAT_SLD_FIN", Formato.TEXTO, 1, true))),

    /** Registro Q200: resumo mensal do demonstrativo. */
    RQ200("Q200", List.of(
            new Campo("MES", Formato.MES, 6, true),
            new Campo("VL_ENTRADA", Formato.VALOR, 19, true),
            new Campo("VL_SAIDA", Formato.VALOR, 19, true),
            new Campo("SLD_FIN", Formato.VALOR, 19, true),
            new Campo("NAT_SLD_FIN", Formato.TEXTO, 1, true))),

    /** Registro 9999: signatário e encerramento do arquivo. */
    R9999("9999", List.of(
            new Campo("IDENT_NOM", Formato.TEXTO, 255, false),
            new Campo("IDENT_CPF_CNPJ", Formato.CPF_CNPJ, 14, false),
            new Campo("IND_CRC", Formato.TEXTO, 255, false),
            new Campo("EMAIL", Formato.TEXTO, 115, false),
            new Campo("FONE", Formato.NUMERO, 15, false),
            new Campo("QTD_LIN", Formato.NUMERO, 30, true)));

    private final String codigo;
    private final List<Campo> campos;

    Registro(String codigo, List<Campo> campos) {
        this.codigo = codigo;
        this.campos = campos;
    }

    /** Código do REG no arquivo, como "0040" ou "Q100". */
    public String codigo() {
        return codigo;
    }

    /** Campos do registro na ordem do arquivo, sem o REG; lista imutável. */
    public List<Campo> campos() {
        return campos;
    }

    /** Localiza o registro pelo código; lança exceção para código desconhecido ou nulo. */
    public static Registro fromCodigo(String codigo) {
        for (Registro registro : values()) {
            if (registro.codigo.equals(codigo)) {
                return registro;
            }
        }
        throw new IllegalArgumentException("Registro desconhecido: " + codigo);
    }
}
