package io.github.tiagoadmstz.lcdpr;

/**
 * Erro de leitura, escrita, JSON ou cálculo do LCDPR. Quando há número de linha, a mensagem termina com " (linha N)".
 */
public class LcdprException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private final int linha;

    /** Erro sem número de linha. */
    public LcdprException(String mensagem) {
        this(mensagem, 0, null);
    }

    /** Erro na linha indicada; linha 0 significa sem linha. */
    public LcdprException(String mensagem, int linha) {
        this(mensagem, linha, null);
    }

    /** Erro sem número de linha, causado por outra exceção. */
    public LcdprException(String mensagem, Throwable causa) {
        this(mensagem, 0, causa);
    }

    /** Erro na linha indicada, causado por outra exceção. */
    public LcdprException(String mensagem, int linha, Throwable causa) {
        super(linha > 0 ? mensagem + " (linha " + linha + ")" : mensagem, causa);
        this.linha = linha;
    }

    /** Número da linha do arquivo; 0 quando o erro não é de uma linha. */
    public int linha() {
        return linha;
    }
}
