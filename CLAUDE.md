# CLAUDE.md

Este arquivo orienta o Claude Code a trabalhar neste repositório.

## O que é

**lcdpr**: biblioteca Java para o Livro Caixa Digital do Produtor Rural
(LCDPR) da Receita Federal, leiaute 1.3. Modela os registros (0000 a 9999),
gera e lê o arquivo texto separado por `|` e o JSON equivalente, valida e
calcula os saldos do Bloco Q. Publicada no GitHub Packages.

## Estado atual × alvo

- **Hoje** (`src/`, versão 2.0.0 no pom): módulo único em Java 8, pacote
  `br.com.dsc.lcdpr`, Lombok, modelos mutáveis e bugs conhecidos. É legado:
  só recebe correção pedida.
- **Alvo 3.0** ([roadmap](docs/roadmap.md)): três artefatos com a mesma API,
  no molde do `hl7-handler` (`C:\Users\fastw\DEV\hl7-handler`):

| Módulo | Java mínimo | Jackson | Sintaxe |
|---|---|---|---|
| `lcdpr-core` | 17 | 3 (`tools.jackson`) | `record`, pattern matching, `switch` com seta |
| `lcdpr-java11` | 11 | 2 (`com.fasterxml.jackson`) | `var`, `List.of`; sem `record` |
| `lcdpr-java8` | 8 | 2 | sem `var`, `List.of`, `isBlank` etc. |

Pacote e groupId `io.github.tiagoadmstz.lcdpr`, sem Lombok, modelos
imutáveis, nomes de domínio em português do leiaute (decisões D1–D4 do roadmap).

## Regra principal: módulos independentes

- Cada módulo tem código e testes próprios, inclusive as amostras. A duplicação é intencional.
- Sem pasta de código compartilhado, `build-helper` com fontes de outro módulo
  ou artefato comum.
- Toda correção ou recurso: `lcdpr-core` → `lcdpr-java11` → `lcdpr-java8`,
  rebaixando a sintaxe, com os mesmos testes passando nos três.
- Mudança de API pública vale para os três ao mesmo tempo.

## Build

- Hoje: `mvn -B test` (Maven do PATH, JDK 25 compilando com `source/target 1.8`).
  Os testes gravam `LCDPR_03072020.*` na raiz (ignorados pelo `.gitignore`).
- Alvo (F1): `./mvnw verify` compila com o JDK 25 (`--release` do mínimo de
  cada módulo) e roda os testes na JVM do módulo (core em 17 e 25, java11 em
  11, java8 em 8), via `~/.m2/toolchains.xml` (8, 11, 17 e 25 já configurados).
- **Não alterar `JAVA_HOME` nem o `java` do `PATH`.**

## Como trabalhar aqui

- Responder em **português (pt-BR)**, de forma direta.
- **Planejar no modelo caro e executar com agentes baratos**: skill
  [`plan-before-execute`](.claude/skills/plan-before-execute/SKILL.md).
- Regras de comportamento (Git, dados fiscais, credenciais, regra fiscal em
  aberto): [`.claude/context/guard-rails-agente.md`](.claude/context/guard-rails-agente.md).
- Valem as skills globais `code-style`, `git-workflow` e `plan-before-execute`,
  estendidas pelas do projeto. **Não** valem convenções do AGHU.
- Branch principal: **`master`**, com o fluxo da skill global `git-workflow`.
  Commit e PR só quando pedido.

### Skills do projeto (`.claude/skills/`)

Ler a skill **antes** de agir, mesmo que não tenha sido carregada sozinha:

| Skill | Quando |
|---|---|
| `plan-before-execute` | Toda tarefa não trivial: níveis de modelo e fluxo planejar → executar → verificar |
| `leiaute-lcdpr` | Qualquer trabalho em registro, writer, reader, validação ou cálculo |
| `code-style` | Antes de escrever ou revisar código, pom ou teste (estende a global) |
| `review-guidelines` | Revisar diff, branch ou PR |
| `pull-request` | Abrir ou descrever PR, ou escrever release notes |

### Agents do projeto (`.claude/agents/`)

| Agent | Modelo | Papel |
|---|---|---|
| `arquiteto` | opus | Planeja uma fase, fatia e escreve a spec |
| `revisor-codigo` | opus | Revisa o diff: paridade, API, leiaute, estilo |
| `implementador-java` | haiku | Executa uma fatia num módulo, com testes |
| `qa-testes` | haiku | Testes de regressão e amostras do manual |

## Convenções (resumo; detalhe na `code-style`)

- Conventional commits, **só o título**, sem corpo, sem coautoria nem menção a IA.
- Sem comentários em `pom.xml` e arquivos de build; em Java, só o porquê.
- Sem ternário aninhado; sem import com asterisco.
- Dinheiro e percentual em `BigDecimal`; CPF, CNPJ e códigos em `String`.
- Dependências com versão publicada há pelo menos duas semanas.
- `CHANGELOG.md` (a partir da F7) registra toda mudança de comportamento ou API em `[Não Lançado]`.

## Referências

- Manual, leiaute e tutorial da RFB: `_arquivos/` (vai para `docs/leiaute/` na F1).
- [Roadmap](docs/roadmap.md): diagnóstico, decisões, perguntas em aberto e fases.
- [Specs](docs/specs/README.md): detalhe de cada fase.
