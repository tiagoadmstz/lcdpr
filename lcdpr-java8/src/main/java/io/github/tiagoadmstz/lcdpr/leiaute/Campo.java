package io.github.tiagoadmstz.lcdpr.leiaute;

import java.util.Objects;

/**
 * Descritor de um campo do leiaute: nome do manual, formato, tamanho e obrigatoriedade incondicional.
 */
public final class Campo {

    private final String nome;
    private final Formato formato;
    private final int tamanho;
    private final boolean obrigatorio;

    /**
     * Construtor com todos os campos, na ordem declarada.
     */
    public Campo(
            String nome,
            Formato formato,
            int tamanho,
            boolean obrigatorio) {
        this.nome = nome;
        this.formato = formato;
        this.tamanho = tamanho;
        this.obrigatorio = obrigatorio;
    }

    /** Nome do campo no manual, como COD_IMOVEL. */
    public String nome() {
        return nome;
    }

    /** Formato do campo. */
    public Formato formato() {
        return formato;
    }

    /** Tamanho máximo do campo no leiaute. */
    public int tamanho() {
        return tamanho;
    }

    /** Indica se o campo é obrigatório em qualquer caso. */
    public boolean obrigatorio() {
        return obrigatorio;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Campo that = (Campo) o;
        return Objects.equals(nome, that.nome)
                && Objects.equals(formato, that.formato)
                && Objects.equals(tamanho, that.tamanho)
                && Objects.equals(obrigatorio, that.obrigatorio);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nome, formato, tamanho, obrigatorio);
    }

    @Override
    public String toString() {
        return "Campo[nome=" + nome
                + ", formato=" + formato
                + ", tamanho=" + tamanho
                + ", obrigatorio=" + obrigatorio
                + "]";
    }
}
