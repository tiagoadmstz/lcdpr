package br.com.dsc.lcdpr.util;

import br.com.dsc.lcdpr.blocos.AberturaIdentificacao;
import br.com.dsc.lcdpr.blocos.DemonstrativoLivroCaixa;
import br.com.dsc.lcdpr.blocos.EncerramentoArquivo;
import br.com.dsc.lcdpr.lcdpr.LCDPR;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.apache.commons.io.IOUtils.readLines;
import static org.apache.commons.io.IOUtils.write;

public abstract class LcdprUtil {

    public static boolean exportLcdprFile(LCDPR lcdpr, LocalDate date) {
        return ExceptionUtil.tryCatch(false, ef -> {
            File file = new File("LCDPR_" + date.format(DateTimeFormatter.ofPattern("ddMMyyyy")) + ".txt");
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            write(lcdpr.generatedPipeText(), fileOutputStream);
            fileOutputStream.close();
            return true;
        });
    }

    public static LCDPR importLcdprFromTxtFile(File txtFile) {
        return ExceptionUtil.tryCatch(null, ef -> {
            List<String> lines = readLines(new FileInputStream(txtFile));
            DemonstrativoLivroCaixa demonstrativoLivroCaixa = new DemonstrativoLivroCaixa();
            EncerramentoArquivo encerramentoArquivo = new EncerramentoArquivo();
            return LCDPR.builder()
                    .bloco0(new AberturaIdentificacao().buildFromLinesList(lines))
                    .build();
        });
    }

    public static LCDPR importLcdprByFromJsonFile(File jsonFile) {
        return ExceptionUtil.tryCatch(null, ef -> new ObjectMapper().readValue(jsonFile, LCDPR.class));
    }

}
