/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.dsc.lcdpr.blocos;

import br.com.dsc.lcdpr.components.DemoLivroCaixa;
import br.com.dsc.lcdpr.components.ResumoDemoLivroCaixa;
import br.com.dsc.lcdpr.interfaces.LcdprHandler;
import lombok.*;

import java.io.Serializable;
import java.util.List;

/**
 * Q - Apresenta o demonstrativo do resultado da atividade rural
 *
 * @author Tiago D.
 */
@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class DemonstrativoLivroCaixa implements Serializable, LcdprHandler {

    private static final long serialVersionUID = 6287575698325024956L;
    private List<DemoLivroCaixa> demonstrativo_livro_caixa; //H = 2, Q100, 1:N, o
    private List<ResumoDemoLivroCaixa> resumo_demonstrativo_livro_caixa; //H = 2, Q200, 1:N, o

}
