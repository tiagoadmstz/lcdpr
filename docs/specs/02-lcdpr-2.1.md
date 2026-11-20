# Spec 02: lcdpr 2.1 (Exemplos e Validações Adicionais)

Evoluções da versão 2.1.0 do LCDPR baseadas nos itens de backlog pós-2.0.
Regras do leiaute: skill `leiaute-lcdpr`. Estilo: skill `code-style`.
Referência de exemplos: `C:\Users\fastw\DEV\hl7-handler\hl7-handler-examples`.

## 1. Objetivo

1. **Validação do Dígito Verificador do CAEPF**:
   - Disponibilizar `LcdprValidator.caepfValido(String)`: 14 dígitos numéricos (CPF do titular, ordem de 3 dígitos e 2 DV).
   - Algoritmo (fonte comunitária, sem documento oficial da RFB; ver pergunta P6 no roadmap): DV1 e DV2 por módulo 11 com pesos 9, 8, 7, 6, 5, 4, 3, 2, 9, 8, 7, 6, 5 a partir da unidade; resto 10 vira 0; o DV2 usa a base mais o DV1; o par final é `(DV1*10 + DV2 + 12) % 100`. Vetores: `29311861000184` e `46246258600298`.
   - O exemplo do manual da RFB (`12345678901234`, seção 3.5) tem DV sintético; por isso a conferência só roda em `LcdprValidator.validate(lcdpr, true)`, e o modo padrão não a aplica. CAEPF vazio não é conferido.

2. **Coerência Município IBGE × UF**:
   - No `0030` e no `0040` (neste, apenas para imóvel no Brasil), quando `UF` e `COD_MUN` estiverem preenchidos:
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
