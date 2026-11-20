# Histórico de Alterações

Todas as mudanças notáveis neste projeto serão documentadas neste arquivo.

O formato é baseado em [Keep a Changelog](https://keepachangelog.com/pt-BR/1.0.0/),
e este projeto adere a [Semantic Versioning](https://semver.org/lang/pt-BR/).

## [Não Lançado]

### Adicionado
- Validação de dígito verificador do CAEPF (`LcdprValidator.caepfValido(String)`) e sobrecarga de validação estrita opcional `LcdprValidator.validate(Lcdpr, boolean estritoCaepf)`. O modo padrão (`validate(Lcdpr)`) mantém compatibilidade com exemplos oficiais da RFB com DV sintético. O algoritmo vem de fonte comunitária, não da RFB.
- Validação de coerência entre código de município IBGE (7 dígitos) e a UF declarada nos registros 0030 e 0040 (`LcdprValidator.municipioCoerenteComUf(String, String)`).
- Módulo prático de demonstração `lcdpr-examples` sob o perfil Maven `-Pexamples`, contendo submódulos `example-pure-java` (Java 17+) e `example-pure-java8` (Java 8).

## [2.0.0] - 2026-11-19

### Adicionado
- Três artefatos com a mesma API: `lcdpr-core` (Java 17 ou superior, Jackson 3), `lcdpr-java11` (Java 11 a 16, Jackson 2) e `lcdpr-java8` (Java 8, Jackson 2).
- `LcdprWriter` grava o arquivo texto como `String`, `Writer` ou arquivo com `Charset`. Aplica os formatos do leiaute 1.3, termina cada linha com CRLF, grava o 0045 logo após cada 0040 e calcula o `QTD_LIN`.
- `LcdprReader` lê o arquivo texto como `String`, `Reader` ou arquivo com `Charset`. Cada linha é identificada pelo código do registro, e não pela posição. Erro de leitura sai como `LcdprException`, com o número da linha.
- `LcdprJson` converte o registro para JSON e de volta, com os nomes de campo do manual em minúsculas e compatível com o JSON da 1.x.
- `LcdprValidator` devolve a lista de erros (`ErroValidacao`), com linha, registro, campo e mensagem. Cobre obrigatoriedade, tamanho e formato dos campos, condições do 0040 e do 0045, códigos de imóvel e de conta, período, saldos, `QTD_LIN` e dígito verificador de CPF e CNPJ.
- `LcdprCalculator` recalcula os saldos do Q100 e gera o Q200 cumulativo, sem alterar o registro de entrada.
- Registros imutáveis, com `builder()` em cada tipo e `toBuilder()` para criar uma cópia alterada.
- Tabelas do leiaute (tipo de documento, tipo de lançamento, tipo de exploração e as demais) com conversão estrita: código fora da tabela gera erro.
- Amostras fictícias baseadas nos exemplos do manual, com testes de ida e volta byte a byte.
- Documentação: README, guia de migração da 1.x, guia de contribuição, política de segurança e templates de issue e de pull request.
- Build com Maven Wrapper e toolchains (8, 11, 17 e 25), com CI em JDK 8, 11, 17 e 25.

### Alterado
- **Quebra:** pacote Java e groupId passam de `br.com.dsc.lcdpr` para `io.github.tiagoadmstz.lcdpr`. O artefato único dá lugar a `lcdpr-core`, `lcdpr-java11` e `lcdpr-java8`.
- **Quebra:** o modelo é imutável e não usa Lombok. Para alterar um registro, use `toBuilder()`.
- **Quebra:** classes e enums têm nomes novos, em português do leiaute (`Lancamento`, `DadosCadastrais`, `TipoDocumento`). Veja o guia de migração.
- **Quebra:** `participacao` e o percentual de contraparte são `BigDecimal` (#25). Telefones são `String`, para manter os zeros à esquerda.
- **Quebra:** o `QTD_LIN` é sempre calculado na escrita. O valor informado no modelo é ignorado.
- **Quebra:** o Q200 tem um registro por mês com lançamento, em ordem crescente. A 1.x gerava os 12 meses, inclusive os zerados.
- **Quebra:** erros de validação são devolvidos como lista. Erros de leitura, escrita e de JSON inválido lançam `LcdprException`, em vez de retornar `false` ou `null`.
- **Quebra:** o encoding é parâmetro de leitura e de escrita. A 1.x usava UTF-8 fixo.
- O 0045 pertence ao imóvel: `ImovelRural.terceiros` no modelo e `exploracao_imoveis_rurais` dentro do imóvel no JSON.
- JSON: os nomes de campo são minúsculos, como no manual (`cad_itr`), e todos os valores são texto. A leitura aceita as chaves da 1.x e não diferencia maiúsculas de minúsculas.

### Removido
- **Quebra:** Gson, commons-io, commons-lang3 e commons-collections4. Jackson é a única dependência de produção.
- **Quebra:** `LcdprUtil`, `LcdprPipeConverter`, `WordUtils`, `ExceptionUtil` e a validação por anotação.
- **Quebra:** extensão do `Lcdpr` por herança (`DocumentoLcdpr` e a seleção de subclasses). Para estender, componha.
- `config/layout_v13.json`, os serializadores e desserializadores próprios e a classe `Main`.

### Corrigido
- Um imóvel que aparece mais de uma vez perdia os seus terceiros (0045) na segunda vez, e a linha saía colada à seguinte. Gerar o arquivo também alterava o objeto de entrada.
- As entradas de lançamento eram ignoradas no saldo do Q100.
- A leitura quebrava com valor vazio ou zero (`||` e `|0|`).
- A leitura supunha uma posição fixa para cada registro. Um arquivo com mais de um imóvel rural era lido errado.
- Linhas Q100, Q200 e 9999 eram reconhecidas pelo texto que aparecia em qualquer campo. Um histórico com "Q100", por exemplo, virava um registro.
- O `QTD_LIN` não era calculado e dependia do valor informado pelo usuário.
- A validação podia lançar `NullPointerException` com valor vazio, a checagem de histórico do lançamento nunca disparava, e o resultado da validação do bloco Q era descartado no recálculo.
- Código fora da tabela era aceito em silêncio e trocado por um valor padrão. Tipo de documento 9, por exemplo, virava nota fiscal.
- Valor com mais de duas casas decimais lançava exceção, e valor negativo não era tratado. Agora o valor é arredondado (HALF_UP) e o valor negativo gera erro.
- Erros eram engolidos: a operação retornava `false` ou `null` e imprimia o stack trace. A exportação gravava `LCDPR_<data>.txt` no diretório atual, sem escolha de destino. Agora as falhas lançam `LcdprException` e o arquivo vai para o caminho informado.

## [1.0.2] - 2024-08-01

### Adicionado
- Publicação no GitHub Packages.

## [1.0.0] - 2024-08-02

### Adicionado
- Publicação no GitHub Packages.

[Não Lançado]: https://github.com/tiagoadmstz/lcdpr/compare/v2.0.0...HEAD
[2.0.0]: https://github.com/tiagoadmstz/lcdpr/compare/1.0.2...v2.0.0
[1.0.2]: https://github.com/tiagoadmstz/lcdpr/compare/1.0.0...1.0.2
[1.0.0]: https://github.com/tiagoadmstz/lcdpr/releases/tag/1.0.0
