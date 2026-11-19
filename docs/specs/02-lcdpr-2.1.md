# Spec 02: lcdpr 2.1 (Exemplos e Validações Adicionais)

Evoluções da versão 2.1.0 do LCDPR baseadas nos itens de backlog pós-2.0.
Regras do leiaute: skill `leiaute-lcdpr`. Estilo: skill `code-style`.
Referência de exemplos: `C:\Users\fastw\DEV\hl7-handler\hl7-handler-examples`.

## 1. Objetivo

1. **Validação do Dígito Verificador do CAEPF**:
   - Disponibilizar método utilitário `LcdprValidator.caepfValido(String)` implementando o algoritmo Módulo 11 da RFB (14 dígitos: 9 base + 3 ordem + 2 DV com pesos 2 a 9).
   - Validação integrada: como o exemplo fictício da RFB (`docs/leiaute/manual-de-preenchimento-do-lcdpr-1-3.pdf`, seção 3.5) utiliza o valor `12345678901234` com DV inválido, a validação de DV do CAEPF é aplicada quando ativada ou quando o CAEPF possui padrão numérico válido, evitando quebrar a amostra oficial do manual no modo padrão.

2. **Coerência Município IBGE × UF**:
   - No `0030` e `0040`, quando `PAIS` for BR e `UF` e `COD_MUN` estiverem presentes:
   - Validar se os 2 primeiros dígitos do código de município IBGE (7 dígitos) correspondem à UF informada (ex: `SP` = 35, `MG` = 31, `DF` = 53, etc.).

3. **Módulo `lcdpr-examples` (perfil `-Pexamples`)**:
   - Submódulo agregado via profile `-Pexamples` no `pom.xml` pai (evita acoplamento no build principal do CI de bibliotecas).
   - `example-pure-java`: Exemplo completo em Java 17 usando `lcdpr-core`. Demonstra:
     - Construção fluente do `Lcdpr` com builder imutável;
     - Cálculo de saldos acumulados com `LcdprCalculator.recalculate`;
     - Validação estrutural e fiscal com `LcdprValidator.validate`;
     - Escrita em pipe delimitado (`LcdprWriter.write`) e exportação JSON (`LcdprJson.toJson`);
     - Leitura de arquivo e impressão de relatório/extrato.
   - `example-pure-java8`: Exemplo equivalente em Java 8 consumindo `lcdpr-java8`, comprovando a interoperabilidade e suporte a ambientes legados.

## 2. API Pública

Novos métodos no pacote `io.github.tiagoadmstz.lcdpr`:

```java
public final class LcdprValidator {
    // utilitários públicos
    public static boolean caepfValido(String caepf);
    public static boolean municipioCoerenteComUf(String codigoMunicipio, String uf);
}
```

## 3. Fatias

| Fatia | Módulo | Conteúdo |
|---|---|---|
| E1 | `lcdpr-core` | `caepfValido`, coerência UF × Município em `LcdprValidator` e testes |
| E2 | `lcdpr-java11` | Porte de E1 para Java 11 |
| E3 | `lcdpr-java8` | Porte de E1 para Java 8 |
| E4 | `lcdpr-examples` | Criação do módulo pai e dos exemplos pure Java (17 e 8) com `-Pexamples` |
| E5 | Raiz / Docs | Atualização de README, CHANGELOG, roadmap e verificação geral |
