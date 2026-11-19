---
name: review-guidelines
description: Roteiro de revisão de código e de pull request do lcdpr. Usar quando pedirem para revisar um PR, uma branch, um diff ou um conjunto de arquivos deste repositório, antes de dar qualquer parecer.
---

# Revisão de código — lcdpr

A revisão usa **todas** as regras aplicáveis: ler também as skills `code-style` e `leiaute-lcdpr` inteiras e conferir cada arquivo tocado.

## 1. Preparação

1. Delimitar o diff: `git diff --stat <base>...HEAD` (base = branch principal `main`, salvo pedido).
2. Ler os arquivos tocados **inteiros**, não só as linhas alteradas.
3. Rodar o build: `./mvnw -B verify`. Os três módulos precisam passar, cada um na sua JVM (conferir as linhas `Toolchain in maven-surefire-plugin`).

## 2. Paridade entre os módulos

- [ ] A mudança aparece nos **três** módulos (`lcdpr-core`, `lcdpr-java11`, `lcdpr-java8`).
- [ ] Os mesmos testes e as mesmas amostras nos três.
- [ ] **API pública idêntica.** Comparar com `javap`:
  ```bash
  mkdir -p target
  for m in core java11 java8; do
    jar=$(ls lcdpr-$m/target/lcdpr-$m-*.jar | grep -v -e sources -e javadoc)
    unzip -Z1 "$jar" | grep '\.class$' | grep -v 'module-info' | sed 's/\.class$//; s#/#.#g' | sort \
      | xargs javap -public -cp "$jar" | grep -v '^Compiled from' > target/api-$m.txt
  done
  diff target/api-core.txt target/api-java11.txt; diff target/api-core.txt target/api-java8.txt
  ```
  Diferenças aceitas: tipos do Jackson (`tools.jackson.*` × `com.fasterxml.jackson.*`) e `record` × classe `final` (`extends java.lang.Record`, `final` de `equals`/`hashCode`/`toString`), com os mesmos nomes de acessores. `java11` × `java8` têm de sair idênticos.
- [ ] Sintaxe rebaixada conforme a tabela da `code-style`.

## 3. Checagens por busca

Nas linhas adicionadas (`git diff <base>...HEAD -U0 | grep '^+'`):

| Procurar | Por quê |
|---|---|
| `? .* ? ` na mesma expressão | Ternário aninhado |
| `^import .*\*;` | Import com asterisco |
| `io\.github\.[a-z.]+\.[A-Z]` fora de `import`/`package` | Nome qualificado no corpo |
| `new BigDecimal\([0-9.]+\)`, `double`, `float` em valor | Dinheiro fora de `BigDecimal` exato |
| `setScale\(\d\)` sem `RoundingMode` | Exceção com mais casas |
| `Long`/`Integer` em CPF, CNPJ, CEP, código, agência, conta | Perde o zero à esquerda |
| `getDeclaredFields`, `setAccessible`, `invoke(` | Reflexão no writer/reader |
| `contains\("Q100"\)`, `split("\\|")` sem limite `-1` | Registro achado por texto; campos vazios no fim descartados |
| `"\r\n"` ou `'|'` espalhados fora da constante | Separador/terminador repetidos |
| `default:` que devolve constante em `fromCodigo` | Código inválido aceito em silêncio |
| `catch \(.*\) \{\s*\}`, `printStackTrace` | Exceção engolida |
| `set[A-Z]\w*\(` em modelo | Modelo mutável |
| `lombok`, `gson`, `commons` | Dependência banida |
| `Claude`, `Gemini`, `IA`, `AI`, `assistente`, `gerado` | Rastro de IA |
| `<!--` em `pom.xml` | Comentário em arquivo de build |
| CPF/CNPJ de 11/14 dígitos fora das amostras do manual | Possível dado real |

## 4. Corretude do leiaute

- [ ] Ordem e quantidade de campos de cada registro iguais às do manual.
- [ ] Cada linha termina em CRLF; sem `|` no início nem no fim.
- [ ] Decimais implícitos certos (`10.000,00` → `1000000`; percentual N5,2).
- [ ] 0045 logo após o 0040 a que pertence, e o 0040 repetido mantém os seus 0045.
- [ ] `QTD_LIN` calculado, contando todas as linhas, inclusive o 9999.
- [ ] Saldos do Q100 e do Q200 batem com a regra (zero no início do ano, cumulativo, valor absoluto + natureza).
- [ ] Gerar, validar e calcular não alteram o modelo de entrada.
- [ ] Ida e volta (ler → escrever) reproduz a amostra byte a byte.
- [ ] Regra fiscal nova tem fonte (seção do manual) ou decisão registrada do usuário.

## 5. Documentação

- [ ] `CHANGELOG.md` com a mudança em `[Não Lançado]` quando muda comportamento ou API.
- [ ] README atualizado se mudou o uso.
- [ ] `docs/roadmap.md` atualizado se a fase andou ou uma pergunta foi respondida.

## 6. Parecer

Achados ordenados por gravidade, cada um com `arquivo:linha`, o problema e a correção sugerida. Separar o que bloqueia do que é sugestão. Sem elogio genérico.
