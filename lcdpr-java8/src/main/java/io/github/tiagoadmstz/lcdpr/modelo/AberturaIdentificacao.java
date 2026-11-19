package io.github.tiagoadmstz.lcdpr.modelo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Bloco 0: registros 0000, 0010, 0030, 0040 (com os 0045 de cada imóvel) e 0050.
 */
public final class AberturaIdentificacao {

    private final IdentificacaoPessoaFisica identificacao;
    private final ParametrosTributacao parametrosTributacao;
    private final DadosCadastrais dadosCadastrais;
    private final List<ImovelRural> imoveisRurais;
    private final List<ContaBancaria> contasBancarias;

    /**
     * Copia as listas (nulas viram vazias).
     */
    public AberturaIdentificacao(
            IdentificacaoPessoaFisica identificacao,
            ParametrosTributacao parametrosTributacao,
            DadosCadastrais dadosCadastrais,
            List<ImovelRural> imoveisRurais,
            List<ContaBancaria> contasBancarias) {
        imoveisRurais = copiar(imoveisRurais);
        contasBancarias = copiar(contasBancarias);
        this.identificacao = identificacao;
        this.parametrosTributacao = parametrosTributacao;
        this.dadosCadastrais = dadosCadastrais;
        this.imoveisRurais = imoveisRurais;
        this.contasBancarias = contasBancarias;
    }

    /** Bloco 0, registro 0000. */
    public IdentificacaoPessoaFisica identificacao() {
        return identificacao;
    }

    /** Registro 0010. */
    public ParametrosTributacao parametrosTributacao() {
        return parametrosTributacao;
    }

    /** Registro 0030. */
    public DadosCadastrais dadosCadastrais() {
        return dadosCadastrais;
    }

    /** Imóveis rurais (registros 0040), na ordem do arquivo. */
    public List<ImovelRural> imoveisRurais() {
        return imoveisRurais;
    }

    /** Contas bancárias (registros 0050), na ordem do arquivo. */
    public List<ContaBancaria> contasBancarias() {
        return contasBancarias;
    }

    private static <T> List<T> copiar(List<T> lista) {
        if (lista == null) {
            return Collections.emptyList();
        }
        for (T elemento : lista) {
            Objects.requireNonNull(elemento);
        }
        return Collections.unmodifiableList(new ArrayList<>(lista));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AberturaIdentificacao that = (AberturaIdentificacao) o;
        return Objects.equals(identificacao, that.identificacao)
                && Objects.equals(parametrosTributacao, that.parametrosTributacao)
                && Objects.equals(dadosCadastrais, that.dadosCadastrais)
                && Objects.equals(imoveisRurais, that.imoveisRurais)
                && Objects.equals(contasBancarias, that.contasBancarias);
    }

    @Override
    public int hashCode() {
        return Objects.hash(identificacao, parametrosTributacao, dadosCadastrais, imoveisRurais, contasBancarias);
    }

    @Override
    public String toString() {
        return "AberturaIdentificacao[identificacao=" + identificacao
                + ", parametrosTributacao=" + parametrosTributacao
                + ", dadosCadastrais=" + dadosCadastrais
                + ", imoveisRurais=" + imoveisRurais
                + ", contasBancarias=" + contasBancarias
                + "]";
    }

    /**
     * Cria um builder com os defaults do leiaute.
     *
     * @return builder vazio
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Cria um builder com os valores deste registro.
     *
     * @return novo builder
     */
    public Builder toBuilder() {
        Builder builder = new Builder();
        builder.identificacao = identificacao;
        builder.parametrosTributacao = parametrosTributacao;
        builder.dadosCadastrais = dadosCadastrais;
        builder.imoveisRurais = new ArrayList<>(imoveisRurais);
        builder.contasBancarias = new ArrayList<>(contasBancarias);
        return builder;
    }

    /** Construtor fluente de AberturaIdentificacao. */
    public static final class Builder {

        private IdentificacaoPessoaFisica identificacao;
        private ParametrosTributacao parametrosTributacao;
        private DadosCadastrais dadosCadastrais;
        private List<ImovelRural> imoveisRurais = new ArrayList<>();
        private List<ContaBancaria> contasBancarias = new ArrayList<>();

        /** Bloco 0, registro 0000. */
        public Builder identificacao(IdentificacaoPessoaFisica identificacao) {
            this.identificacao = identificacao;
            return this;
        }

        /** Registro 0010. */
        public Builder parametrosTributacao(ParametrosTributacao parametrosTributacao) {
            this.parametrosTributacao = parametrosTributacao;
            return this;
        }

        /** Registro 0030. */
        public Builder dadosCadastrais(DadosCadastrais dadosCadastrais) {
            this.dadosCadastrais = dadosCadastrais;
            return this;
        }

        /** Substitui a lista de imóveis rurais (registros 0040). */
        public Builder imoveisRurais(List<ImovelRural> imoveisRurais) {
            this.imoveisRurais = imoveisRurais == null ? new ArrayList<>() : new ArrayList<>(imoveisRurais);
            return this;
        }

        /** Acrescenta um imóvel rural (registro 0040). */
        public Builder addImovelRural(ImovelRural imovelRural) {
            this.imoveisRurais.add(imovelRural);
            return this;
        }

        /** Substitui a lista de contas bancárias (registros 0050). */
        public Builder contasBancarias(List<ContaBancaria> contasBancarias) {
            this.contasBancarias = contasBancarias == null ? new ArrayList<>() : new ArrayList<>(contasBancarias);
            return this;
        }

        /** Acrescenta uma conta bancária (registro 0050). */
        public Builder addContaBancaria(ContaBancaria contaBancaria) {
            this.contasBancarias.add(contaBancaria);
            return this;
        }

        /**
         * Monta o registro sem validar o conteúdo; validar é papel do LcdprValidator.
         *
         * @return AberturaIdentificacao montado
         */
        public AberturaIdentificacao build() {
            return new AberturaIdentificacao(
                    identificacao,
                    parametrosTributacao,
                    dadosCadastrais,
                    imoveisRurais,
                    contasBancarias
            );
        }
    }
}
