package io.github.tiagoadmstz.lcdpr.tabela;

/**
 * Tipo de documento do lançamento (campo TIPO_DOC do registro Q100).
 */
public enum TipoDocumento {

    NOTA_FISCAL("1", "Nota fiscal"),
    FATURA("2", "Fatura"),
    RECIBO("3", "Recibo"),
    CONTRATO("4", "Contrato"),
    FOLHA_PAGAMENTO("5", "Folha de pagamento"),
    OUTROS("6", "Outros");

    private final String codigo;
    private final String descricao;

    TipoDocumento(String codigo, String descricao) {
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
    public static TipoDocumento fromCodigo(String codigo) {
        for (TipoDocumento tipo : values()) {
            if (tipo.codigo.equals(codigo)) {
                return tipo;
            }
        }
        throw new IllegalArgumentException("Código inválido para TipoDocumento: " + codigo);
    }
}
