package io.github.tiagoadmstz.lcdpr.leiaute;

/**
 * Formato de um campo do leiaute: texto, numérico, data, mês, valor monetário ou percentual.
 */
public enum Formato {
    TEXTO,
    NUMERO,
    NUMERO_EXATO,
    CPF_CNPJ,
    DATA,
    MES,
    VALOR,
    PERCENTUAL
}
