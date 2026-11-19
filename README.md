# LCDPR

[![CI](https://github.com/tiagoadmstz/lcdpr/actions/workflows/ci.yml/badge.svg)](https://github.com/tiagoadmstz/lcdpr/actions/workflows/ci.yml)

Biblioteca Java para o **Livro Caixa Digital do Produtor Rural (LCDPR)**. Monta, grava, lê, valida e calcula o arquivo texto separado por `|` e o JSON equivalente, no leiaute 1.3.

O LCDPR foi instituído pela Instrução Normativa RFB nº 1.848, de 28 de novembro de 2018, e o leiaute 1.3 está no Ato Declaratório Executivo COPES nº 1, de 2020. A biblioteca cuida do conteúdo do arquivo. A assinatura digital com certificado ICP-Brasil e a entrega à Receita Federal ficam fora dela.

## Qual artefato usar

A biblioteca é publicada em três artefatos com a mesma API, no mesmo pacote `io.github.tiagoadmstz.lcdpr`. Cada artefato é um projeto independente, escrito na sintaxe do seu Java mínimo. Use apenas um por projeto.

| Artefato | Java | Jackson |
|---|---|---|
| `lcdpr-core` | 17 ou superior | 3 (`tools.jackson`) |
| `lcdpr-java11` | 11 a 16 | 2 (`com.fasterxml.jackson`) |
| `lcdpr-java8` | 8 | 2 (`com.fasterxml.jackson`) |

## Instalação

Os artefatos são publicados no GitHub Packages, que exige autenticação mesmo para leitura. Declare o repositório no `pom.xml` do projeto:

```xml
<repositories>
    <repository>
        <id>github</id>
        <url>https://maven.pkg.github.com/tiagoadmstz/lcdpr</url>
        <snapshots>
            <enabled>false</enabled>
        </snapshots>
    </repository>
</repositories>
```

No `~/.m2/settings.xml`, configure o servidor `github` com um **token clássico** (*Personal access token (classic)*) com o escopo `read:packages`. O registro Maven do GitHub Packages não aceita token *fine-grained*. Use uma variável de ambiente em vez de gravar o token no arquivo:

```xml
<servers>
    <server>
        <id>github</id>
        <username>SEU_USUARIO_GITHUB</username>
        <password>${env.GITHUB_PACKAGES_TOKEN}</password>
    </server>
</servers>
```

Se o `settings.xml` tiver um `<mirror>` com `<mirrorOf>*</mirrorOf>` (Nexus, Archiva, Artifactory), acrescente `,!github` ao `mirrorOf`. Sem isso, o pedido ao GitHub Packages é desviado para o mirror.

Adicione o artefato correspondente à versão do Java do projeto:

```xml
<dependency>
    <groupId>io.github.tiagoadmstz.lcdpr</groupId>
    <artifactId>lcdpr-core</artifactId>
    <version>2.0.0</version>
</dependency>
```

Troque `lcdpr-core` por `lcdpr-java11` ou `lcdpr-java8` conforme a tabela acima.

## Uso

Os exemplos usam dados fictícios, os mesmos das amostras do projeto.

### Montar o registro

Os registros são imutáveis e se montam com `builder()`. O cálculo do Q100 e do Q200 e o registro 9999 não precisam de valores informados: o `QTD_LIN` é calculado na escrita.

```java
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.List;

import io.github.tiagoadmstz.lcdpr.ErroValidacao;
import io.github.tiagoadmstz.lcdpr.Lcdpr;
import io.github.tiagoadmstz.lcdpr.LcdprCalculator;
import io.github.tiagoadmstz.lcdpr.LcdprJson;
import io.github.tiagoadmstz.lcdpr.LcdprReader;
import io.github.tiagoadmstz.lcdpr.LcdprValidator;
import io.github.tiagoadmstz.lcdpr.LcdprWriter;
import io.github.tiagoadmstz.lcdpr.modelo.AberturaIdentificacao;
import io.github.tiagoadmstz.lcdpr.modelo.ContaBancaria;
import io.github.tiagoadmstz.lcdpr.modelo.DadosCadastrais;
import io.github.tiagoadmstz.lcdpr.modelo.DemonstrativoResultado;
import io.github.tiagoadmstz.lcdpr.modelo.EncerramentoArquivo;
import io.github.tiagoadmstz.lcdpr.modelo.IdentificacaoPessoaFisica;
import io.github.tiagoadmstz.lcdpr.modelo.ImovelRural;
import io.github.tiagoadmstz.lcdpr.modelo.Lancamento;
import io.github.tiagoadmstz.lcdpr.modelo.ParametrosTributacao;
import io.github.tiagoadmstz.lcdpr.tabela.FormaApuracao;
import io.github.tiagoadmstz.lcdpr.tabela.TipoDocumento;
import io.github.tiagoadmstz.lcdpr.tabela.TipoExploracao;
import io.github.tiagoadmstz.lcdpr.tabela.TipoLancamento;

Lcdpr lcdpr = Lcdpr.builder()
        .bloco0(AberturaIdentificacao.builder()
                .identificacao(IdentificacaoPessoaFisica.builder()
                        .cpf("11144477735")
                        .nome("JOSÉ DA SILVA")
                        .dataInicial(LocalDate.of(2019, 1, 1))
                        .dataFinal(LocalDate.of(2019, 12, 31))
                        .build())
                .parametrosTributacao(ParametrosTributacao.builder()
                        .formaApuracao(FormaApuracao.LIVRO_CAIXA)
                        .build())
                .dadosCadastrais(DadosCadastrais.builder()
                        .endereco("RUA TESTE")
                        .numero("1234")
                        .bairro("BAIRRO LCDPR")
                        .uf("DF")
                        .codigoMunicipio("5300108")
                        .cep("71000000")
                        .telefone("6133333333")
                        .email("testeLCDPR@LCDPR.com.br")
                        .build())
                .addImovelRural(ImovelRural.builder()
                        .codigoImovel("001")
                        .cadastroItr("12345678")
                        .caepf("12345678901234")
                        .nome("Fazenda Tudo Certo")
                        .endereco("Rodovia BR 999, Km 3000")
                        .bairro("Distrito do Meio")
                        .uf("DF")
                        .codigoMunicipio("5300108")
                        .cep("71000000")
                        .tipoExploracao(TipoExploracao.INDIVIDUAL)
                        .participacao(new BigDecimal("100.00"))
                        .build())
                .addContaBancaria(ContaBancaria.builder()
                        .codigoConta("001")
                        .banco("999")
                        .nomeBanco("Banco LCDPR")
                        .agencia("1234")
                        .numeroConta("0000000123456789")
                        .build())
                .build())
        .blocoQ(DemonstrativoResultado.builder()
                .addLancamento(Lancamento.builder()
                        .data(LocalDate.of(2019, 1, 2))
                        .codigoImovel("001")
                        .codigoConta("001")
                        .numeroDocumento("2")
                        .tipoDocumento(TipoDocumento.RECIBO)
                        .historico("Venda de 100 sacas de milho")
                        .cpfCnpjParticipante("12345678909")
                        .tipoLancamento(TipoLancamento.RECEITA)
                        .valorEntrada(new BigDecimal("10000.00"))
                        .build())
                .addLancamento(Lancamento.builder()
                        .data(LocalDate.of(2019, 1, 15))
                        .codigoImovel("001")
                        .codigoConta("001")
                        .numeroDocumento("3")
                        .tipoDocumento(TipoDocumento.NOTA_FISCAL)
                        .historico("Pagamento de sementes e outros insumos")
                        .cpfCnpjParticipante("11222333000181")
                        .tipoLancamento(TipoLancamento.DESPESA_CUSTEIO_INVESTIMENTO)
                        .valorSaida(new BigDecimal("5000.00"))
                        .build())
                .build())
        .bloco9(EncerramentoArquivo.builder()
                .nomeContador("JOSE DE SOUZA")
                .cpfCnpjContador("12345678909")
                .crcContador("AL123456O")
                .emailContador("testeLCDPR@LCDPR.com.br")
                .telefoneContador("6133333333")
                .build())
        .build();
```

### Calcular, validar e gravar

`LcdprCalculator.recalculate` devolve um novo registro, com os saldos do bloco Q preenchidos. O registro de entrada não muda. `LcdprValidator.validate` devolve a lista de erros, vazia quando o registro está válido.

```java
Lcdpr calculado = LcdprCalculator.recalculate(lcdpr);

// Validação padrão (compatível com dados oficiais do manual da RFB)
List<ErroValidacao> erros = LcdprValidator.validate(calculado);

// Validação estrita (ativa verificação de dígito verificador do CAEPF)
List<ErroValidacao> errosEstritos = LcdprValidator.validate(calculado, true);

for (ErroValidacao erro : erros) {
    System.out.println("linha " + erro.linha() + ": " + erro.mensagem());
}

if (erros.isEmpty()) {
    LcdprWriter.write(calculado, Paths.get("LCDPR-2019.txt"), StandardCharsets.UTF_8);
}
```

- `ErroValidacao` tem `linha()`, `registro()`, `campo()` e `mensagem()`. A linha é a posição que o registro teria no arquivo gerado; `0` indica erro que não é de um registro.
- O `LcdprWriter` não valida as regras do leiaute (obrigatoriedade, referências entre registros). Ele só recusa valor que não pode ser formatado, como valor negativo ou texto com `|`. Por isso, valide antes de gravar.
- `LcdprWriter.write(calculado)` devolve o conteúdo como `String`, sem gravar em arquivo.
- O validador confere o dígito verificador de CPF e CNPJ, a coerência entre a UF e o prefixo do código de município IBGE (0030 e 0040) e, sob demanda, o DV do CAEPF via `LcdprValidator.caepfValido(String)`.

### Ler e converter para JSON

```java
Lcdpr lido = LcdprReader.read(Paths.get("LCDPR-2019.txt"), StandardCharsets.UTF_8);

String json = LcdprJson.toJson(lido);
Lcdpr doJson = LcdprJson.fromJson(json);
```

`LcdprReader.read` também aceita um `String` ou um `Reader`. `LcdprJson` tem as mesmas variantes de entrada e saída com `Writer` e `Reader`.

Erros de leitura, escrita e de JSON inválido lançam `LcdprException`, que não é verificada. Quando o erro está em uma linha do arquivo, `linha()` informa o número dela.

Para alterar um registro já montado, use `toBuilder()`. O original não muda:

```java
Lancamento original = calculado.blocoQ().lancamentos().get(0);
Lancamento corrigido = original.toBuilder()
        .valorSaida(new BigDecimal("5500.00"))
        .build();
```

## Arquivo gerado

Amostra `src/test/resources/samples/lcdpr-valido.txt`, com dados fictícios e o mesmo conteúdo do registro montado nos testes. Cada linha termina com CRLF, inclusive a última.

```
0000|LCDPR|0013|11144477735|JOSÉ DA SILVA|0|0||01012019|31122019
0010|1
0030|RUA TESTE|1234|BLOCO Z SALA 301|BAIRRO LCDPR|DF|5300108|71000000|6133333333|testeLCDPR@LCDPR.com.br
0040|001|BR|BRL|12345678|12345678901234|12345678901234|Fazenda Tudo Certo|Rodovia BR 999, Km 3000|||Distrito do Meio|DF|5300108|71000000|2|05000
0045|001|1|12345678909|JOÃO DE SOUSA|05000
0040|002|BR|BRL|87654321|12345678901234||Sítio Boa Vista|Estrada Vicinal 10||KM 2|Zona Rural|DF|5300108|71000000|1|10000
0050|001|BR|999|Banco LCDPR|1234|0000000123456789
Q100|02012019|001|001|2|3|Venda de 100 sacas de milho|12345678909|1|1000000|000|1000000|P
Q100|15012019|001|001|3|1|Pagamento de sementes e outros insumos|11222333000181|2|000|500000|500000|P
Q100|10022019|002|000|4|3|Pagamento de diarista|12345678909|2|000|800000|300000|N
Q200|012019|1000000|500000|500000|P
Q200|022019|000|800000|300000|N
9999|JOSE DE SOUZA|12345678909|AL123456O|testeLCDPR@LCDPR.com.br|6133333333|13
```

## JSON

Trecho do bloco 0 do arquivo `src/test/resources/samples/lcdpr-valido.json`, formatado para leitura. O 0045 fica dentro do imóvel a que pertence, e campo vazio não aparece:

```json
{
  "bloco_0": {
    "identificacao_pessoa_fisica": {
      "reg": "0000",
      "nome_esc": "LCDPR",
      "cod_ver": "0013",
      "cpf": "11144477735",
      "nome": "JOSÉ DA SILVA",
      "ind_sit_ini_per": "0",
      "sit_especial": "0",
      "dt_ini": "01012019",
      "dt_fin": "31122019"
    },
    "imoveis_rurais": [
      {
        "reg": "0040",
        "cod_imovel": "001",
        "pais": "BR",
        "moeda": "BRL",
        "cad_itr": "12345678",
        "caepf": "12345678901234",
        "inscr_estadual": "12345678901234",
        "nome_imovel": "Fazenda Tudo Certo",
        "endereco": "Rodovia BR 999, Km 3000",
        "bairro": "Distrito do Meio",
        "uf": "DF",
        "cod_mun": "5300108",
        "cep": "71000000",
        "tipo_exploracao": "2",
        "participacao": "05000",
        "exploracao_imoveis_rurais": [
          {
            "reg": "0045",
            "cod_imovel": "001",
            "tipo_contraparte": "1",
            "id_contraparte": "12345678909",
            "nome_contraparte": "JOÃO DE SOUSA",
            "perc_contraparte": "05000"
          }
        ]
      }
    ]
  }
}
```

No JSON, os nomes dos campos são os do manual em minúsculas, e todos os valores são texto, como no arquivo. Na leitura, as chaves não diferenciam maiúsculas e minúsculas, então as chaves da 1.x (`cad_ITR`, `CAEPF`) continuam funcionando.

## Regras do leiaute cobertas

- **Formato do arquivo:** uma linha por registro, campos separados por `|`, sem `|` no início nem no fim da linha. O `LcdprWriter` termina cada linha com CRLF. O `LcdprReader` aceita CRLF ou LF.
- **Valores:** sem separador de milhar nem vírgula. As duas casas decimais ficam implícitas: `1.129.998,99` vira `112999899`, e zero vira `000`. Valor negativo é recusado.
- **Percentuais:** `PARTICIPACAO` e `PERC_CONTRAPARTE` são N5 com 2 casas: `50,00%` vira `05000`, e `5,20%` vira `00520`. No modelo, são `BigDecimal` (`new BigDecimal("50.00")`).
- **Datas:** `ddMMaaaa` para datas e `MMaaaa` para o mês do Q200. A leitura é estrita: data inválida gera erro.
- **CPF e CNPJ:** só dígitos, com zeros à esquerda preservados, e é conferido o dígito verificador. O CNPJ tem 14 posições e pode ser alfanumérico.
- **Registros 0045:** cada 0045 sai logo depois do 0040 a que pertence. Um imóvel repetido mantém os seus terceiros.
- **QTD_LIN:** calculado na escrita, com o total de linhas do arquivo, contando todos os registros e o próprio 9999.
- **Validações principais:**
  - obrigatórios de cada registro e presença de 0000, 0010, 0030, ao menos um 0040 e ao menos um 0050;
  - tamanho, tipo e formato dos campos;
  - `CAEPF`, `UF`, `COD_MUN` e `CEP` quando o país é BR e o imóvel é de exploração individual;
  - 0045 obrigatório quando `PARTICIPACAO` é menor que 100% ou `TIPO_EXPLORACAO` não é individual;
  - `BANCO` obrigatório para conta no Brasil;
  - `COD_IMOVEL` e `COD_CONTA` do Q100 cadastrados, aceitando `000` e `999` quando o manual admite;
  - coerência entre a UF declarada e os 2 primeiros dígitos do código de município IBGE no 0030 e no 0040;
  - `DATA` do lançamento e `MES` do Q200 dentro do período da escrituração;
  - `VL_ENTRADA` e `VL_SAIDA` não podem ser maiores que zero no mesmo lançamento;
  - `QTD_LIN` informado, quando existir, igual ao calculado;
  - conferência de DV do CAEPF sob demanda (via `LcdprValidator.validate(lcdpr, true)` ou `caepfValido(String)`).
- **Cálculo do Q100:** os lançamentos saem em ordem de data. O saldo começa em zero no início do ano e é acumulado (entradas menos saídas). O `SLD_FIN` guarda o valor absoluto, e `NAT_SLD_FIN` é `P` ou `N`.
- **Cálculo do Q200:** o Q200 cumulativo tem um registro por mês em que há lançamento, em ordem crescente. Cada `SLD_FIN` é o saldo do mês somado ao saldo final do mês anterior.

  Este é um ponto em aberto da escrituração: se o programa da RFB exigir um registro para cada mês do período, mesmo sem lançamento, o cálculo do Q200 muda.

## Charset

A biblioteca não escolhe o encoding do arquivo. `LcdprWriter.write(lcdpr, caminho, charset)` e `LcdprReader.read(caminho, charset)` recebem o `Charset` do chamador. Os exemplos usam UTF-8, mas confira o encoding que o programa de entrega da RFB aceita antes de gerar o arquivo que vai ser entregue. O tutorial de entrega está em [`docs/leiaute/`](docs/leiaute/).

## Build

O projeto tem o Maven Wrapper (`mvnw` / `mvnw.cmd`), então não é preciso instalar o Maven.

```bash
# Windows
.\mvnw.cmd -B verify

# Linux / macOS
./mvnw -B verify
```

O build compila com o JDK 25 e roda os testes de cada módulo na JVM do seu Java mínimo: `lcdpr-core` em 17 e 25, `lcdpr-java11` em 11 e `lcdpr-java8` em 8. Os JDKs são localizados pelo `~/.m2/toolchains.xml`, independentemente do `JAVA_HOME`:

```xml
<toolchains>
    <toolchain>
        <type>jdk</type>
        <provides><version>8</version></provides>
        <configuration><jdkHome>/caminho/para/jdk-8</jdkHome></configuration>
    </toolchain>
</toolchains>
```

Repita o bloco para as versões 11, 17 e 25.

Os três módulos são independentes, com código e testes próprios. Uma correção precisa ser aplicada nos três.

## Exemplos

O repositório contém o módulo `lcdpr-examples` com projetos de exemplo prontos para execução em código puro:
- `example-pure-java`: exemplo em Java 17+ usando `lcdpr-core` com records e builders.
- `example-pure-java8`: exemplo em Java 8 usando `lcdpr-java8`.

Para compilar e testar os exemplos, execute com o perfil `examples`:

```bash
./mvnw test -Pexamples
```

## Documentação

- [Histórico de alterações](CHANGELOG.md)
- [Guia de migração da 1.x para a 2.0](docs/migracao-1x-2x.md)
- [Manual de preenchimento e leiaute da RFB](docs/leiaute/), com o manual oficial `manual-de-preenchimento-do-lcdpr-1-3.pdf`
- [Guia de contribuição](CONTRIBUTING.md)
- [Política de segurança](SECURITY.md)

## Licença

Distribuído sob a licença [MIT](LICENSE).
