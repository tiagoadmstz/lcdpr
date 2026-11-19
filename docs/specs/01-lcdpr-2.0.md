# Spec 01: lcdpr 2.0 (F1 a F6)

Fases F1 a F6 do [roadmap](../roadmap.md). Decisões D1–D4 valem aqui.
Regras do leiaute: skill `leiaute-lcdpr`. Estilo: skill `code-style`.
Referência de build: `C:\Users\fastw\DEV\hl7-handler`.

## Suposições (perguntas abertas)

- **P1**: o Q200 tem um registro por mês **com lançamento**, em ordem crescente.
- **P2**: a biblioteca não escolhe o charset; as variantes com `Path` recebem `Charset`.
- **P3**: leiaute 1.3. CNPJ alfanumérico (IN RFB 2.229/2024) é aceito na validação
  de `CPF_CNPJ` com 14 posições: `[0-9A-Z]{12}[0-9]{2}`, com DV calculado por `char - '0'`.

## F1: estrutura multi-módulo

- `pom.xml` pai (`io.github.tiagoadmstz.lcdpr:lcdpr-parent:${revision}`, `packaging pom`), no molde de
  `hl7-handler/pom.xml`. Mudanças em relação a ele:
  - módulos `lcdpr-core`, `lcdpr-java11`, `lcdpr-java8`, sem os perfis `examples`/`benchmarks`;
  - `revision` `2.0.0-SNAPSHOT`; `lcdpr.module.name` = `io.github.tiagoadmstz.lcdpr`;
  - `dependencyManagement` só com `tools.jackson.core:jackson-databind`
    (`${jackson.version}`), `com.fasterxml.jackson.core:jackson-databind`
    (`${jackson2.version}`), JUnit e AssertJ;
  - `url`, `licenses` (**MIT**, igual ao `LICENSE`), `developers` e `scm` do lcdpr;
    `distributionManagement` em `https://maven.pkg.github.com/tiagoadmstz/lcdpr`;
  - `outputTimestamp` `2026-11-19T00:00:00Z`.
- Poms dos módulos no molde dos do `hl7-handler`: `java.version` 17/11/8; dependência
  única de produção = `jackson-databind` (3 no core, 2 nos outros); `Automatic-Module-Name`
  `${lcdpr.module.name}` no manifesto **dos três** (sem `module-info.java`); no core,
  a execução extra de surefire `test-jdk25`.
- `mvnw`, `mvnw.cmd` e `.mvn/wrapper/maven-wrapper.properties` copiados do `hl7-handler`.
- `.editorconfig`, `.gitignore` e `.github/dependabot.yml` copiados do `hl7-handler`.
  `.gitattributes` copiado, mais a linha `src/test/resources/samples/** -text`
  (as amostras têm CRLF e precisam ficar byte a byte).
- `.github/workflows/ci.yml` e `release.yml` copiados, trocando `hl7-handler-*` por `lcdpr-*`
  e sem o passo "Compilar exemplos". Remover `.github/workflows/maven-publish.yml`.
- Remover `src/`, `config/` e `LCDPR.iml`. `git mv _arquivos docs/leiaute`.
- Aceite: `./mvnw -B verify` passa (módulos vazios), e o log mostra os toolchains 8, 11, 17 e 25.

## Pacotes e API pública (idêntica nos três módulos)

```
io.github.tiagoadmstz.lcdpr           Lcdpr, LcdprWriter, LcdprReader, LcdprJson,
                                      LcdprValidator, LcdprCalculator, LcdprException, ErroValidacao
io.github.tiagoadmstz.lcdpr.modelo    AberturaIdentificacao, IdentificacaoPessoaFisica, ParametrosTributacao,
                                      DadosCadastrais, ImovelRural, Terceiro, ContaBancaria,
                                      DemonstrativoResultado, Lancamento, ResumoMensal, EncerramentoArquivo
io.github.tiagoadmstz.lcdpr.tabela    IndicadorInicioPeriodo, SituacaoEspecial, FormaApuracao, TipoExploracao,
                                      TipoContraparte, TipoDocumento, TipoLancamento, NaturezaSaldo
io.github.tiagoadmstz.lcdpr.leiaute   Registro, Campo, Formato
```

Classes auxiliares package-private no pacote raiz: `Formatos` (formatar/interpretar
valores) e `RegistroCodec` (modelo ↔ lista de valores formatados, na ordem do leiaute).
O writer, o reader, o JSON e o validador usam os dois; nenhum faz formatação por conta própria.

### Tabelas internas (`tabela`)

Todo enum: `String codigo()`, `String descricao()`, `static X fromCodigo(String codigo)`,
que lança `IllegalArgumentException("Código inválido para <Enum>: <codigo>")` para
código fora da tabela ou `null`.

| Enum | Constantes (código) |
|---|---|
| `IndicadorInicioPeriodo` | `REGULAR` (0), `ABERTURA` (1), `INICIO_OBRIGATORIEDADE` (2) |
| `SituacaoEspecial` | `NORMAL` (0), `FALECIMENTO` (1), `ESPOLIO` (2), `SAIDA_DEFINITIVA_PAIS` (3) |
| `FormaApuracao` | `LIVRO_CAIXA` (1), `VINTE_POR_CENTO_RECEITA_BRUTA` (2) |
| `TipoExploracao` | `INDIVIDUAL` (1), `CONDOMINIO` (2), `ARRENDADO` (3), `PARCERIA` (4), `COMODATO` (5), `OUTROS` (6) |
| `TipoContraparte` | `CONDOMINO` (1), `ARRENDADOR` (2), `PARCEIRO` (3), `COMODANTE` (4), `OUTRO` (5) |
| `TipoDocumento` | `NOTA_FISCAL` (1), `FATURA` (2), `RECIBO` (3), `CONTRATO` (4), `FOLHA_PAGAMENTO` (5), `OUTROS` (6) |
| `TipoLancamento` | `RECEITA` (1), `DESPESA_CUSTEIO_INVESTIMENTO` (2), `RECEITA_ADIANTAMENTO` (3) |
| `NaturezaSaldo` | `POSITIVO` (P), `NEGATIVO` (N) |

Descrições: texto do manual (skill `leiaute-lcdpr`).

### Leiaute (`leiaute`)

- `enum Formato { TEXTO, NUMERO, NUMERO_EXATO, CPF_CNPJ, DATA, MES, VALOR, PERCENTUAL }`.
- `record Campo(String nome, Formato formato, int tamanho, boolean obrigatorio)`.
  `nome` sem acento, como na tabela abaixo.
- `enum Registro`: `R0000`, `R0010`, `R0030`, `R0040`, `R0045`, `R0050`, `RQ100`, `RQ200`, `R9999`,
  com `String codigo()` ("0000"...), `List<Campo> campos()` (sem o `REG`, imutável) e
  `static Registro fromCodigo(String)` (estrito, `IllegalArgumentException`).

Campos (nome formato tamanho obrigatório; `S` = obrigatório incondicional):

| REG | Campos |
|---|---|
| 0000 | `NOME_ESC` TEXTO 5 S · `COD_VER` TEXTO 4 S · `CPF` NUMERO_EXATO 11 S · `NOME` TEXTO 255 S · `IND_SIT_INI_PER` NUMERO_EXATO 1 S · `SIT_ESPECIAL` NUMERO_EXATO 1 S · `DT_SIT_ESP` DATA 8 N · `DT_INI` DATA 8 S · `DT_FIN` DATA 8 S |
| 0010 | `FORMA_APUR` NUMERO_EXATO 1 S |
| 0030 | `ENDERECO` TEXTO 150 S · `NUM` TEXTO 6 S · `COMPL` TEXTO 50 N · `BAIRRO` TEXTO 50 S · `UF` TEXTO 2 S · `COD_MUN` TEXTO 7 S · `CEP` TEXTO 8 S · `NUM_TEL` NUMERO 15 N · `EMAIL` TEXTO 115 S |
| 0040 | `COD_IMOVEL` NUMERO_EXATO 3 S · `PAIS` TEXTO 2 S · `MOEDA` TEXTO 3 S · `CAD_ITR` NUMERO_EXATO 8 N · `CAEPF` NUMERO_EXATO 14 N · `INSCR_ESTADUAL` NUMERO 14 N · `NOME_IMOVEL` TEXTO 50 S · `ENDERECO` TEXTO 150 S · `NUM` TEXTO 6 N · `COMPL` TEXTO 50 N · `BAIRRO` TEXTO 50 S · `UF` TEXTO 2 N · `COD_MUN` TEXTO 7 N · `CEP` TEXTO 8 N · `TIPO_EXPLORACAO` NUMERO_EXATO 1 S · `PARTICIPACAO` PERCENTUAL 5 S |
| 0045 | `COD_IMOVEL` NUMERO_EXATO 3 S · `TIPO_CONTRAPARTE` NUMERO_EXATO 1 S · `ID_CONTRAPARTE` CPF_CNPJ 14 S · `NOME_CONTRAPARTE` TEXTO 50 S · `PERC_CONTRAPARTE` PERCENTUAL 5 S |
| 0050 | `COD_CONTA` NUMERO_EXATO 3 S · `PAIS_CTA` TEXTO 3 S · `BANCO` NUMERO_EXATO 3 N · `NOME_BANCO` TEXTO 30 S · `AGENCIA` NUMERO_EXATO 4 S · `NUM_CONTA` NUMERO_EXATO 16 S |
| Q100 | `DATA` DATA 8 S · `COD_IMOVEL` NUMERO_EXATO 3 S · `COD_CONTA` NUMERO_EXATO 3 S · `NUM_DOC` TEXTO 255 N · `TIPO_DOC` NUMERO_EXATO 1 S · `HIST` TEXTO 255 S · `ID_PARTIC` CPF_CNPJ 14 S · `TIPO_LANC` NUMERO_EXATO 1 S · `VL_ENTRADA` VALOR 19 S · `VL_SAIDA` VALOR 19 S · `SLD_FIN` VALOR 19 S · `NAT_SLD_FIN` TEXTO 1 S |
| Q200 | `MES` MES 6 S · `VL_ENTRADA` VALOR 19 S · `VL_SAIDA` VALOR 19 S · `SLD_FIN` VALOR 19 S · `NAT_SLD_FIN` TEXTO 1 S |
| 9999 | `IDENT_NOM` TEXTO 255 N · `IDENT_CPF_CNPJ` CPF_CNPJ 14 N · `IND_CRC` TEXTO 255 N · `EMAIL` TEXTO 115 N · `FONE` NUMERO 15 N · `QTD_LIN` NUMERO 30 S |

### Modelo (`modelo` + `Lcdpr`)

Record no core; classe `final` nos outros, com o **mesmo** construtor público de todos os
campos, os mesmos acessores (`cpf()`), `equals`/`hashCode`/`toString`.
Cada tipo tem `static Builder builder()`, `Builder toBuilder()` e uma classe
`public static final class Builder` com um setter fluente por campo (mesmo nome do campo),
`add<Item>(item)` para cada lista e `build()`.

Construtor (compact constructor no core):
- listas `null` → vazias; listas copiadas e imutáveis (`List.copyOf` no core; nos outros,
  `Collections.unmodifiableList(new ArrayList<>(lista))`);
- `BigDecimal` não nulo → `setScale(2, RoundingMode.HALF_UP)`.
- Sem outras validações no construtor (validar é papel do `LcdprValidator`).

Campos em ordem (tipo; default do builder entre colchetes). Strings podem ser `null`.

- `Lcdpr(AberturaIdentificacao bloco0, DemonstrativoResultado blocoQ, EncerramentoArquivo bloco9)`
- `AberturaIdentificacao(IdentificacaoPessoaFisica identificacao, ParametrosTributacao parametrosTributacao, DadosCadastrais dadosCadastrais, List<ImovelRural> imoveisRurais, List<ContaBancaria> contasBancarias)`;
  adders `addImovelRural`, `addContaBancaria`.
- `IdentificacaoPessoaFisica(String codigoVersao ["0013"], String cpf, String nome, IndicadorInicioPeriodo indicadorInicioPeriodo [REGULAR], SituacaoEspecial situacaoEspecial [NORMAL], LocalDate dataSituacaoEspecial, LocalDate dataInicial, LocalDate dataFinal)`.
  `NOME_ESC` não é campo: sempre `LCDPR`.
- `ParametrosTributacao(FormaApuracao formaApuracao [LIVRO_CAIXA])`
- `DadosCadastrais(String endereco, String numero, String complemento, String bairro, String uf, String codigoMunicipio, String cep, String telefone, String email)`
- `ImovelRural(String codigoImovel, String pais ["BR"], String moeda ["BRL"], String cadastroItr, String caepf, String inscricaoEstadual, String nome, String endereco, String numero, String complemento, String bairro, String uf, String codigoMunicipio, String cep, TipoExploracao tipoExploracao, BigDecimal participacao, List<Terceiro> terceiros)`; adder `addTerceiro`.
- `Terceiro(String codigoImovel, TipoContraparte tipoContraparte, String cpfCnpj, String nome, BigDecimal participacao)`
- `ContaBancaria(String codigoConta, String pais ["BR"], String banco, String nomeBanco, String agencia, String numeroConta)`
- `DemonstrativoResultado(List<Lancamento> lancamentos, List<ResumoMensal> resumosMensais)`; adders `addLancamento`, `addResumoMensal`.
- `Lancamento(LocalDate data, String codigoImovel, String codigoConta, String numeroDocumento, TipoDocumento tipoDocumento, String historico, String cpfCnpjParticipante, TipoLancamento tipoLancamento, BigDecimal valorEntrada [0.00], BigDecimal valorSaida [0.00], BigDecimal saldoFinal [0.00], NaturezaSaldo naturezaSaldoFinal [POSITIVO])`
- `ResumoMensal(YearMonth mes, BigDecimal valorEntrada [0.00], BigDecimal valorSaida [0.00], BigDecimal saldoFinal [0.00], NaturezaSaldo naturezaSaldoFinal [POSITIVO])`
- `EncerramentoArquivo(String nomeContador, String cpfCnpjContador, String crcContador, String emailContador, String telefoneContador, Long quantidadeLinhas)`

### `Formatos` (package-private)

| Formato | Escrever (modelo → texto) | Ler (texto → modelo) |
|---|---|---|
| TEXTO, NUMERO, NUMERO_EXATO, CPF_CNPJ | `null` → `""`; valor com `|`, `\r` ou `\n` → `LcdprException` | `""` → `null` |
| DATA | `LocalDate` → `ddMMyyyy` | `ddMMyyyy` estrito (`ResolverStyle.STRICT`, padrão `ddMMuuuu`) |
| MES | `YearMonth` → `MMyyyy` | `MMyyyy` estrito |
| VALOR | `setScale(2, HALF_UP)`; negativo → `LcdprException`; dígitos de `unscaledValue()` com zeros à esquerda até 3 (`0` → `000`, `0.01` → `001`, `10000` → `1000000`) | `""` → `null`; só dígitos, senão `LcdprException`; `new BigDecimal(new BigInteger(s), 2)` (`0` → `0.00`) |
| PERCENTUAL | igual a VALOR, com zeros à esquerda até 5 (`50` → `05000`, `5.2` → `00520`); mais de 5 dígitos → `LcdprException` | igual a VALOR |
| enum | `codigo()` | `fromCodigo`, com `IllegalArgumentException` convertida em `LcdprException` |

Toda mensagem de erro de formato cita `<REG>.<CAMPO>` e, no reader e no writer, a linha.
Constantes únicas: `SEPARADOR = "|"`, `FIM_DE_LINHA = "\r\n"` e os `DateTimeFormatter`.

### `LcdprException`

`public class LcdprException extends RuntimeException`, construtores `(String mensagem)`,
`(String mensagem, int linha)`, `(String mensagem, Throwable causa)` e
`(String mensagem, int linha, Throwable causa)`; `int linha()` (0 = sem linha).
Quando há linha, a mensagem termina com ` (linha N)`.

### `LcdprWriter`

`static String write(Lcdpr)`, `static void write(Lcdpr, Writer)` e
`static void write(Lcdpr, Path, Charset)`.

- Ordem: 0000, 0010, 0030, cada 0040 seguido dos **seus** 0045, cada 0050, Q100, Q200, 9999.
- Linha = `REG` + `|` + valores do `RegistroCodec` unidos por `|`, terminada em CRLF
  (inclusive a última).
- `QTD_LIN` sempre calculado: total de linhas, incluindo o 9999. O `quantidadeLinhas` do modelo é ignorado.
- `bloco0`, `identificacao`, `parametrosTributacao` ou `dadosCadastrais` nulos →
  `LcdprException`. `blocoQ` nulo = vazio. `bloco9` nulo = 9999 só com `QTD_LIN`.
- Nunca altera o modelo; `IOException` vira `LcdprException`.

### `LcdprReader`

`static Lcdpr read(String)`, `static Lcdpr read(Reader)` e `static Lcdpr read(Path, Charset)`.

- Linhas separadas por CRLF ou LF; uma linha vazia final é ignorada, e qualquer outra
  linha vazia → `LcdprException`.
- `split("\\|", -1)`; o primeiro valor é o `REG` (`Registro.fromCodigo`, desconhecido →
  exceção). Quantidade de valores depois do `REG` diferente de `campos().size()` → exceção
  `"Registro 0040 com 15 campos; esperado 16 (linha 4)"`.
- A primeira linha tem de ser 0000 (com `NOME_ESC` = `LCDPR`), e a última, 9999.
  0000, 0010, 0030 e 9999 no máximo uma vez. 0045 vai para o último 0040 lido; 0045
  sem 0040 antes → exceção. Fora isso, a ordem não é imposta.
- `quantidadeLinhas` = valor lido do arquivo. Todo erro sai como `LcdprException` com a linha.

### `LcdprJson`

`static String toJson(Lcdpr)`, `static void toJson(Lcdpr, Writer)`,
`static Lcdpr fromJson(String)` e `static Lcdpr fromJson(Reader)`. Usa só a árvore do
Jackson (`ObjectNode`/`ArrayNode`), sem anotação no modelo. Um `JsonMapper` estático.
No core, a API do Jackson 3 (`tools.jackson.databind`): `asString()`, `isString()`,
`properties()`, exceções `JacksonException` unchecked. Conferir no `hl7-handler-core`.

Formato, compatível com o JSON da 1.x (README):

```json
{"bloco_0": {"identificacao_pessoa_fisica": {...}, "parametros_tributacao": {...},
             "dados_cadastrais_contribuinte": {...},
             "imoveis_rurais": [{..., "exploracao_imoveis_rurais": [{...0045...}]}],
             "contas_bancarias": [{...}]},
 "bloco_Q": {"demonstrativo_livro_caixa": [{...}], "resumo_demonstrativo_livro_caixa": [{...}]},
 "bloco_9": {...}}
```

- Cada registro é um objeto: primeiro `"reg"`, depois cada campo do leiaute com chave =
  `nome` em minúsculas e valor = texto formatado igual ao do arquivo (`"vl_entrada":"1000000"`).
  Campo com texto vazio é omitido. No 0000, `nome_esc` sai sempre como `"LCDPR"`.
- `qtd_lin` sai como no modelo (omitido se `null`).
- Leitura: chaves comparadas sem diferenciar maiúsculas (`cad_ITR` e `CAEPF` da 1.x funcionam);
  chave ausente = texto vazio; aceita string ou número (texto do número); `reg`,
  `nome_esc` e chaves desconhecidas são ignorados. JSON inválido ou com conteúdo depois do
  objeto raiz → `LcdprException` (nos três módulos).
- `bloco_0.exploracao_imoveis_rurais` (formato do exemplo do README da 1.x): cada 0045 é
  anexado ao 0040 de mesmo `cod_imovel` (o primeiro, se houver repetição); sem 0040 com esse
  código → `LcdprException`. O 1.x gravava os terceiros dentro do imóvel, que é o formato da 2.0.
- Entrada `null` no reader e no `fromJson` → `LcdprException`. BOM UTF-8 no início é ignorado.

### `ErroValidacao` e `LcdprValidator`

`record ErroValidacao(int linha, String registro, String campo, String mensagem)`:
`linha` = posição que o registro teria no arquivo gerado pelo `LcdprWriter`
(0 quando o erro não é de um registro); `campo` = nome do leiaute ou `null`.

`static List<ErroValidacao> validate(Lcdpr)`: devolve a lista (vazia = válido), nunca lança
exceção por conteúdo. Percorre na ordem do writer. Regras:

1. Presença: `bloco0`, `identificacao`, `parametrosTributacao`, `dadosCadastrais`;
   ao menos um 0040 e um 0050.
2. Por campo (descritor), sobre o texto formatado: obrigatório vazio → `"<REG>.<CAMPO> obrigatório"`;
   TEXTO com mais caracteres que `tamanho`; NUMERO com não dígito ou mais que `tamanho`;
   NUMERO_EXATO diferente de `tamanho` dígitos; CPF_CNPJ fora de 11 dígitos ou de 14
   posições; VALOR com mais de 19 dígitos. Se o `RegistroCodec` lançar `LcdprException`
   (valor negativo, `|` no texto), essa mensagem vira o erro do registro.
3. DV: CPF (11; todos os dígitos iguais é inválido) e CNPJ (14, inclusive alfanumérico;
   todos os caracteres iguais é inválido) em `0000.CPF`, `0045.ID_CONTRAPARTE`,
   `Q100.ID_PARTIC` e `9999.IDENT_CPF_CNPJ` (quando preenchido) → `"<REG>.<CAMPO> com dígito verificador inválido"`.
4. 0000: `DT_INI` ≤ `DT_FIN`, no mesmo ano; `SIT_ESPECIAL` = FALECIMENTO exige `DT_SIT_ESP`.
5. 0040: `PARTICIPACAO` entre 0 e 100. Com `PAIS` = BR, `UF`, `COD_MUN` e `CEP` são
   obrigatórios, e `CAEPF` também, se `TIPO_EXPLORACAO` = INDIVIDUAL. Sem 0045 quando
   `PARTICIPACAO` < 100 ou `TIPO_EXPLORACAO` ≠ INDIVIDUAL → erro no 0040. O 0045 com
   `COD_IMOVEL` diferente do 0040 pai → erro; `PERC_CONTRAPARTE` entre 0 e 100.
6. 0050: `BANCO` obrigatório com `PAIS_CTA` = BR; `COD_CONTA` repetido → erro.
7. Q100: lançamentos em ordem de `DATA` (manual, seção 3: a ordem dos registros é
   sequencial; o saldo é acumulado na ordem do arquivo) → um erro no primeiro fora de ordem;
   `COD_IMOVEL` cadastrado num 0040 ou `000`; `COD_CONTA` cadastrado num 0050, `000`
   ou `999`; `DATA` dentro de `DT_INI`..`DT_FIN`; `VL_ENTRADA` e `VL_SAIDA` maiores que zero
   no mesmo lançamento → erro.
8. Q200: `MES` dentro do período.
9. Saldos: compara Q100 e Q200 com o `LcdprCalculator.recalculate` (cada lançamento com o
   seu calculado, ligado pela posição na ordenação estável, mesmo fora de ordem); divergência de
   `SLD_FIN`/`NAT_SLD_FIN` → `"Q100.SLD_FIN divergente do calculado: esperado 5000.00 P"`.
   Q200 com quantidade de meses diferente → um erro no bloco Q (linha 0).
10. 9999: `quantidadeLinhas` não nulo e diferente do calculado → erro.

### `LcdprCalculator`

`static Lcdpr recalculate(Lcdpr)`: devolve um novo `Lcdpr`, com `bloco0` e `bloco9`
intactos. Com `blocoQ` nulo, devolve o mesmo objeto.

- Q100 em ordem de `data` (ordenação estável; `data` nula no fim). Saldo começa em zero;
  `saldo += entrada − saída` (`null` = zero); `saldoFinal` = `abs(saldo)`;
  `naturezaSaldoFinal` = POSITIVO se `saldo` ≥ 0, senão NEGATIVO.
- Q200: um por mês com lançamento (P1), em ordem crescente; somas de entrada e saída do
  mês; saldo cumulativo = saldo do mês anterior + entrada − saída; `abs` + natureza.

## Amostras e testes

Amostras em `src/test/resources/samples/` (idênticas nos três módulos), em UTF-8:

- `lcdpr-valido.txt`: arquivo válido de referência, com CRLF (fornecido na fatia S3);
- `lcdpr-valido.json`: o mesmo em JSON, gerado pelo `LcdprJson` e conferido à mão;
- `lcdpr-legado-1x.json`: o JSON do README da 1.x.

Classe de teste `Amostras` (pacote `io.github.tiagoadmstz.lcdpr`, em `src/test`) com
`static Lcdpr valido()`, que monta pelo builder o mesmo conteúdo de `lcdpr-valido.txt`.

Testes, um método por caso, nos três módulos:

- `TabelaTest`: `fromCodigo` de cada enum com código válido e com inválido (bug 8).
- `FormatosTest`: os exemplos do manual para VALOR e PERCENTUAL; `0` e `""` (bug 3);
  negativo; texto com `|`.
- `LcdprWriterTest`: `Amostras.valido()` gera `lcdpr-valido.txt` byte a byte; o mesmo
  imóvel repetido mantém os seus 0045 e a quebra de linha (bug 1); `QTD_LIN` calculado
  (bug 6); o modelo não muda (`equals` antes e depois).
- `LcdprReaderTest`: lê a amostra e compara com `Amostras.valido()`; ida e volta byte a byte;
  dois 0040 com 0045 (bug 4); `HIST` contendo `Q100` e `9999` (bug 5); quantidade de campos
  errada com a linha na mensagem; 0045 antes do 0040; REG desconhecido; arquivo só com LF.
- `LcdprJsonTest`: ida e volta; lê `lcdpr-legado-1x.json`; JSON inválido.
- `LcdprValidatorTest`: amostra válida sem erros; um teste por regra 1–10.
- `LcdprCalculatorTest`: entradas somam (bug 2); saldo negativo vira N; Q200 cumulativo
  (janeiro +5.000,00 → fevereiro −8.000,00 → 3.000,00 N); ordena por data; não altera a entrada.

## Fatias

| Fatia | Módulo | Conteúdo | Agente |
|---|---|---|---|
| S1 | raiz | F1 inteira | implementador-java |
| S2 | core | `tabela`, `leiaute`, `modelo`, `Lcdpr`, `LcdprException`, `TabelaTest` | implementador-java |
| S3 | core | `Formatos`, `RegistroCodec`, `LcdprWriter`, `LcdprReader`, amostra txt, `Amostras` e testes | implementador-java |
| S4 | core | `LcdprJson`, amostras json e testes | implementador-java |
| S5 | core | `ErroValidacao`, `LcdprValidator`, `LcdprCalculator` e testes | implementador-java |
| S6 | java11 | porte de S2–S5 | implementador-java |
| S7 | java8 | porte de S6 | implementador-java |

Critério de aceite de cada fatia: `./mvnw -B verify -pl <módulo>` passa, com os testes da
fatia, e Checkstyle limpo. Na S7, o `diff` de API da `review-guidelines` só com as diferenças aceitas.
