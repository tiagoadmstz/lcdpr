package br.com.dsc.lcdpr.util;

import br.com.dsc.lcdpr.blocos.AberturaIdentificacao;
import br.com.dsc.lcdpr.blocos.DemonstrativoLivroCaixa;
import br.com.dsc.lcdpr.blocos.EncerramentoArquivo;
import br.com.dsc.lcdpr.components.DemoLivroCaixa;
import br.com.dsc.lcdpr.components.ResumoDemoLivroCaixa;
import br.com.dsc.lcdpr.enumerated.NATUREZA_SALDO_FINAL;
import br.com.dsc.lcdpr.exceptions.ServiceException;
import br.com.dsc.lcdpr.lcdpr.Lcdpr;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.imageio.plugins.common.InputStreamAdapter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.IntStream;

import static org.apache.commons.io.IOUtils.readLines;
import static org.apache.commons.io.IOUtils.write;

/**
 * Class with util methods for uses on LCDPR projects
 */
public abstract class LcdprUtil {

    /**
     * Export LCDPR root class for text file with pattern format
     *
     * @param lcdpr Lcdpr class
     * @param date  LocalDate to append in the file name
     * @return true if successful
     */
    public static boolean exportLcdprFile(Lcdpr lcdpr, LocalDate date) {
        return ExceptionUtil.tryCatch(false, ef -> {
            File file = new File("LCDPR_" + date.format(DateTimeFormatter.ofPattern("ddMMyyyy")) + ".txt");
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            write(lcdpr.generatePipeText(), fileOutputStream);
            fileOutputStream.close();
            return true;
        });
    }

    /**
     * Export LCDPR root class for json file with pattern format
     *
     * @param lcdpr Lcdpr class
     * @param date  LocalDate to append in the file name
     * @return true if successful
     */
    public static boolean exportLcdprJson(Lcdpr lcdpr, LocalDate date) {
        return ExceptionUtil.tryCatch(false, ef -> {
            File jsonFile = new File("LCDPR_" + date.format(DateTimeFormatter.ofPattern("ddMMyyyy")) + ".json");
            FileOutputStream fileOutputStream = new FileOutputStream(jsonFile);
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.writeValue(fileOutputStream, lcdpr);
            fileOutputStream.close();
            return true;
        });
    }

    /**
     * Imports text file in the pattern format to Java class Lcdr
     *
     * @param txtFile File LCDPR
     * @return Lcdpr with the file values
     */
    public static Lcdpr importLcdprFromTxtFile(File txtFile) {
        return ExceptionUtil.tryCatch(null, ef -> {
            List<String> lines = readLines(new FileInputStream(txtFile));
            return Lcdpr.builder()
                    .bloco0(new AberturaIdentificacao().buildFromLinesList(lines))
                    .blocoQ(new DemonstrativoLivroCaixa().buildFromLinesList(lines))
                    .bloco9(new EncerramentoArquivo().buildFromLinesList(lines))
                    .build();
        });
    }

    /**
     * Imports text file in the json format to Java class Lcdpr.
     * the keys must be the same as those passed in the LCDPR filling manual
     *
     * @param jsonFile File json
     * @return Lcdpr with the json values
     */
    public static Lcdpr importLcdprByFromJsonFile(File jsonFile) {
        return ExceptionUtil.tryCatch(null, ef -> new ObjectMapper().readValue(jsonFile, Lcdpr.class));
    }

}
