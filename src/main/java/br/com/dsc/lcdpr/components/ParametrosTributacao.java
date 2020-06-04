/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.dsc.lcdpr.components;

import br.com.dsc.lcdpr.enumerated.FORMA_APURACAO;
import br.com.dsc.lcdpr.interfaces.LcdprHandler;
import com.google.gson.annotations.SerializedName;
import lombok.*;

import java.io.Serializable;

/**
 * @author Tiago
 */
@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class ParametrosTributacao implements Serializable, LcdprHandler {

    private static final long serialVersionUID = 901801438322291828L;
    @Builder.Default
    private String reg = "0010"; //t = 4, o = sim, p = 0010
    @SerializedName("forma_apur")
    private FORMA_APURACAO forma_apuracao; //t = 1, o = sim

}
