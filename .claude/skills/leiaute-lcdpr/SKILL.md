---
name: leiaute-lcdpr
description: Regras do leiaute 1.3 do LCDPR (Livro Caixa Digital do Produtor Rural) da RFB - registros, hierarquia, formatos de campo, obrigatoriedade e cálculo de saldos. Usar antes de implementar, revisar ou testar writer, reader, validação, cálculo ou modelo de qualquer registro (0000, 0010, 0030, 0040, 0045, 0050, Q100, Q200, 9999).
---

# Leiaute do LCDPR (versão 1.3)

Fonte: Manual de Preenchimento do LCDPR, anexo do Ato Declaratório Executivo
COPES nº 1/2020, em `_arquivos/manual-de-preenchimento-do-lcdpr-1-3.pdf`
(planilha do leiaute em `_arquivos/leiaute-1-3-lcdpr.xls`). Em caso de
dúvida, o manual prevalece sobre esta skill e sobre o código.
Para ler o PDF: `pdftotext -layout <pdf> <saida>` (saída em Latin-1).

Regra fiscal que não estiver aqui nem no manual **não se supõe**: perguntar
ao usuário (ver guard-rails).

## Formato do arquivo

- Uma linha por registro, campos separados por `|`, **sem** `|` no início
  nem no fim da linha (`0010|1`).
- Toda linha termina com CR LF (ASCII 13 e 10), inclusive a última. No
  manual, "CRLF" aparece literalmente nos exemplos, mas é o terminador e não
  texto do campo.
- Campo vazio: `||`; vazio no fim da linha: `12300|` + CRLF.

## Formatos de campo

| Tipo | Regra | Exemplos do manual |
|---|---|---|
| N com decimais | Só dígitos, sem separador; as casas decimais ficam implícitas | `1.129.998,99` → `112999899`; `10.000` → `1000000`; `17,00%` → `01700`; `0,00` → `000`; `0` em campo sem decimal → `0` |
| N sem decimais | Só dígitos | |
| Data (N 8) | `ddMMaaaa` | `01012005` |
| Mês (N 6) | `MMaaaa` | `012005` |
| CPF / CNPJ (N 11 / 14) | Só dígitos, com zeros à esquerda | `00233344940` |
| C | Texto; `-` no tamanho = até 255 | |

Percentuais (`PARTICIPACAO`, `PERC_CONTRAPARTE`) são N 5 com 2 decimais:
`05000` = 50,00%, `00520` = 5,20%.

## Registros

Ocorrência e nível hierárquico do manual (seção 3.2):

| REG | Nome | Nível | Ocorrência |
|---|---|---|---|
| 0000 | Abertura do arquivo e identificação da pessoa física | 0 | 1:1 |
| 0010 | Parâmetros de tributação | 2 | 1:1 |
| 0030 | Dados cadastrais do contribuinte | 2 | 1:1 |
| 0040 | Cadastro dos imóveis rurais | 2 | 1:N |
| 0045 | Cadastro de terceiros (filho do 0040, vem logo após cada 0040) | 3 | 1:N, obrigatório se `0040.PARTICIPACAO` < 100% ou `0040.TIPO_EXPLORACAO` ≠ 1 |
| 0050 | Contas bancárias | 2 | 1:N |
| Q100 | Demonstrativo do resultado da atividade rural | 2 | 0:N |
| Q200 | Resumo mensal do demonstrativo | 2 | 0:N |
| 9999 | Signatário e encerramento | 1 | 1:1 |

### Campos (ordem do arquivo)

- **0000**: `REG`, `NOME_ESC` (`LCDPR`), `COD_VER` (`0013`), `CPF` N11, `NOME`,
  `IND_SIT_INI_PER` N1 [0;1;2], `SIT_ESPECIAL` N1 [0;1;2;3], `DT_SIT_ESP` N8
  (opcional), `DT_INI` N8, `DT_FIN` N8. A escrituração é identificada por
  `CPF` + `DT_FIN`.
- **0010**: `REG`, `FORMA_APUR` N1 [1 = Livro Caixa; 2 = 20% da receita bruta, art. 5º da Lei 8.023/1990].
- **0030**: `REG`, `ENDERECO` C150, `NUM` C6, `COMPL` C50 (opcional), `BAIRRO` C50,
  `UF` C2, `COD_MUN` C7, `CEP` C8, `NUM_TEL` N15 (opcional), `EMAIL` C115.
- **0040**: `REG`, `COD_IMOVEL` N3, `PAIS` C2, `MOEDA` C3, `CAD_ITR` N8 (obrigatório
  se zona rural e país BR), `CAEPF` N14 (obrigatório se país BR e exploração
  individual, ou para o primeiro titular/representante nas coletivas),
  `INSCR_ESTADUAL` N14 (opcional), `NOME_IMOVEL` C50, `ENDERECO` C150, `NUM` C6,
  `COMPL` C50, `BAIRRO` C50, `UF` C2, `COD_MUN` C7, `CEP` C8 (UF/COD_MUN/CEP
  obrigatórios se país BR), `TIPO_EXPLORACAO` N1 [1 individual; 2 condomínio;
  3 arrendado; 4 parceria; 5 comodato; 6 outros], `PARTICIPACAO` N5,2.
  Mudança de exploração no ano = novo 0040 do mesmo imóvel.
- **0045**: `REG`, `COD_IMOVEL` N3 (do 0040), `TIPO_CONTRAPARTE` N1 [1 condômino;
  2 arrendador; 3 parceiro; 4 comodante; 5 outro], `ID_CONTRAPARTE` N11/14,
  `NOME_CONTRAPARTE` C50, `PERC_CONTRAPARTE` N5,2.
- **0050**: `REG`, `COD_CONTA` N3, `PAIS_CTA` C3, `BANCO` N3 (obrigatório se país BR),
  `NOME_BANCO` C30, `AGENCIA` N4 (sem DV), `NUM_CONTA` N16 (com DV).
- **Q100**: `REG`, `DATA` N8, `COD_IMOVEL` N3, `COD_CONTA` N3, `NUM_DOC` C (opcional),
  `TIPO_DOC` N1 [1 NF; 2 fatura; 3 recibo; 4 contrato; 5 folha; 6 outros], `HIST` C,
  `ID_PARTIC` N11/14, `TIPO_LANC` N1 [1 receita; 2 despesa de custeio e
  investimento; 3 receita de produtos entregues no ano, referente a adiantamento],
  `VL_ENTRADA` N19,2, `VL_SAIDA` N19,2, `SLD_FIN` N19,2, `NAT_SLD_FIN` C1 [N;P].
- **Q200**: `REG`, `MES` N6, `VL_ENTRADA` N19,2, `VL_SAIDA` N19,2, `SLD_FIN` N19,2,
  `NAT_SLD_FIN` C1 [N;P].
- **9999**: `REG`, `IDENT_NOM`, `IDENT_CPF_CNPJ` N11/14, `IND_CRC`, `EMAIL` C115,
  `FONE` N15, `QTD_LIN` N30 (obrigatório).

## Regras do Q100 e do Q200

- `COD_CONTA` = `000` para pagamento em espécie; `999` para numerário em trânsito.
  Fora isso, aponta um `0050.COD_CONTA`.
- `COD_IMOVEL` aponta um `0040.COD_IMOVEL`. `000` só para despesa de
  condomínio/parceria que afeta vários imóveis, com o histórico explicando.
- Saldo: começa em zero no início do ano. Cada Q100 guarda o saldo acumulado
  (receitas − despesas até ele), em valor absoluto, com `NAT_SLD_FIN` P ou N.
- Q200.`SLD_FIN` é cumulativo: saldo do mês + saldo final do mês anterior
  (exemplo do manual: janeiro 15.000,00 P; fevereiro −3.000,00 no mês → 12.000,00 P).
- Valores dos lançamentos de áreas exploradas em conjunto respeitam a
  `0040.PARTICIPACAO`.
- Importação/exportação direta com estrangeiro: `ID_PARTIC` = CPF do declarante
  e `NUM_DOC` = número da DI/DE (ou da NF, no Mercosul).

## Registro 9999

`QTD_LIN` = total de linhas do arquivo, contando todos os registros, inclusive
os repetidos e o próprio 9999. Calcular sempre, nunca pedir ao usuário.

## Exemplos de referência

Os exemplos de preenchimento do manual usam dados fictícios
(`JOSÉ DA SILVA`, CPF `11111111191`, `Fazenda Tudo Certo`) e servem de amostra
para os testes. O `README.md` e `src/test/resources` reproduzem esses exemplos.
Atenção: o exemplo do Q200 no manual tem erros de digitação (`100000,00` no
texto, `|P` grudado em `Q200`); conferir contra a regra, não contra o texto.
