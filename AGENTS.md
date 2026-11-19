# lcdpr — Instruções para agentes de IA

Biblioteca Java para o Livro Caixa Digital do Produtor Rural (LCDPR) da
Receita Federal, leiaute 1.3: modelo dos registros, geração e leitura do
arquivo `|` e do JSON, validação e cálculo dos saldos do Bloco Q.

O contexto completo está em [`CLAUDE.md`](CLAUDE.md), e as regras
detalhadas estão nas skills em `.claude/skills/`. Ler antes de agir:

| Situação | Arquivo |
|---|---|
| Qualquer tarefa | `CLAUDE.md` e `.claude/context/guard-rails-agente.md` |
| Registro, writer, reader, validação, cálculo | `.claude/skills/leiaute-lcdpr/SKILL.md` |
| Escrever ou alterar código, pom ou teste | `.claude/skills/code-style/SKILL.md` |
| Revisar diff, branch ou PR | `.claude/skills/review-guidelines/SKILL.md` |
| Abrir PR ou escrever release notes | `.claude/skills/pull-request/SKILL.md` |
| Tarefa não trivial | `.claude/skills/plan-before-execute/SKILL.md` |
| Planejamento | `docs/roadmap.md` e `docs/specs/` |

Regras que não podem escapar:

- Três módulos independentes (`lcdpr-core` 17, `lcdpr-java11`, `lcdpr-java8`)
  com a mesma API pública; toda mudança nos três, nessa ordem.
- Planejar com modelo avançado, executar com modelo econômico, verificar
  build e diff antes de relatar.
- Nenhum dado real de produtor, contador ou terceiro; nenhum segredo no código.
- Regra fiscal fora do manual: perguntar, não supor.
- Commits em conventional commits, só título, sem menção a IA.
- Não alterar `JAVA_HOME` nem o `PATH`; os JDKs vêm do `~/.m2/toolchains.xml`.
