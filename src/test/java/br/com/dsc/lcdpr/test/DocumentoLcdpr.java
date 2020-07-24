package br.com.dsc.lcdpr.test;

import br.com.dsc.lcdpr.lcdpr.Lcdpr;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class DocumentoLcdpr extends Lcdpr {

    private Long id;

}
