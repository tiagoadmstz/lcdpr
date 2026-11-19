package io.github.tiagoadmstz.lcdpr.tabela;

/**
 * Indicador do início do período (campo IND_SIT_INI_PER do registro 0000).
 */
public enum IndicadorInicioPeriodo {

    REGULAR("0", "Regular (início no primeiro dia do ano)"),
    ABERTURA("1", "Abertura (início de atividades no ano-calendário)"),
    INICIO_OBRIGATORIEDADE("2", "Início de obrigatoriedade da escrituração no curso do ano-calendário");

    private final String codigo;
    private final String descricao;

    IndicadorInicioPeriodo(String codigo, String descricao) {
        this.codigo = codigo;
        this.descricao = descricao;
    }

    /** Código do campo no arquivo. */
    public String codigo() {
        return codigo;
    }

    /** Descrição do manual. */
    public String descricao() {
        return descricao;
    }

    /** Localiza a constante pelo código; lança exceção para código fora da tabela ou nulo. */
    public static IndicadorInicioPeriodo fromCodigo(String codigo) {
        for (IndicadorInicioPeriodo indicador : values()) {
            if (indicador.codigo.equals(codigo)) {
                return indicador;
            }
        }
        throw new IllegalArgumentException("Código inválido para IndicadorInicioPeriodo: " + codigo);
    }
}
