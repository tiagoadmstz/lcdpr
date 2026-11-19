# Roadmap: lcdpr 3.0

Estado da biblioteca em 2026-11-19 e plano para a versão multi-JDK, no
mesmo formato do `hl7-handler`.

## Decisões tomadas

| # | Decisão | Data |
|---|---|---|
| D1 | Pacote `io.github.tiagoadmstz.lcdpr` e groupId `io.github.tiagoadmstz.lcdpr`. Quebra a API, por isso a próxima versão é major. | 2026-11-19 |
| D2 | Sem Lombok. Modelos imutáveis com builder escrito à mão: `record` no core e classe `final` no java11 e no java8. | 2026-11-19 |
| D3 | Nomes de domínio em português, seguindo o leiaute da RFB, em CamelCase (`TipoDocumento`, não `TIPO_DOCUMENTO`). Métodos técnicos em inglês (`read`, `write`, `validate`). | 2026-11-19 |
| D4 | Três artefatos com a mesma API: `lcdpr-core` (Java 17, Jackson 3), `lcdpr-java11` e `lcdpr-java8` (Jackson 2). Mesmas regras de independência do `hl7-handler`. | 2026-11-19 |

## Perguntas em aberto

| # | Pergunta | Impacto |
|---|---|---|
| P1 | O Q200 lista só os meses com lançamento ou sempre os 12 meses do período? Hoje o código gera os 12, inclusive os zerados. | Cálculo do Q200 e `QTD_LIN` |
| P2 | Que encoding o programa da RFB aceita no arquivo: UTF-8 ou ISO-8859-1? Hoje é UTF-8, e a fonte é o tutorial de entrega em `_arquivos/`. | Writer e reader |
| P3 | O leiaute 1.3 (Ato Declaratório Executivo COPES nº 1/2020) continua vigente, ou saiu versão nova? | `COD_VER` e regras |
| P4 | O `pom.xml` diz 2.0.0, mas só existem as tags 1.0.0 e 1.0.2. A próxima versão sai como 2.0.0 ou como 3.0.0? Por ora, `3.0.0-SNAPSHOT` (só a propriedade `revision` muda). | Número da release |
| P5 | O branch principal passa de `master` para `main`, como no `hl7-handler` e no Eirion? Por ora, fica `master`. | CI e skills |

## Diagnóstico

### Bugs confirmados

Saída real de `mvn test` em 2026-11-19, com o `DummyData` atual:

1. **Segundo 0040 colado no 0050, sem o 0045.** A geração emitiu
   `...|2|050000050|001|BR|...`. `LcdprPipeConverter.splitImoveisRuraisTerceiros`
   apaga os terceiros do próprio objeto (`setCadastroTerceiros(null)`).
   Na segunda vez que o mesmo imóvel aparece, ele já está sem terceiros,
   e o `replace("|\r\n", "")` remove a quebra de linha. Gerar o arquivo
   muda o modelo.
2. **Entradas ignoradas no saldo do Q100.** `DemonstrativoLivroCaixa.calculate`
   só soma quando `valorSaida == null`, mas o default é `0`. Por isso
   toda linha cai no `subtract`.
3. **Importação quebra com valor vazio ou `0`.** O manual aceita `||` e
   `|0|`, mas `BigDecimalUtil.stringToBigDecimal` ignora o `scale` e usa
   `length() - 2`. Com isso, `"0"` e `""` estouram `StringIndexOutOfBoundsException`.
4. **Importação por posição de linha.** `AberturaIdentificacao.buildFromLinesList`
   supõe que a linha 3 é o único 0040. O 0045 está comentado e o 0050 é
   lido na linha 5. Um arquivo com dois imóveis é lido errado.
5. **Filtro por `contains`.** As linhas Q100, Q200 e 9999 são achadas com
   `line.contains("Q100")` etc., então um histórico, telefone ou conta com
   esse texto vira registro.
6. **`QTD_LIN` não é calculado**: o usuário informa o valor (o teste usa 8007).
7. **Validação quebrada.** Em `DemoLivroCaixa.validate`, o `compareTo` em
   valor `null` dá NPE, e `entryDescription() == null` nunca é verdadeiro
   (devia checar o `historico`). `validateByAnnotation` só cobre o 0000
   e, em campo sem `@Validation`, engole um NPE e devolve `false`.
   Em `Lcdpr.recalculateBlockQ`, o resultado de `blocoQ.validate()` é
   descartado.
8. **Enum inválido aceito em silêncio.** O `getEnum` cai no default
   (`TIPO_DOCUMENTO` 9 vira `NOTA_FISCAL`).
9. **`bigDecimalToStringAndRemovePuncts`** usa `setScale(2)` sem arredondamento
   (exceção com 3 casas) e não trata valor negativo.
10. **Exceções engolidas.** `ExceptionUtil.tryCatch` devolve `false`/`null`
    e imprime o stack trace. A exportação grava `LCDPR_<data>.txt` no
    diretório corrente, sem escolha de destino.

### Evoluções inacabadas

| Item | Onde | Destino na 3.0 |
|---|---|---|
| Validação guiada por leiaute (`config/layout_v13.json` + `@Validation`) | commit `c6854f2` (2020) | Vira descritor de leiaute em código, usado por writer, reader e validador (F2/F4). O JSON sai. |
| Importação do 0045 | `AberturaIdentificacao:76` comentado | Reader por `REG`, com hierarquia 0040 → 0045 (F3) |
| Teste de importação txt × json | `LcdprTest.importLcdprTest` sem `@Test` | Teste de ida e volta com os exemplos do manual (F3) |
| Recálculo do Q100/Q200 (`TODO: refazer método`) | `DemonstrativoLivroCaixa` | Calculadora de saldos separada do modelo (F5) |
| Percentual como `BigDecimal` | issue #25 | `PARTICIPACAO` e `PERC_CONTRAPARTE` como `BigDecimal` com 2 casas (F2) |
| Subclasse de `Lcdpr` (`DocumentoLcdpr`, `filterSubClasses`) | branch `json-age` | Sai: com modelo imutável, o consumidor compõe em vez de herdar |

### Lixo

- Dependências sem uso ou que a JDK resolve: `gson` (sem uso), `commons-io`,
  `commons-lang3`, `jackson-core`/`jackson-annotations` declarados à parte,
  `junit-jupiter-api` `5.11.0-M2` (milestone).
- Código morto: `main/Main.java` (main privado vazio), `WordUtils`
  (só `lpad` é usado), `ExceptionFunction`/`ExceptionUtil`, `annotations/Validation`
  e o pacote redundante `lcdpr.lcdpr`.
- Cabeçalho "To change this license header" do NetBeans em vários arquivos.
- Comentários `//t = 4, o = sim` que repetem o manual: o leiaute vira código.
- `LCDPR.iml` versionado; `config/` sai junto com o JSON de leiaute.
- Branches remotos: `dev`, `json-age` e `release-2.0.0` já estão no
  `master`. `release-1.0.0` e `release-1.0.2` têm versões dos mesmos commits
  de workflow. Os PRs #33 e #34 (Dependabot) ficam obsoletos com a reescrita.
  Apagar só com aprovação (guard-rails).
- README com JSON de exemplo divergente do código e texto com espaços quebrados.
- Workflow `maven-publish.yml` só com JDK 8, sem CI de PR.

## Fases

Cada fase vira um PR. As fases F2 a F5 são escritas no `lcdpr-core` e
portadas na F6.

| Fase | Entrega | Depende de |
|---|---|---|
| F0 | Contexto de IA (`CLAUDE.md`, `AGENTS.md`, `.claude/`) e este roadmap | — |
| F1 | Estrutura multi-módulo: pom pai com `${revision}`, `mvnw`, toolchains, Checkstyle, flatten, enforcer, `ci.yml` (JDK 8/11/17/25), `release.yml` por tag, `.editorconfig`, `.gitattributes`, Dependabot. Remove `src/` antigo, `config/`, `LCDPR.iml`. Move `_arquivos/` para `docs/leiaute/`. | F0, P4, P5 |
| F2 | Modelo no core: records por registro, enums das tabelas internas com `fromCodigo` estrito, descritor de leiaute por campo (tipo N/C, tamanho, decimais, obrigatoriedade) | F1, P3 |
| F3 | `LcdprWriter` (pipe, CRLF, formatos do manual, 0045 após cada 0040, `QTD_LIN` calculado, sem mutar o modelo), `LcdprReader` (por `REG`, campos vazios, erro com número da linha) e JSON com as chaves do manual | F2, P2 |
| F4 | Validação que devolve a lista de erros: obrigatórios, tamanhos, tipos, condicionais (CAEPF, 0045 se participação < 100% ou tipo ≠ 1, `CAD_ITR`/`BANCO` se país BR), referências de `COD_IMOVEL`/`COD_CONTA` (aceitando `000` e `999`), datas dentro do período, entrada e saída exclusivas, DV de CPF/CNPJ | F3 |
| F5 | Cálculo do saldo do Q100 e do Q200 cumulativo, com a natureza do saldo | F3, P1 |
| F6 | Porte para `lcdpr-java11` e `lcdpr-java8`, com os mesmos testes e checagem de API idêntica | F2–F5 |
| F7 | README, CHANGELOG, CONTRIBUTING, SECURITY, templates do GitHub, guia de migração 2.x → 3.0 e release | F6 |

Testes de referência: os exemplos de preenchimento do manual (seção 3.5)
viram amostras em `src/test/resources/samples/`, com ida e volta
(ler → escrever → comparar) e cada bug acima reproduzido por um teste.
