package io.github.tiagoadmstz.lcdpr.tabela;

/**
 * Indicador de situação especial e outros eventos (campo SIT_ESPECIAL do registro 0000).
 */
public enum SituacaoEspecial {

    NORMAL("0", "Normal (sem ocorrência de situação especial ou evento)"),
    FALECIMENTO("1", "Falecimento"),
    ESPOLIO("2", "Espólio"),
    SAIDA_DEFINITIVA_PAIS("3", "Saída definitiva do país");

    private final String codigo;
    private final String descricao;

    SituacaoEspecial(String codigo, String descricao) {
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
    public static SituacaoEspecial fromCodigo(String codigo) {
        for (SituacaoEspecial situacao : values()) {
            if (situacao.codigo.equals(codigo)) {
                return situacao;
            }
        }
        throw new IllegalArgumentException("Código inválido para SituacaoEspecial: " + codigo);
    }
}
