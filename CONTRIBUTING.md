# Guia de Contribuição

Obrigado por considerar contribuir para o **LCDPR**! Este documento reúne as orientações para fazer contribuições ao projeto.

O projeto é uma biblioteca fiscal: uma regra errada no arquivo gera uma declaração errada. Por isso, toda regra do leiaute tem fonte no manual oficial, e a dúvida é levada ao mantenedor em vez de ser suposta.

## Reportando bugs

Antes de criar um relatório de bug, verifique se o problema já não foi reportado. Se encontrar um bug:

1. **Descreva o comportamento observado** e o **comportamento esperado**
2. **Forneça um exemplo mínimo**: um trecho de arquivo fictício ou um código que reproduza o problema
3. **Descreva o ambiente**:
   - Versão do Java (ex: Java 17)
   - Versão do Maven
   - Sistema operacional
   - Artefato e versão do LCDPR (ex: `lcdpr-core` 2.0.0)
4. **Inclua stack traces** se aplicável

**Não envie arquivo LCDPR real.** Ele contém CPF, CNPJ, nome, endereço, contas bancárias e valores de livro caixa. Troque esses dados por valores inventados antes de anexar qualquer coisa a uma issue.

### Template para report de bug

```markdown
**Descrição do bug**
Uma descrição clara do que é o bug.

**Passos para reproduzir**
1. ...
2. ...

**Comportamento esperado**
O que deveria acontecer?

**Comportamento observado**
O que realmente aconteceu?

**Ambiente**
- Java: [versão]
- Maven: [versão]
- SO: [ex: Windows 11, Ubuntu 24.04]
- Artefato e versão: [ex: lcdpr-core 2.0.0]

**Stack trace (se disponível)**
```
...
```
```

## Divergência com o leiaute

Se o código não segue o manual (formato, campo, obrigatoriedade, validação ou cálculo), abra uma issue do tipo **Divergência com o leiaute**. Ela pede o registro, o campo, a seção do manual e um exemplo fictício. Se a regra não estiver no manual, diga isso na issue.

## Sugerindo melhorias

Se tiver uma ideia para uma nova funcionalidade:

1. Verifique se a sugestão já não existe
2. Forneça uma **descrição clara** do comportamento esperado
3. Liste **exemplos de uso**
4. Descreva como isso seria **benéfico** para o projeto

## Pull request

### Antes de começar

1. **Fork** o repositório
2. **Clone** seu fork localmente
3. **Crie uma branch** baseada em `main`:
   ```bash
   git checkout -b feature/descricao-clara
   git checkout -b fix/corrige-problema-xyz
   ```

### Desenvolvendo

1. **Siga o estilo de código** do projeto (clean code, SOLID). Os três módulos são independentes: toda mudança é feita no `lcdpr-core`, depois portada para o `lcdpr-java11` e, por último, para o `lcdpr-java8`, cada um na sintaxe do seu Java mínimo. A API pública tem de ser idêntica nos três.
2. **Escreva testes** para novas funcionalidades e para cada bug corrigido. Os mesmos testes, com as mesmas amostras, passam nos três módulos.
3. **Atualize a documentação** se mudou o uso: README, guia de migração ou `docs/roadmap.md`.
4. **Execute o build** na raiz:
   ```bash
   ./mvnw -B verify
   ```
   No Windows, use `.\mvnw.cmd -B verify`. O build precisa dos JDKs 8, 11, 17 e 25 no `~/.m2/toolchains.xml`.

### Enviando seu PR

1. **Commit** só com o título, em inglês, no padrão abaixo:
   ```bash
   git commit -m "fix(writer): keep third parties when a rural property repeats"
   git commit -m "feat(validation): reject duplicated account codes"
   ```

2. **Push** para sua branch
3. **Abra um Pull Request** para `main`, preenchendo o template (`.github/pull_request_template.md`):
   - título no mesmo padrão do commit;
   - todas as seções do template, com `N/A` onde não se aplica;
   - o checklist marcado com honestidade, inclusive a aplicação nos três módulos;
   - issues relacionadas em formato de link (ex.: `[#25](https://github.com/tiagoadmstz/lcdpr/issues/25)`).
4. **Atualize o `CHANGELOG.md`** na seção `## [Não Lançado]` quando a mudança altera comportamento ou API.

### Padrões de commit

Use **conventional commits**:
- `feat:` nova funcionalidade
- `fix:` correção de bug
- `refactor:` refatoração sem mudança de comportamento
- `test:` adição ou modificação de testes
- `docs:` mudanças de documentação
- `style:` formatação, sem mudanças lógicas
- `chore:` manutenção, sem mudança de comportamento
- `build:` build e dependências
- `ci:` integração contínua
- `perf:` desempenho
- `revert:` reversão de commit anterior

Regras:
- **Só o título**, sem corpo: o contexto vai na descrição do PR.
- Em inglês, imperativo, minúsculo depois dos dois-pontos, sem ponto final.
- Escopo opcional quando ajuda: `model`, `writer`, `reader`, `json`, `validation`, `calc`, `build`, `ci`.
- Sem coautoria nem linhas de atribuição no commit e no corpo do PR.
- Poucos commits por PR, agrupados por tema.

**Exemplo:** `fix(writer): keep third parties when a rural property repeats`

## Testes

- **Toda** nova funcionalidade deve ter testes.
- **Bugs** devem ter um teste que reproduz o problema, nos três módulos.
- Use **JUnit 5** com **AssertJ**. Um método por caso, sem `@ParameterizedTest`.
- Nome do método em inglês (`shouldWriteThirdPartyAfterEachRuralProperty`) e `@DisplayName` em português.
- As amostras ficam em `src/test/resources/samples/` e são **fictícias**. Os exemplos do manual servem de base. CPF e CNPJ de teste precisam ter dígito verificador válido, mas ser inventados.
- O teste de ida e volta lê a amostra, escreve de novo e compara byte a byte, com o CRLF incluso.
- Testes não gravam fora de `target/` nem de `@TempDir`.

Exemplo:

```java
@Test
@DisplayName("Gera o arquivo de exemplo byte a byte")
void shouldWriteSampleFileExactly() {
    String gerado = LcdprWriter.write(Amostras.valido());

    assertThat(gerado).isEqualTo(lerAmostra("samples/lcdpr-valido.txt"));
}
```

## Regras fiscais

- Toda regra nova do leiaute cita a seção do manual em `docs/leiaute/` (manual de preenchimento 1.3).
- Regra que não está no manual não é suposta: pergunte ao mantenedor e registre a decisão no `docs/roadmap.md` ou na spec.
- Mudança de formato de campo, de tabela ou de cálculo de saldo sempre pede revisão do mantenedor.

## Documentação

- Atualize o `README.md` se mudou o uso público.
- Atualize o `docs/migracao-1x-2x.md` se a mudança afeta quem vem da 1.x.
- Adicione Javadoc para classes e métodos públicos, curto, citando o registro ou o campo do manual quando houver.
- Use `code-block` com exemplos reais, e dados fictícios.

## Processo de review

1. Pelo menos **um membro da equipe** revisor deve aprovar
2. **Testes** devem passar nos três módulos
3. Sem **conflitos** com a branch `main`
4. O CHANGELOG e a documentação estão atualizados quando a mudança pede

## Áreas de contribuição bem-vindas

- **Bugs**: correções de comportamentos inesperados
- **Leiaute**: divergências com o manual, com a fonte citada
- **Documentação**: guias, exemplos e traduções de erros
- **Testes**: amostras fictícias adicionais, com casos de borda do manual
- **Desempenho**: otimizações com medição antes e depois

## Como lançar uma versão

Lançamentos são feitos pelo mantenedor. A publicação é automatizada pelo workflow `release.yml`, disparado por tag `v*.*.*`.

**Versão final `X.Y.Z`:**

1. `main` com o CI verde.
2. A propriedade `revision` do `pom.xml` raiz deve ser `X.Y.Z-SNAPSHOT`.
3. No `CHANGELOG.md`, renomear `## [Não Lançado]` para `## [X.Y.Z] - AAAA-MM-DD` e criar um novo `## [Não Lançado]` vazio acima dela.
4. Commitar: `chore: release X.Y.Z`.
5. Criar e enviar a tag anotada: `git tag -a vX.Y.Z -m "vX.Y.Z"` e `git push origin vX.Y.Z`.
6. O workflow *Release* valida a tag, a `revision` e a seção do CHANGELOG, publica os artefatos no GitHub Packages e cria a GitHub Release com os três JARs e as notas da seção.
7. Commitar: `chore: start X.Y.(Z+1)-SNAPSHOT`, alterando a propriedade `revision` no `pom.xml` raiz.

**Pré-lançamento `vX.Y.Z-rc.N`:** segue os mesmos passos de tag, mas não exige seção no CHANGELOG. A GitHub Release é marcada como pré-lançamento.

## Dúvidas

Abra uma issue com o label `question`.

---

Obrigado por contribuir.
