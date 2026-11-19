package io.github.tiagoadmstz.lcdpr.modelo;

import java.util.ArrayList;
import java.util.List;

/**
 * Bloco 0: registros 0000, 0010, 0030, 0040 (com os 0045 de cada imóvel) e 0050.
 */
public record AberturaIdentificacao(
        IdentificacaoPessoaFisica identificacao,
        ParametrosTributacao parametrosTributacao,
        DadosCadastrais dadosCadastrais,
        List<ImovelRural> imoveisRurais,
        List<ContaBancaria> contasBancarias
) {

    /** Copia as listas (nulas viram vazias). */
    public AberturaIdentificacao {
        imoveisRurais = imoveisRurais == null ? List.of() : List.copyOf(imoveisRurais);
        contasBancarias = contasBancarias == null ? List.of() : List.copyOf(contasBancarias);
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
