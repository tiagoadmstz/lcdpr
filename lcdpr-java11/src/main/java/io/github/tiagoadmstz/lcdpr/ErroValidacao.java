package io.github.tiagoadmstz.lcdpr;

import java.util.Objects;

/**
 * Erro encontrado pelo LcdprValidator.
 *
 * @param linha posição que o registro ocupa no arquivo gerado pelo LcdprWriter; 0 quando o erro não é de um registro
 * @param registro código do registro, como "0040" ou "Q100"; nulo quando o erro não é de um registro
 * @param campo nome do campo no leiaute, como "CAEPF"; nulo quando o erro não é de um campo
 * @param mensagem descrição do erro em português
 */
public final class ErroValidacao {

    private final int linha;
    private final String registro;
    private final String campo;
    private final String mensagem;

    /**
     * Construtor com todos os campos, na ordem declarada.
     */
    public ErroValidacao(
            int linha,
            String registro,
            String campo,
            String mensagem) {
        this.linha = linha;
        this.registro = registro;
        this.campo = campo;
        this.mensagem = mensagem;
    }

    /** Posição que o registro ocupa no arquivo gerado pelo LcdprWriter; 0 quando o erro não é de um registro. */
    public int linha() {
        return linha;
    }

    /** Código do registro, como "0040" ou "Q100"; nulo quando o erro não é de um registro. */
    public String registro() {
        return registro;
    }

    /** Nome do campo no leiaute, como "CAEPF"; nulo quando o erro não é de um campo. */
    public String campo() {
        return campo;
    }

    /** Descrição do erro em português. */
    public String mensagem() {
        return mensagem;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ErroValidacao that = (ErroValidacao) o;
        return Objects.equals(linha, that.linha)
                && Objects.equals(registro, that.registro)
                && Objects.equals(campo, that.campo)
                && Objects.equals(mensagem, that.mensagem);
    }

    @Override
    public int hashCode() {
        return Objects.hash(linha, registro, campo, mensagem);
    }

    @Override
    public String toString() {
        return "ErroValidacao[linha=" + linha
                + ", registro=" + registro
                + ", campo=" + campo
                + ", mensagem=" + mensagem
                + "]";
    }
}
