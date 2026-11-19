package io.github.tiagoadmstz.lcdpr.tabela;

/**
 * Tipo de lançamento do demonstrativo (campo TIPO_LANC do registro Q100).
 */
public enum TipoLancamento {

    RECEITA("1", "Receita da atividade rural"),
    DESPESA_CUSTEIO_INVESTIMENTO("2", "Despesas de custeio e investimentos"),
    RECEITA_ADIANTAMENTO("3", "Receita de produtos entregues no ano referente a adiantamento de recursos financeiros");

    private final String codigo;
    private final String descricao;

    TipoLancamento(String codigo, String descricao) {
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
    public static TipoLancamento fromCodigo(String codigo) {
        for (TipoLancamento tipo : values()) {
            if (tipo.codigo.equals(codigo)) {
                return tipo;
            }
        }
        throw new IllegalArgumentException("Código inválido para TipoLancamento: " + codigo);
    }
}
