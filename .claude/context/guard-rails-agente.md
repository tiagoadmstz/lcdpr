---
title: Guard-rails do agente para este projeto
status: instrução durável - combinada com o usuário para o trabalho no lcdpr (mesmas regras do eirion-platform, adaptadas a uma biblioteca fiscal)
---

# Guard-rails do agente

Regras de comportamento para qualquer sessão ou agente atuando neste
repositório, além da memória pessoal do usuário.

## Sem comentários de andaime no que é entregue

Nada de comentários que só serviam de guia durante a geração (rascunho,
"ajustar depois", raciocínio inline) em código, testes, documentação ou
specs. Um `TODO` pedido pelo usuário é parte do conteúdo e pode ficar.

## Autonomia dentro de tarefa combinada

Dentro de uma tarefa já combinada, não pausar a cada etapa para pedir
"posso prosseguir?". Autonomia cobre decisões de implementação.

Abrir uma pergunta antes de decidir quando houver trade-off de **regra
fiscal**: interpretação do manual da RFB, obrigatoriedade condicional,
cálculo de saldo, arredondamento, encoding ou formato aceito pelo programa
de entrega, ou versão do leiaute. O agente descreve as opções e o impacto, e
espera a resposta. Não supõe a regra. A fonte é o manual em `docs/leiaute/`
(skill `leiaute-lcdpr`).

## Nenhuma ação com risco de dano irreversível ao Git sem aprovação prévia

Exige aprovação explícita antes de executar, mesmo dentro da autonomia:
`git stash`, `git reset --hard`, `git checkout -- <arquivo>`, `git restore`,
`git clean`, `git push --force` (inclusive `--force-with-lease`), `git rebase`,
apagar branch (local ou remoto), apagar tag ou release, fechar PR de outra
pessoa ou do Dependabot, e qualquer comando que descarte ou reescreva working
directory, staging ou histórico.

Preferir sempre a forma só de leitura (`git status`, `git diff`, `git log`,
`git show`). Se não houver, explicar o que o comando faz e esperar. Vale também
para investigação.

## Dados pessoais e fiscais (LGPD)

- Nunca usar dado real de produtor rural, contador ou terceiro em código,
  teste, amostra, log, documentação ou prompt: nome, CPF, CNPJ, CAEPF, CAFIR,
  inscrição estadual, conta bancária, endereço, valores de livro caixa.
- Amostras usam os exemplos fictícios do manual ou dados inventados.
- Se o usuário mandar um arquivo LCDPR real para investigar um bug, reproduzir
  o caso com dados sintéticos antes de virar teste; ao reportar, citar
  `arquivo:linha`, nunca o valor.
- A biblioteca não loga valores de campos.

## Credenciais

- Nunca commitar segredo: token do GitHub Packages, `settings.xml` com senha,
  certificado digital (o LCDPR é assinado com ICP-Brasil; certificado de teste
  também não entra no repositório).
- Segredos ficam em variável de ambiente ou secret do GitHub Actions.
- Antes de commit, procurar segredos no diff. Se achar um já commitado,
  avisar sem reescrever histórico.

## Atribuição

Nunca mencionar IA, Claude, assistente ou ferramenta de geração em commits,
PRs, código, comentários, CHANGELOG ou release notes. O contexto em `.claude/`
é a exceção natural.

## Modelos

Planejar no modelo caro e executar com agentes baratos: skill
`plan-before-execute` do projeto.

## Fluxo de Git deste repositório

- Branch principal: `main` (renomeado de `master` em 2026-11-19).
  Todo PR tem o branch principal como base.
- Commits, branches e PR: skill global `git-workflow` e skill de projeto
  `pull-request`. Em conflito, a skill de projeto prevalece.
- Commit e PR só quando o usuário pedir.
