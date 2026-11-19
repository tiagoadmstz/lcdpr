package io.github.tiagoadmstz.lcdpr;

/**
 * Erro encontrado pelo LcdprValidator.
 *
 * @param linha posição que o registro ocupa no arquivo gerado pelo LcdprWriter; 0 quando o erro não é de um registro
 * @param registro código do registro, como "0040" ou "Q100"; nulo quando o erro não é de um registro
 * @param campo nome do campo no leiaute, como "CAEPF"; nulo quando o erro não é de um campo
 * @param mensagem descrição do erro em português
 */
public record ErroValidacao(int linha, String registro, String campo, String mensagem) {
}
