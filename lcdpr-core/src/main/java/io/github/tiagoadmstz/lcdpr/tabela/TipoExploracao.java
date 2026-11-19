package io.github.tiagoadmstz.lcdpr.tabela;

/**
 * Tipo de exploração do imóvel (campo TIPO_EXPLORACAO do registro 0040).
 */
public enum TipoExploracao {

    INDIVIDUAL("1", "Exploração individual (imóvel próprio)"),
    CONDOMINIO("2", "Condomínio"),
    ARRENDADO("3", "Imóvel arrendado"),
    PARCERIA("4", "Parceria"),
    COMODATO("5", "Comodato"),
    OUTROS("6", "Outros");

    private final String codigo;
    private final String descricao;

    TipoExploracao(String codigo, String descricao) {
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
    public static TipoExploracao fromCodigo(String codigo) {
        for (TipoExploracao tipo : values()) {
            if (tipo.codigo.equals(codigo)) {
                return tipo;
            }
        }
        throw new IllegalArgumentException("Código inválido para TipoExploracao: " + codigo);
    }
}
