---
name: plan-before-execute
description: Regra do lcdpr para qualquer tarefa não trivial - planejar primeiro num modelo analítico/avançado (Gemini Pro / Opus), executar com agentes mais rápidos/econômicos (Gemini Flash / Haiku) e verificar no modelo analítico. Use antes de implementar, migrar, portar entre módulos, refatorar ou escrever testes em lote.
---

# Planejar no modelo caro, executar no barato

## Níveis de modelo

| Nível | Modelos sugeridos | Papel / Quem usa |
|---|---|---|
| Planejamento e julgamento | `Gemini Pro` / `Opus` | Sessão principal ao planejar, papéis de Arquiteto e Revisor de Código |
| Execução | `Gemini Flash` / `Haiku` | Execução de fatias (Implementador Java, QA / Testes); subagentes rápidos |
| Escalada de execução | `Gemini Pro` / `Sonnet` | Só quando a execução em modelo leve falhar duas vezes na mesma fatia |

## Fluxo

1. **Planejar (modelo analítico/forte).** Antes de criar ou editar arquivos, ou de rodar comando que altere estado:
   - Diagnosticar o problema ou definir o objetivo.
   - Listar exatamente quais arquivos serão tocados.
   - Dividir em fatias pequenas, reversíveis e sequenciadas (ordem padrão: `core` → `java11` → `java8`).
   - Identificar riscos e definir critérios de aceite testáveis.
   - Fases do roadmap viram especificações em `docs/specs/` antes da execução.
2. **Confirmar** com o usuário quando houver ambiguidade, risco ou regra fiscal em aberto (guard-rails).
3. **Executar (modelo econômico/rápido).** Cada fatia vai para um subagente ou execução focada com prompt **autocontido**:
   - Objetivo cirúrgico e arquivos exatos.
   - Padrão de referência (classe vizinha ou equivalente do `hl7-handler`).
   - Seção aplicável do manual da RFB.
   - Comandos de build e teste esperados (`./mvnw verify -pl lcdpr-<modulo>`).
   - Critérios de aceite e formato da resposta.
   - O porte core → java11 → java8 é sequencial por fatia: o java8 parte do java11 já aprovado, não do core.
4. **Verificar (modelo analítico).** Antes de relatar sucesso:
   - Rodar `./mvnw verify`.
   - Analisar o `git diff` real.
   - Relato do modelo de execução não é prova sem validação real de build e diff.
5. **Revisar (modelo analítico).** Mudança relevante passa por revisão completa (`review-guidelines`) antes de PR.

## Regras de Execução

- **Escalada:** duas falhas seguidas na mesma fatia com modelo leve → tentar com modelo mais forte. Se persistir, o problema está na modelagem do plano: volte ao planejamento e fatie melhor.
- Tarefas pontuais (correção óbvia de 1 linha, leitura simples) podem ser feitas diretamente sem subagentes.
