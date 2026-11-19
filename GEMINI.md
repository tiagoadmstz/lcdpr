# lcdpr — Regras e Diretrizes para Gemini / Antigravity

Biblioteca Java para o Livro Caixa Digital do Produtor Rural (LCDPR) da Receita Federal, leiaute 1.3: modelo dos registros (0000 a 9999), geração e leitura do arquivo `|` e do JSON equivalente, validação e cálculo dos saldos do Bloco Q. Publicada no GitHub Packages.

## Módulos e Tecnologias

Três módulos independentes com a mesma API pública (`io.github.tiagoadmstz.lcdpr`):

| Módulo | Java mínimo | Jackson | Sintaxe |
|---|---|---|---|
| `lcdpr-core` | 17 | 3 (`tools.jackson`) | Java 17: `record`, pattern matching em `instanceof`, `switch` com seta |
| `lcdpr-java11` | 11 (até 16) | 2 (`com.fasterxml.jackson`) | Java 11: `var`, `List.of`; classes `final` imutáveis (sem `record`) |
| `lcdpr-java8` | 8 | 2 (`com.fasterxml.jackson`) | Java 8: sem APIs Java 9+ (`List.of`, `readAllBytes`, `isBlank`), sem `var` |

Sem Lombok, modelos imutáveis com builders escritos à mão, nomes de domínio em português do leiaute (decisões D1–D6 do roadmap).

## Regra Fundamental: Independência dos Módulos

- Cada módulo possui **código e testes próprios**, inclusive as amostras em `src/test/resources/samples/`. A duplicação é intencional.
- **Proibido:** criar pasta de código compartilhado, `build-helper` com fontes de outro módulo ou artefato comum.
- **Ordem obrigatória de implementação:**
  1. `lcdpr-core`;
  2. `lcdpr-java11`, rebaixando a sintaxe;
  3. `lcdpr-java8`, rebaixando a sintaxe.
- Os mesmos testes devem existir e passar nos três módulos. A compilação usa `--release` do Java mínimo correspondente.
- Toda alteração na API pública deve ser espelhada nos três módulos simultaneamente.

## Comandos de Build e Ambiente

- Compilação com JDK 25 (`--release` do mínimo de cada módulo), testes executados na JVM alvo configurada no `~/.m2/toolchains.xml`:
  - `core`: testado em JDK 17 e JDK 25
  - `java11`: testado em JDK 11 (`C:\Users\fastw\.jdks\jdk-11.0.33+10`)
  - `java8`: testado em JDK 8
- **Importante:** Nunca alterar `JAVA_HOME` nem o `PATH` do sistema operacional. Os JDKs vêm do toolchains.
- Execução:
  - `./mvnw verify`: valida e roda testes de todos os três módulos.
  - `./mvnw verify -pl lcdpr-core`: apenas o core.
  - `./mvnw test -pl lcdpr-java11`: testes de um módulo específico.

## Skills do Projeto

Localizadas em `.agents/skills/` (também disponíveis em `.claude/skills/`):
- `leiaute-lcdpr` (`.agents/skills/leiaute-lcdpr/SKILL.md`): Regras do leiaute 1.3 da RFB, campos, formatos N/C, registros 0000 a 9999 e regras do Bloco Q.
- `code-style` (`.agents/skills/code-style/SKILL.md`): Padrões de código, tabela de rebaixamento de sintaxe por versão Java, regras de import, imutabilidade, `BigDecimal`.
- `plan-before-execute` (`.agents/skills/plan-before-execute/SKILL.md`): Fluxo planejar no modelo analítico (Gemini Pro) -> executar no rápido (Gemini Flash) -> verificar no analítico.
- `review-guidelines` (`.agents/skills/review-guidelines/SKILL.md`): Checklist de revisão de PRs, diffs, conformidade com o leiaute e paridade entre módulos via `javap`.
- `pull-request` (`.agents/skills/pull-request/SKILL.md`): Formato de título de PR (conventional commits em inglês) e template de descrição em português.

## Fluxo de Trabalho com IA: Planejamento vs Execução

- **Planejamento estruturado primeiro:** Sempre realizar o diagnóstico, análise de impacto e plano passo a passo com um modelo analítico de maior capacidade (Gemini Pro ou modelo equivalente "mais caro"). Proibido executar direto sem planejamento estruturado e fatias com critérios de aceite verificáveis.
- **Execução por etapas:** Após a definição e aprovação do plano, a execução cirúrgica do código, testes e sincronização entre os três módulos deve ser realizada por um modelo mais leve e econômico (Gemini Flash ou subagente rápido).
- **Verificação no modelo analítico:** Antes de dar como concluído, rodar `./mvnw verify` e inspecionar o `git diff` real. Relato de modelo rápido não é prova de sucesso sem verificação.

## Papéis de Agentes (Subagentes)

Quando delegar trabalho para subagentes, utilize os papéis especializados:
- **Arquiteto** (Modelo: Pro): Planeja fatias de roadmap, define API pública e especificações em `docs/specs/`. Não altera código de produção.
- **Implementador Java** (Modelo: Flash): Executa uma fatia por módulo (core -> java11 -> java8) com testes e validação de build real.
- **QA / Testes** (Modelo: Flash): Cria amostras fictícias e casos de teste a partir do manual da RFB ou reproduz bugs nos três módulos.
- **Revisor de Código** (Modelo: Pro): Revisa diffs contra a `main`, checa paridade entre módulos com `javap`, corretude fiscal e estilo. Não edita código.

## Convenções Críticas e Guard-Rails

1. **Sem import com asterisco:** Importações explícitas sempre. Checkstyle `AvoidStarImport` falha a compilação no `validate` se houver asteriscos.
2. **Sem comentários em arquivos de build:** Manter `pom.xml` e workflows sem comentários.
3. **Sem menção a IA:** Nunca incluir referências a IA, Gemini, Claude, prompts ou coautoria em commits, PRs, javadocs, comentários ou código.
4. **Commits:** Conventional commits em inglês, **apenas título**, sem corpo (ex: `feat(writer): add pipe file writer and reader`).
5. **Regras fiscais:** Dúvidas de interpretação do manual da RFB não devem ser supostas; pergunte ao usuário. A fonte primária é `docs/leiaute/manual-de-preenchimento-do-lcdpr-1-3.pdf`.
6. **Dados pessoais e fiscais (LGPD):** Jamais usar CPF, CNPJ, CAEPF, nomes ou contas bancárias reais. Usar sempre dados fictícios ou exemplos do manual.
7. **Credenciais e segredos:** Nunca commitar tokens, senhas ou certificados digitais.
8. **Segurança do Git:** Proibido executar sem aprovação prévia comandos destrutivos como `git stash`, `git reset --hard`, `git checkout --`, `git restore`, `git clean`, `git push --force` ou apagar branches/tags.
9. **Monetário e percentual:** Sempre `BigDecimal` com escala 2 e arredondamento `HALF_UP` onde permitido. Identificadores com zero à esquerda (`CPF`, `COD_CONTA`, etc.) são `String`.
10. **JSON exclusivamente por Jackson:** Nunca montar JSON via concatenação manual de strings.
11. **CHANGELOG.md:** Registrar toda mudança de comportamento ou API na seção `[Não Lançado]`.
