package io.github.tiagoadmstz.lcdpr.modelo;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import io.github.tiagoadmstz.lcdpr.tabela.TipoExploracao;

/**
 * Registro 0040: cadastro dos imóveis rurais.
 */
public record ImovelRural(
        String codigoImovel,
        String pais,
        String moeda,
        String cadastroItr,
        String caepf,
        String inscricaoEstadual,
        String nome,
        String endereco,
        String numero,
        String complemento,
        String bairro,
        String uf,
        String codigoMunicipio,
        String cep,
        TipoExploracao tipoExploracao,
        BigDecimal participacao,
        List<Terceiro> terceiros
) {

    /** Copia as listas (nulas viram vazias) e normaliza os valores para escala 2. */
    public ImovelRural {
        participacao = participacao == null ? null : participacao.setScale(2, RoundingMode.HALF_UP);
        terceiros = terceiros == null ? List.of() : List.copyOf(terceiros);
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
        builder.codigoImovel = codigoImovel;
        builder.pais = pais;
        builder.moeda = moeda;
        builder.cadastroItr = cadastroItr;
        builder.caepf = caepf;
        builder.inscricaoEstadual = inscricaoEstadual;
        builder.nome = nome;
        builder.endereco = endereco;
        builder.numero = numero;
        builder.complemento = complemento;
        builder.bairro = bairro;
        builder.uf = uf;
        builder.codigoMunicipio = codigoMunicipio;
        builder.cep = cep;
        builder.tipoExploracao = tipoExploracao;
        builder.participacao = participacao;
        builder.terceiros = new ArrayList<>(terceiros);
        return builder;
    }

    /** Construtor fluente de ImovelRural. */
    public static final class Builder {

        private String codigoImovel;
        private String pais = "BR";
        private String moeda = "BRL";
        private String cadastroItr;
        private String caepf;
        private String inscricaoEstadual;
        private String nome;
        private String endereco;
        private String numero;
        private String complemento;
        private String bairro;
        private String uf;
        private String codigoMunicipio;
        private String cep;
        private TipoExploracao tipoExploracao;
        private BigDecimal participacao;
        private List<Terceiro> terceiros = new ArrayList<>();

        /** Registro 0040, campo COD_IMOVEL. */
        public Builder codigoImovel(String codigoImovel) {
            this.codigoImovel = codigoImovel;
            return this;
        }

        /** Registro 0040, campo PAIS. */
        public Builder pais(String pais) {
            this.pais = pais;
            return this;
        }

        /** Registro 0040, campo MOEDA. */
        public Builder moeda(String moeda) {
            this.moeda = moeda;
            return this;
        }

        /** Registro 0040, campo CAD_ITR. */
        public Builder cadastroItr(String cadastroItr) {
            this.cadastroItr = cadastroItr;
            return this;
        }

        /** Registro 0040, campo CAEPF. */
        public Builder caepf(String caepf) {
            this.caepf = caepf;
            return this;
        }

        /** Registro 0040, campo INSCR_ESTADUAL. */
        public Builder inscricaoEstadual(String inscricaoEstadual) {
            this.inscricaoEstadual = inscricaoEstadual;
            return this;
        }

        /** Registro 0040, campo NOME_IMOVEL. */
        public Builder nome(String nome) {
            this.nome = nome;
            return this;
        }

        /** Registro 0040, campo ENDERECO. */
        public Builder endereco(String endereco) {
            this.endereco = endereco;
            return this;
        }

        /** Registro 0040, campo NUM. */
        public Builder numero(String numero) {
            this.numero = numero;
            return this;
        }

        /** Registro 0040, campo COMPL. */
        public Builder complemento(String complemento) {
            this.complemento = complemento;
            return this;
        }

        /** Registro 0040, campo BAIRRO. */
        public Builder bairro(String bairro) {
            this.bairro = bairro;
            return this;
        }

        /** Registro 0040, campo UF. */
        public Builder uf(String uf) {
            this.uf = uf;
            return this;
        }

        /** Registro 0040, campo COD_MUN. */
        public Builder codigoMunicipio(String codigoMunicipio) {
            this.codigoMunicipio = codigoMunicipio;
            return this;
        }

        /** Registro 0040, campo CEP. */
        public Builder cep(String cep) {
            this.cep = cep;
            return this;
        }

        /** Registro 0040, campo TIPO_EXPLORACAO. */
        public Builder tipoExploracao(TipoExploracao tipoExploracao) {
            this.tipoExploracao = tipoExploracao;
            return this;
        }

        /** Registro 0040, campo PARTICIPACAO. */
        public Builder participacao(BigDecimal participacao) {
            this.participacao = participacao;
            return this;
        }

        /** Substitui a lista de terceiros (registros 0045). */
        public Builder terceiros(List<Terceiro> terceiros) {
            this.terceiros = terceiros == null ? new ArrayList<>() : new ArrayList<>(terceiros);
            return this;
        }

        /** Acrescenta um terceiro (registro 0045). */
        public Builder addTerceiro(Terceiro terceiro) {
            this.terceiros.add(terceiro);
            return this;
        }

        /**
         * Monta o registro sem validar o conteúdo; validar é papel do LcdprValidator.
         *
         * @return ImovelRural montado
         */
        public ImovelRural build() {
            return new ImovelRural(
                    codigoImovel,
                    pais,
                    moeda,
                    cadastroItr,
                    caepf,
                    inscricaoEstadual,
                    nome,
                    endereco,
                    numero,
                    complemento,
                    bairro,
                    uf,
                    codigoMunicipio,
                    cep,
                    tipoExploracao,
                    participacao,
                    terceiros
            );
        }
    }
}
