/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.dsc.lcdpr.components;

import br.com.dsc.lcdpr.interfaces.LcdprHandler;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.*;

import java.io.Serializable;

/**
 * Registro 0030: Dados Cadastrais
 * Apresenta os dados cadastrais do produtor rural pessoa física declarante.
 * Ocorrência [1:1]
 *
 * @author Tiago D.
 */
@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@JsonPropertyOrder({
        "registro", "endereco", "numero", "complemento", "bairro",
        "uf", "codigoMunicipio", "cep", "numeroTelefone", "email"
})
public class Contribuinte implements Serializable, LcdprHandler {

    private static final long serialVersionUID = 8869191708501012066L;
    @Builder.Default
    @JsonProperty("reg")
    private String registro = "0030"; //t = 4, o = sim, p = 0030
    private String endereco; //t = 150, o = sim
    @JsonProperty("num")
    private String numero; //t = 6, o = sim
    @JsonProperty("compl")
    private String complemento; //t = 50, o = não
    private String bairro; //t = 50, o = sim
    private String uf; //t = 2, o = sim  (http://sped.rfb.gov.br/pasta/show/1932)
    @JsonProperty("cod_mun")
    private String codigoMunicipio; //t = 7, o = sim http://sped.rfb.gov.br/pasta/show/1932
    private String cep; //t = 8, o = sim
    @JsonProperty("num_tel")
    private Long numeroTelefone; //t = 15, o = não
    private String email; //t = 115, o = sim

}
