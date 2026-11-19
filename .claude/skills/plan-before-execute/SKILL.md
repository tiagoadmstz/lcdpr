---
name: plan-before-execute
description: Regra do lcdpr para qualquer tarefa não trivial - planejar primeiro num modelo mais caro (Opus), executar com agentes mais baratos (Haiku) e verificar no modelo caro. Use antes de implementar, migrar, portar entre módulos, refatorar ou escrever testes em lote; prevalece sobre a skill global de mesmo nome neste repositório.
---

# Planejar no modelo caro, executar no barato

## Níveis de modelo

| Nível | Modelo | Quem usa |
|---|---|---|
| Planejamento e julgamento | `opus` | Sessão principal ao planejar, agents `arquiteto` e `revisor-codigo` |
| Execução | `haiku` | Agents `implementador-java` e `qa-testes`; `general-purpose` com `model: "haiku"` |
| Escalada de execução | `sonnet` | Só quando o Haiku falhar duas vezes na mesma fatia |

## Fluxo

1. **Planejar (caro).** Antes de criar ou editar arquivos, ou de rodar
   comando que mude estado: o que muda, quais arquivos, em que ordem, riscos.
   Fase do [roadmap](../../../docs/roadmap.md) vira spec em `docs/specs/`
   antes da execução. Investigação só de leitura faz parte do planejamento.
2. **Confirmar** com o usuário quando houver ambiguidade, risco ou regra
   fiscal em aberto (guard-rails).
3. **Executar (barato).** Cada fatia vai para um agent de execução com prompt
   **autocontido**: objetivo, arquivos exatos, padrão de referência (arquivo
   vizinho ou classe equivalente do `hl7-handler`), seção do manual,
   comandos de build e teste, critério de aceite e formato da resposta.
   - Fatias independentes rodam em paralelo, em agents separados na mesma mensagem.
   - Fatias que compartilham contrato (modelo, descritor de leiaute) rodam em sequência.
   - O porte core → java11 → java8 é sequencial por fatia: o java8 parte do
     java11 já aprovado, não do core.
4. **Verificar (caro).** Antes de relatar sucesso: rodar `./mvnw -B verify`
   (ou `mvn -B test` enquanto a F1 não terminar) e ler o diff. Relato de
   agente barato não é prova.
5. **Revisar (caro).** Mudança relevante passa pelo `revisor-codigo` antes do PR.

## Regras do mecanismo

- `subagent_type: "fork"` herda sempre o modelo da sessão (o `model` é
  ignorado). Para execução barata, usar agent do projeto ou `general-purpose`
  com `model: "haiku"`.
- **Escalada:** duas falhas do Haiku na mesma fatia → reenviar com
  `model: "sonnet"`. Se falhar de novo, o problema é o plano: voltar ao passo 1
  e quebrar a fatia.
- Tarefa pequena (uma edição óbvia, uma consulta) pode ser feita direto na
  sessão, sem subagente; dizer isso ao usuário.

## Por quê

Mesmo pedido do usuário no `eirion-platform` (2026-11-17) e no `hl7-handler`:
qualidade de planejamento vale o modelo forte; execução mecânica de um plano
fechado, não. Estende a skill global `plan-before-execute`.
