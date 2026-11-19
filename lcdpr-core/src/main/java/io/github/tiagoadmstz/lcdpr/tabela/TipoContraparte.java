package io.github.tiagoadmstz.lcdpr.tabela;

/**
 * Tipo de contraparte que explora o imóvel em conjunto (campo TIPO_CONTRAPARTE do registro 0045).
 */
public enum TipoContraparte {

    CONDOMINO("1", "Condômino"),
    ARRENDADOR("2", "Arrendador"),
    PARCEIRO("3", "Parceiro"),
    COMODANTE("4", "Comodante"),
    OUTRO("5", "Outro");

    private final String codigo;
    private final String descricao;

    TipoContraparte(String codigo, String descricao) {
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
    public static TipoContraparte fromCodigo(String codigo) {
        for (TipoContraparte tipo : values()) {
            if (tipo.codigo.equals(codigo)) {
                return tipo;
            }
        }
        throw new IllegalArgumentException("Código inválido para TipoContraparte: " + codigo);
    }
}
