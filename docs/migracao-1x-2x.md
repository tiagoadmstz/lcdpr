# Guia de migração: 1.x para 2.0

A versão 2.0.0 quebra a API das versões 1.0.0 e 1.0.2. Este guia lista o que mudou e como trocar cada parte do código.

A 1.x era um artefato único em Java 8, com Lombok, no pacote `br.com.dsc.lcdpr`. A 2.0 tem três artefatos com a mesma API, sem Lombok, e os nomes de classe seguem o leiaute em português.

## 1. Dependência

Na 1.x, as versões publicadas no GitHub Packages usam:

```xml
<groupId>io.github.tiagoadmstz</groupId>
<artifactId>lcdpr</artifactId>
<version>1.0.2</version>
```

Na 2.0.0, escolha um artefato pela versão do Java do projeto:

| Artefato | Java | Jackson |
|---|---|---|
| `lcdpr-core` | 17 ou superior | 3 |
| `lcdpr-java11` | 11 a 16 | 2 |
| `lcdpr-java8` | 8 | 2 |

```xml
<dependency>
    <groupId>io.github.tiagoadmstz.lcdpr</groupId>
    <artifactId>lcdpr-core</artifactId>
    <version>2.0.0</version>
</dependency>
```

O repositório e a configuração do token estão no [README](../README.md#instalação).

## 2. Classes

Os pacotes passam a ser `io.github.tiagoadmstz.lcdpr`, `...lcdpr.modelo` e `...lcdpr.tabela`.

| 1.x | 2.0 | Mudanças nos campos |
|---|---|---|
| `br.com.dsc.lcdpr.lcdpr.Lcdpr` | `Lcdpr` | Mantém `bloco0`, `blocoQ` e `bloco9`. Não é mais superclasse. |
| `blocos.AberturaIdentificacao` | `modelo.AberturaIdentificacao` | `identificacaoPessoaFisica` vira `identificacao`; `dadosCadastraisContribuinte` vira `dadosCadastrais`; `imoveisRurais` e `contasBancarias` mantêm o nome. |
| `blocos.DemonstrativoLivroCaixa` | `modelo.DemonstrativoResultado` | `demonstrativoLivroCaixa` vira `lancamentos`; `resumoDemonstrativoLivroCaixa` vira `resumosMensais`. |
| `blocos.EncerramentoArquivo` | `modelo.EncerramentoArquivo` | `numeroConselhoRegionalContabilidade` vira `crcContador`; `quantidadeRegistrosArquivo` vira `quantidadeLinhas` (`Long`); `telefoneContador` passa a `String`. |
| `components.IdentificacaoPessoaFisica` | `modelo.IdentificacaoPessoaFisica` | `dataInicioPeriodo` vira `dataInicial`; `dataFinalPeriodo` vira `dataFinal`; `nomeEsc` sai (o valor é sempre `LCDPR`). |
| `components.ParametrosTributacao` | `modelo.ParametrosTributacao` | `formaApuracao` mantém o nome, com o enum novo. |
| `components.Contribuinte` | `modelo.DadosCadastrais` | `numeroTelefone` (`Long`) vira `telefone` (`String`). |
| `components.ImovelRural` | `modelo.ImovelRural` | `cadastroImpostoTerritorialRural` vira `cadastroItr`; `cadastroAtividadeEconomicaPessoaFisica` vira `caepf`; `nomeImovel` vira `nome`; `participacao` passa de `String` a `BigDecimal`; `cadastroTerceiros` vira `terceiros`. |
| `components.CadastroTerceiro` | `modelo.Terceiro` | `cpfCnpjContraparte` vira `cpfCnpj`; `nomeContraparte` vira `nome`; `percentualContraparte` (`String`) vira `participacao` (`BigDecimal`); `registro` sai. |
| `components.ContaBancaria` | `modelo.ContaBancaria` | `paisConta` vira `pais`. |
| `components.DemoLivroCaixa` | `modelo.Lancamento` | `registro` sai; os valores mantêm o nome, em `BigDecimal`. |
| `components.ResumoDemoLivroCaixa` | `modelo.ResumoMensal` | `mes` (`String`, `MMaaaa`) vira `mes` (`YearMonth`). |

Os enums passam para o pacote `tabela`, com nomes em CamelCase. Os códigos continuam sendo os do manual, mas agora são `String`, e a conversão é com `fromCodigo(String)`:

| 1.x | 2.0 | Constantes que mudaram |
|---|---|---|
| `enumerated.FORMA_APURACAO` | `tabela.FormaApuracao` | `APURACAO_LUCRO_8023` vira `VINTE_POR_CENTO_RECEITA_BRUTA` |
| `enumerated.INICIO_PERIODO` | `tabela.IndicadorInicioPeriodo` | `OBRIGATORIO_ESCRITURACAO` vira `INICIO_OBRIGATORIEDADE` |
| `enumerated.SITUACAO_ESPECIAL` | `tabela.SituacaoEspecial` | sem mudança |
| `enumerated.TIPO_CONTRAPARTE` | `tabela.TipoContraparte` | sem mudança |
| `enumerated.TIPO_DOCUMENTO` | `tabela.TipoDocumento` | sem mudança |
| `enumerated.TIPO_EXPLORACAO` | `tabela.TipoExploracao` | `IMOVEL_PROPRIO` vira `INDIVIDUAL`; `IMOVEL_ARRENDADO` vira `ARRENDADO` |
| `enumerated.TIPO_LANCAMENTO` | `tabela.TipoLancamento` | `RECEITA_ATIVIDADE_RURAL` vira `RECEITA`; `DESPESAS_CUSTEIO_INVESTIMENTOS` vira `DESPESA_CUSTEIO_INVESTIMENTO`; `RECEITA_PRODUTOS_ENTREGUES` vira `RECEITA_ADIANTAMENTO` |
| `enumerated.NATUREZA_SALDO_FINAL` | `tabela.NaturezaSaldo` | sem mudança |

Na 1.x, `getEnum(int)` e `getValor()` (código numérico) viram `fromCodigo(String)` e `codigo()`. `getDescription` vira `descricao()`.

### Classes sem equivalente

| 1.x | 2.0 |
|---|---|
| `util.LcdprUtil` | `LcdprWriter`, `LcdprReader` e `LcdprJson` (ver a tabela de métodos) |
| `util.LcdprPipeConverter` | `LcdprWriter.write` |
| `util.BigDecimalUtil` | sem equivalente público; a formatação é interna |
| `util.WordUtils`, `util.ExceptionUtil`, `interfaces.ExceptionFunction` | removidas, sem substituto |
| `interfaces.LcdprHandler` | removida |
| `exceptions.ServiceException` | `LcdprException` |
| `annotations.Validation` e a validação por anotação | `LcdprValidator` |
| `serializers.*`, `deserializers.*` | removidos; o JSON é feito por `LcdprJson` |
| `main.Main` | removida |

## 3. Métodos

| 1.x | 2.0 | Observação |
|---|---|---|
| `lcdpr.generatePipeText()` ou `LcdprPipeConverter.generatePipeText(obj)` | `LcdprWriter.write(lcdpr)` | Devolve `String`. |
| `LcdprUtil.exportLcdprFile(lcdpr, data)` | `LcdprWriter.write(lcdpr, path, charset)` | A 1.x gravava `LCDPR_<ddMMyyyy>.txt` no diretório atual e devolvia `boolean`. Agora o caminho é informado e a falha lança `LcdprException`. |
| `LcdprUtil.exportLcdprJson(lcdpr, data)` | `LcdprJson.toJson(lcdpr)` ou `LcdprJson.toJson(lcdpr, writer)` | Mesma observação sobre o nome do arquivo. |
| `LcdprUtil.importLcdprFromTxtFile(file)` | `LcdprReader.read(path, charset)` | Também aceita `String` e `Reader`. |
| `LcdprUtil.importLcdprByFromJsonFile(file)` | `LcdprJson.fromJson(reader)` ou `LcdprJson.fromJson(string)` | Ler o arquivo com `Files.newBufferedReader(path, charset)`. |
| `lcdpr.recalculateBlockQ()` (altera o objeto) | `LcdprCalculator.recalculate(lcdpr)` | Devolve um novo `Lcdpr`. É preciso usar o retorno. |
| `lcdpr.validateBlockQ()`, `isBlockQNull()`, `DemoLivroCaixa.validate(...)` | `LcdprValidator.validate(lcdpr)` | Devolve `List<ErroValidacao>`; não lança exceção por conteúdo. |
| `LcdprPipeConverter.validateByAnnotation(obj)` | `LcdprValidator.validate(lcdpr)` | |
| `DemonstrativoLivroCaixa.calculate()`, `recalculateBlockQ200()` | `LcdprCalculator.recalculate(lcdpr)` | Recalcula Q100 e Q200 juntos. |
| `AberturaIdentificacao.buildFromLinesList(lines)` e similares | `LcdprReader.read(...)` | Não há leitura por linha isolada: o arquivo inteiro é lido. |
| `addImovelRural`, `addContaBancaria`, `addDemoLivroCaixa`, `addResumoDemonstrativoLivroCaixa` | `addImovelRural`, `addContaBancaria`, `addLancamento`, `addResumoMensal` nos builders | Os nomes dos métodos de adição mudam. Terceiros: `addTerceiro` no `ImovelRural`. |

## 4. Mudanças de comportamento

1. **Modelo imutável.** Na 1.x, os objetos tinham setters (Lombok). Na 2.0, não há setters. Para alterar, use `toBuilder()`:

   ```java
   // 1.x
   lancamento.setValorSaida(new BigDecimal("5500.00"));

   // 2.0
   Lancamento corrigido = lancamento.toBuilder()
           .valorSaida(new BigDecimal("5500.00"))
           .build();
   ```

2. **Percentuais como `BigDecimal`.** Na 1.x, `participacao` e o percentual de contraparte eram `String` no formato do arquivo (`05000`). Na 2.0, guarde o percentual como ele é (50%), e o formato `05000` é gerado na escrita:

   ```java
   // 1.x
   imovel.setParticipacao("05000");

   // 2.0
   ImovelRural.builder().participacao(new BigDecimal("50.00"))
   ```

3. **Telefones como `String`.** A 1.x guardava o telefone como `Long`, e o zero à esquerda se perdia. Na 2.0, o campo é `String`.

4. **`QTD_LIN` calculado.** O valor informado em `EncerramentoArquivo.quantidadeLinhas` é ignorado na escrita. O arquivo sempre sai com o total de linhas.

5. **Erros de validação como lista.** Na 1.x, a validação lançava `ServiceException`, devolvia `false` ou `null`, ou engolia a falha. Na 2.0:

   ```java
   List<ErroValidacao> erros = LcdprValidator.validate(lcdpr);
   for (ErroValidacao erro : erros) {
       System.out.println(erro.linha() + ": " + erro.mensagem());
   }
   ```

6. **Exceções.** Falhas de leitura, de escrita, de formatação e de JSON inválido lançam `LcdprException`, que não é verificada. Quando o problema está em uma linha, `linha()` informa o número dela.

7. **Encoding explícito.** A 1.x gravava e lia em UTF-8 sem opção. Na 2.0, o `Charset` é parâmetro. A biblioteca não escolhe: confira o encoding que o programa da RFB aceita.

8. **Caminho do arquivo.** A 1.x gravava `LCDPR_<data>.txt` no diretório atual. Na 2.0, o caminho é passado na chamada.

9. **JSON.**
   - Os nomes dos campos são minúsculos, como no manual (`cad_itr`, `caepf`).
   - Todos os valores são texto. A 1.x escrevia números sem aspas, e o zero à esquerda se perdia. Na 1.x, o CPF saía como `"cpf": 11111111191`; na 2.0, sai como `"cpf": "11111111191"`.

   - A leitura aceita as chaves da 1.x (`cad_ITR`, `CAEPF`), sem diferenciar maiúsculas de minúsculas, e aceita valores numéricos.

10. **0045 dentro do imóvel.** O terceiro (0045) pertence ao imóvel. No modelo, é `ImovelRural.terceiros`. No JSON, é `exploracao_imoveis_rurais`, dentro do objeto do imóvel, como a 1.x já gerava no código. O README da 1.x mostrava esse campo fora do imóvel, e a 2.0 segue o código.

11. **Q200 só com meses de lançamento.** A 1.x gerava os 12 meses do período, inclusive os zerados. A 2.0 gera um registro para cada mês com lançamento, em ordem crescente. Se o programa da RFB exigir os 12 meses, confira antes de migrar.

12. **Enum com código inválido.** A 1.x trocava código inválido por um valor padrão. Na 2.0, `fromCodigo` lança erro.

13. **Valores monetários.** Na 2.0, valores são `BigDecimal` com duas casas, arredondados (HALF_UP). Valor negativo é recusado na escrita.

## 5. Sem herança de `Lcdpr`: componha

Na 1.x, o consumidor podia estender `Lcdpr` (por exemplo, `DocumentoLcdpr extends Lcdpr`), e o conversor escolhia a subclasse pelo tipo. Na 2.0, o `Lcdpr` é um `record` final. Para carregar dados extras, componha:

```java
public final class DocumentoCliente {

    private final Lcdpr lcdpr;
    private final String identificadorInterno;

    public DocumentoCliente(Lcdpr lcdpr, String identificadorInterno) {
        this.lcdpr = lcdpr;
        this.identificadorInterno = identificadorInterno;
    }

    public Lcdpr lcdpr() {
        return lcdpr;
    }

    public String identificadorInterno() {
        return identificadorInterno;
    }
}
```

## 6. Checklist de migração

- [ ] Trocar a dependência por `io.github.tiagoadmstz.lcdpr`, com o artefato da versão do Java.
- [ ] Configurar o repositório e o token do GitHub Packages.
- [ ] Trocar os imports (pacote, classes e enums) pelos nomes da tabela.
- [ ] Trocar os setters por `toBuilder()`.
- [ ] Guardar percentuais como `BigDecimal` (50.00) e telefones como `String`.
- [ ] Trocar `recalculateBlockQ()` por `LcdprCalculator.recalculate(...)` e usar o retorno.
- [ ] Trocar as checagens por `LcdprValidator.validate(...)` e tratar a lista de erros.
- [ ] Passar o `Charset` explicitamente, conferindo o exigido pelo programa da RFB.
- [ ] Passar o caminho do arquivo explicitamente, em vez de contar com o diretório atual.
- [ ] Tratar `LcdprException` nas leituras e escritas.
- [ ] Confirmar se o sistema espera o Q200 com os 12 meses ou só com os meses de lançamento.
- [ ] Trocar a herança de `Lcdpr` por composição.
