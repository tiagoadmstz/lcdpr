### Descrição
<!-- Resuma em poucas linhas o que esta alteração faz. -->

### Motivação
<!-- Qual problema está sendo resolvido, ou qual necessidade motivou a alteração? Cite a fase do roadmap, se houver. -->

### Alterações
<!-- Pontos principais, agrupados por tema. Diga se a mudança foi feita nos três módulos (lcdpr-core, lcdpr-java11, lcdpr-java8) e, se não foi, por quê. -->
-

### Validação
<!-- Como você verificou que a alteração funciona: ./mvnw -B verify, JDKs usados, amostras usadas. -->
-

### Como testar
<!-- Só os passos necessários: comando e amostra. -->
N/A

### Observações
<!-- Restrições, quebra de API, dependências novas. Em correção de bug: de onde o bug veio (commit provável, achado com git log ou git blame). (Opcional) -->
N/A

### Links relacionados
<!-- Issue, fase do roadmap, seção do manual, PR dependente, sempre em formato de link. Ex.: [#25](url) -->
-

### Checklist
- [ ] Alteração aplicada em `lcdpr-core`, `lcdpr-java11` e `lcdpr-java8` (ou justificada)
- [ ] Mesmos testes e mesmas amostras nos três módulos, passando com `./mvnw -B verify`
- [ ] API pública idêntica nos três módulos (ou diferença justificada)
- [ ] Regra fiscal nova cita a seção do manual em `docs/leiaute/`, ou a decisão do mantenedor está registrada
- [ ] Amostras e exemplos só com dados fictícios
- [ ] `CHANGELOG.md` atualizado em `[Não Lançado]` (se muda comportamento ou API)
- [ ] README, guia de migração ou `docs/roadmap.md` atualizados (se mudou o uso)
- [ ] Dependências novas publicadas há pelo menos duas semanas
- [ ] Sem coautoria nem linhas de atribuição no título, no corpo nem nos commits
