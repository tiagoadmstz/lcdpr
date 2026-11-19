# Política de Segurança

## Versões suportadas

Apenas a última versão publicada da série 2.x recebe correções de segurança. A série 1.x não recebe correções: quem usa a 1.x deve migrar (ver [o guia de migração](docs/migracao-1x-2x.md)).

| Versão | Suporte |
|---|---|
| Última `2.x` publicada | Sim |
| Série `1.x` | Não |

## Reportando uma vulnerabilidade

**Não abra uma issue pública.** Reporte pelo recurso de relatórios privados do GitHub:

1. Acesse a aba **Security** do repositório.
2. Clique em **Report a vulnerability**.

Ou abra diretamente: https://github.com/tiagoadmstz/lcdpr/security/advisories/new

Informe, se possível:

- descrição da vulnerabilidade e o impacto esperado;
- versão e artefato afetados;
- passos para reproduzir, com um arquivo fictício (um trecho com dados inventados basta).

## Dados pessoais

**Não envie arquivo LCDPR real** em issue, pull request, commit ou no relatório de vulnerabilidade. O arquivo contém CPF, CNPJ, nome, endereço, contas bancárias e valores de livro caixa. Para reproduzir um problema, use dados inventados ou os exemplos do manual.

## Prazos

- **Confirmação do recebimento:** em até 7 dias.
- **Plano de correção ou resposta:** em até 30 dias.

Os prazos são de resposta, não de correção: a correção depende da gravidade e da complexidade do caso.

## Escopo

Estão no escopo:

- **Entrada não confiável no leitor de arquivo:** arquivo malformado que cause exceção que não seja `LcdprException`, travamento ou consumo excessivo de memória ou CPU.
- **Entrada não confiável no JSON:** conteúdo JSON malformado ou hostil que cause consumo excessivo de recursos ou execução de código.
- **Vazamento de dados:** valores de campos do arquivo que apareçam em log, em mensagem de erro ou em exceção. A biblioteca não registra valores de campos.

Fora do escopo:

- **Assinatura digital e entrega:** a biblioteca não assina o arquivo com certificado ICP-Brasil e não transmite nada à Receita Federal.
- **Sistema que usa a biblioteca:** autenticação, autorização, armazenamento e transporte dos arquivos e dos dados.
- **Dependências:** vulnerabilidades em dependências que não sejam exploráveis através da API pública.
