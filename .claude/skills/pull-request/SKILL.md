---
name: pull-request
description: Como escrever título e descrição de pull request no lcdpr. Usar ao abrir um PR, ao escrever ou revisar a descrição de um PR, ou ao preparar o texto de uma release.
---

# Pull request — lcdpr

Quando existir `.github/pull_request_template.md` (entra na F7 do roadmap), a
descrição segue exatamente as seções dele. Até lá, usar as seções abaixo.

## Título

- Formato do commit: `tipo(escopo opcional): resumo no imperativo`, em inglês,
  minúsculo depois dos dois-pontos, sem ponto final.
- Tipos: `feat`, `fix`, `refactor`, `test`, `docs`, `chore`, `build`, `ci`, `perf`, `style`, `revert`.
- Escopos úteis: `model`, `writer`, `reader`, `json`, `validation`, `calc`, `build`, `ci`, `ai`.
  Exemplo: `fix(writer): keep third parties when a rural property repeats`.

## Descrição

Em **português**, em tópicos curtos, começando direto em `### Descrição`:

```
### Descrição
### Alterações
### Validação
### Como testar
### Observações
### Links relacionados
```

- **Alterações:** pontos principais agrupados por tema; dizer se a mudança foi
  feita nos três módulos (ou por que não).
- **Validação:** o que rodou e onde (`./mvnw verify`, JDK 8, 11, 17 e 25; amostra usada).
- **Como testar:** passos mínimos, com a amostra ou o comando.
- **Links:** em markdown (`- [#25](url)`), nunca URL solta. Referenciar a issue
  e a fase do roadmap.
- Seção sem conteúdo: `N/A`. Até ~3000 caracteres.

## PR de correção de bug

Em **Observações**, explicar de onde o bug veio (o que mudou, quando, por quê),
apontando o commit provável achado com `git log`/`git blame`.

## Commits do PR

- Título só, sem corpo; nenhuma menção a IA ou coautoria.
- Poucos commits, agrupados por tema.
