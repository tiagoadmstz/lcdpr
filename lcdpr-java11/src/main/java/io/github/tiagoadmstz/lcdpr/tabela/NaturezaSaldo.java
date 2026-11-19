package io.github.tiagoadmstz.lcdpr.tabela;

/**
 * Natureza do saldo final (campos NAT_SLD_FIN dos registros Q100 e Q200).
 */
public enum NaturezaSaldo {

    POSITIVO("P", "Positivo"),
    NEGATIVO("N", "Negativo");

    private final String codigo;
    private final String descricao;

    NaturezaSaldo(String codigo, String descricao) {
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
    public static NaturezaSaldo fromCodigo(String codigo) {
        for (NaturezaSaldo natureza : values()) {
            if (natureza.codigo.equals(codigo)) {
                return natureza;
            }
        }
        throw new IllegalArgumentException("Código inválido para NaturezaSaldo: " + codigo);
    }
}
