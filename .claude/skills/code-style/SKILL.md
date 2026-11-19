---
name: code-style
description: Estilo de código do lcdpr (Java nos módulos core/java11/java8, modelo do leiaute, testes, dependências, comentários). Estende a skill global code-style. Usar antes de escrever ou alterar código Java, pom.xml ou teste neste repositório, e como checklist na revisão.
---

# Estilo de código — lcdpr

Esta skill **estende** a skill global `code-style` (`~/.claude/skills/code-style/SKILL.md`):
sem comentários em arquivos de build e CI, nenhum comentário que cite IA ou
agente, nada de ternário aninhado. Na dúvida sobre algo que não está aqui,
seguir o arquivo vizinho e, depois, o `hl7-handler` (`C:\Users\fastw\DEV\hl7-handler`),
que é a referência de estrutura.

Enquanto a F1 do [roadmap](../../../docs/roadmap.md) não terminar, o código
antigo (`src/`, pacote `br.com.dsc.lcdpr`, Java 8 com Lombok) é legado: só
recebe correção pedida, sem adotar estas regras à força.

## Idiomas

| O quê | Idioma |
|---|---|
| Tipos e campos do domínio (registros, enums das tabelas internas, campos do leiaute) | Português do leiaute, em CamelCase: `ImovelRural`, `TipoExploracao`, `valorEntrada` |
| Métodos e tipos técnicos (`read`, `write`, `validate`, `LcdprReader`, `Builder`) | Inglês |
| Javadoc | Português (o público lê o manual da RFB em português) |
| Mensagens de exceção e de erro de validação | Português, citando registro e campo do manual: `"0040.CAEPF obrigatório para exploração individual (linha 4)"` |
| `@DisplayName` dos testes, README, CHANGELOG, CONTRIBUTING, templates do GitHub | Português |
| Nome do método de teste e mensagens de commit | Inglês |

Nomes de registro e campo do manual (`REG`, `COD_IMOVEL`, `VL_ENTRADA`) ficam
como no manual nas chaves JSON, nas mensagens e no descritor de leiaute.
Enums em CamelCase, com constantes em MAIÚSCULAS (`TipoDocumento.NOTA_FISCAL`).

## Os três módulos

Mesma regra do `hl7-handler`: código e testes próprios em cada módulo, sem
pasta compartilhada. A mudança nasce no `lcdpr-core` e é portada, nesta ordem,
para o `lcdpr-java11` e o `lcdpr-java8`, rebaixando a sintaxe:

| Recurso | core (17) | java11 | java8 |
|---|---|---|---|
| `record` | Sim | Não → classe `final` com acessores no formato do record (`codigoImovel()`), `equals`/`hashCode`/`toString` | Não → idem |
| Pattern matching (`instanceof X x`) | Sim | Não → `instanceof` + cast | Não → idem |
| `switch` com seta / expressão | Sim | Não → `switch` clássico ou `if` | Não → idem |
| Text blocks (`"""`) | Sim | Não → concatenação | Não → idem |
| `var` | Sim | Sim | Não |
| `List.of`, `Map.of`, `Set.of` | Sim | Sim | Não → `Collections.unmodifiableList(new ArrayList<>(...))`, `Arrays.asList`, `emptyList` |
| `String.isBlank()`, `strip()`, `repeat()` | Sim | Sim | Não → `trim().isEmpty()`, laço |
| `Files.readString`/`writeString` | Sim | Sim | Não → `new String(Files.readAllBytes(p), cs)`, `Files.write` |
| `Optional.isEmpty()`, `orElseThrow()` sem argumento | Sim | Sim | Não → `!isPresent()`, `orElseThrow(Supplier)` |
| `List.getFirst()` (Java 21) | **Não** | Não | Não |
| Jackson | 3 (`tools.jackson.*`, exceções unchecked) | 2 (`com.fasterxml.jackson.*`) | 2 |

- API pública (tipos, assinaturas, nomes) **idêntica** nos três; só muda a implementação.
- O `--release` barra API nova, mas não barra sintaxe aceita por todas as versões: conferir a tabela.

## Modelo do leiaute

- **Imutável.** Um record (ou classe `final`) por registro, com `builder()`
  escrito à mão; listas expostas com `List.copyOf` (core) ou
  `Collections.unmodifiableList` com cópia defensiva (java11/java8).
  Gerar arquivo, validar ou calcular **nunca** altera o modelo: o cálculo
  devolve um novo objeto.
- Defaults do leiaute ficam no builder (`REG`, `NOME_ESC = "LCDPR"`,
  `COD_VER = "0013"`, `PAIS = "BR"`, `MOEDA = "BRL"`), não em campo mutável.
- **Valores monetários e percentuais:** `BigDecimal` com escala 2, sem `double`
  e sem `new BigDecimal(double)`. Arredondamento explícito (`RoundingMode.HALF_UP`)
  e só onde o manual permite.
- **Datas:** `LocalDate` para `ddMMaaaa` e `YearMonth` para o mês do Q200.
  Formatos em constantes `DateTimeFormatter` únicas, não repetidas a cada método.
- **Identificadores com zero à esquerda** (`CPF`, `CNPJ`, `COD_IMOVEL`, `COD_CONTA`,
  `CEP`, `AGENCIA`, `NUM_CONTA`) são `String`, nunca `Long`/`Integer`.
- **Enums das tabelas internas:** `codigo()` e `descricao()`, e
  `fromCodigo(...)` estrito, que lança exceção para código fora da tabela.
  Nunca cair num default silencioso.
- O formato de cada campo (tipo N/C, tamanho, decimais, obrigatoriedade) vem
  de **um** descritor de leiaute, usado por writer, reader e validador. Não
  espalhar tamanho e formato por classes diferentes.
- Separador `|` e terminador CRLF definidos num lugar só.

## Java

- **Guard clause:** caso inválido primeiro, saída cedo.
- **Sem ternário aninhado.** Um ternário simples é aceito.
- **Sem reflexão** para gerar ou ler o arquivo: a ordem dos campos é explícita.
- **Sem helper de uso único:** fica inline, com comentário curto se a regra não for óbvia.
- **Sem import com asterisco** (nem `import static ...*`); sem nome qualificado
  no corpo; sem import não usado. O Checkstyle (`AvoidStarImport`) barra no `validate`.
- **Exceções:** sem `catch` vazio e sem `printStackTrace`. Erro de leitura sai
  como `LcdprException` com o número da linha e o registro; erro de
  validação sai como lista de resultados, não como exceção.
- **I/O:** a API recebe `Reader`/`Writer`, `InputStream`/`OutputStream` ou
  `Path`, com o `Charset` explícito. Nunca grava no diretório corrente por conta própria.
- **JSON só via Jackson**, com as chaves do manual em minúsculas (`vl_entrada`).
- **Javadoc** em toda classe e método público, curto, citando o registro/campo do manual.

## Comentários

- Só para explicar o **porquê** (regra do manual, contorno, decisão não óbvia),
  citando a seção do manual quando houver.
- Nenhum comentário em `pom.xml`, workflow ou outro arquivo de build.
- Nada de cabeçalho de template de IDE ("To change this license header...").
- Anotação temporária: `// TODO(remove before done): ...`, removida antes de terminar.

## Testes

- JUnit 5 + AssertJ. **Um método por caso**; sem `@ParameterizedTest`.
- Nome do método em inglês (`shouldWriteThirdPartyAfterEachRuralProperty`);
  `@DisplayName` em português.
- Amostras em `src/test/resources/samples/`, partindo dos exemplos de
  preenchimento do manual. Dados sempre fictícios: nada de CPF, CNPJ, nome ou
  conta real. CPF/CNPJ de teste com DV válido, mas inventados
  (os do manual, como `11111111191`, servem).
- Teste de ida e volta: ler a amostra, escrever e comparar byte a byte (CRLF incluso).
- Bug corrigido = teste que o reproduz, nos três módulos.
- Os mesmos testes, com as mesmas amostras, nos três módulos.
- Teste não grava fora de `target/` nem de `@TempDir`.

## Dependências e build

- Versões fixadas no `pom.xml` pai, publicadas há pelo menos duas semanas. Nada de milestone ou RC.
- Só a dependência necessária: Jackson (`jackson-databind`). Datas, texto, I/O e
  regex ficam na JDK; sem `commons-*`, Gson ou Lombok.
- O `lcdpr-java8` só aceita dependência com bytecode major ≤ 52.
- Não alterar o `JAVA_HOME` nem o `java` do `PATH`: os JDKs vêm do `~/.m2/toolchains.xml`.
