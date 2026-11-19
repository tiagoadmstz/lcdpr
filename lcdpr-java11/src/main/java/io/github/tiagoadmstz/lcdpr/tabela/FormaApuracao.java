package io.github.tiagoadmstz.lcdpr.tabela;

/**
 * Forma de apuração do resultado da atividade rural (campo FORMA_APUR do registro 0010).
 */
public enum FormaApuracao {

    LIVRO_CAIXA("1", "Livro Caixa"),
    VINTE_POR_CENTO_RECEITA_BRUTA("2", "Apuração pelo art. 5º da Lei nº 8.023/1990 (20% da receita bruta)");

    private final String codigo;
    private final String descricao;

    FormaApuracao(String codigo, String descricao) {
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
    public static FormaApuracao fromCodigo(String codigo) {
        for (FormaApuracao forma : values()) {
            if (forma.codigo.equals(codigo)) {
                return forma;
            }
        }
        throw new IllegalArgumentException("Código inválido para FormaApuracao: " + codigo);
    }
}
