package io.github.tiagoadmstz.lcdpr.leiaute;

/**
 * Descritor de um campo do leiaute: nome do manual, formato, tamanho e obrigatoriedade incondicional.
 */
public record Campo(String nome, Formato formato, int tamanho, boolean obrigatorio) {
}
