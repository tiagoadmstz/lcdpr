/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.dsc.lcdpr.components;

import br.com.dsc.lcdpr.interfaces.LcdprHandler;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.*;

import java.io.Serializable;

/**
 * Registro 0050: Cadastro das Contas Bancárias
 * <p>
 * Apresenta os dados das contas bancárias da pessoa física declarante (individuais ou conjuntas) utilizadas na atividade rural.
 * As contas que o produtor rural declarante não seja um dos titulares junto à instituição financeira, mas que por força de contrato registrado sejam
 * de propriedade e uso exclusivo de sociedade da qual ele participe, devem ser informadas no registro 0050 normalmente, com a informação de seu
 * código no registro Q100 das operações que transitem por elas. Nesse caso, pelo menos um dos parceiros deve ser titular da conta utilizada.
 * Ocorrência [1:N]
 *
 * @author Tiago D.
 */
@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "registro", "codigoConta", "paisConta", "banco", "nomeBanco", "agencia", "numeroConta"
})
public class ContaBancaria implements Serializable, LcdprHandler {

    private static final long serialVersionUID = -3013153228791071332L;
    @Builder.Default
    @JsonProperty("reg")
    private String registro = "0050"; // t = 4, o = sim, p = 0050
    @JsonProperty("cod_conta")
    private String codigoConta; // t = 3, o = sim
    @Builder.Default
    @JsonProperty("pais_cta")
    private String paisConta = "BR"; // t = 3, o = sim http://sped.rfb.gov.br/pasta/show/1932
    private String banco; // t = 3, o = sim(BRA)
    @JsonProperty("nome_banco")
    private String nomeBanco; // t = 30, o = sim
    private String agencia; // t = 4, o = sim
    @JsonProperty("num_conta")
    private String numeroConta; // t = 16, o = sim

}
