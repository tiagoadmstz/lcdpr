package br.com.dsc.lcdpr.util;

import br.com.dsc.lcdpr.blocos.AberturaIdentificacao;
import br.com.dsc.lcdpr.blocos.DemonstrativoLivroCaixa;
import br.com.dsc.lcdpr.blocos.EncerramentoArquivo;
import br.com.dsc.lcdpr.lcdpr.Lcdpr;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static br.com.dsc.lcdpr.util.ExceptionUtil.tryCatch;
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
    public static boolean exportLcdprFile(final Lcdpr lcdpr, final LocalDate date) {
        return tryCatch(false, ef -> {
            final File file = new File(String.format("LCDPR_%s.txt", date.format(DateTimeFormatter.ofPattern("ddMMyyyy"))));
            final FileOutputStream fileOutputStream = new FileOutputStream(file);
            write(lcdpr.generatePipeText(), fileOutputStream, StandardCharsets.UTF_8);
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
    public static boolean exportLcdprJson(final Lcdpr lcdpr, final LocalDate date) {
        return tryCatch(false, ef -> {
            final File jsonFile = new File(String.format("LCDPR_%s.json", date.format(DateTimeFormatter.ofPattern("ddMMyyyy"))));
            final FileOutputStream fileOutputStream = new FileOutputStream(jsonFile);
            final ObjectMapper objectMapper = new ObjectMapper();
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
    public static Lcdpr importLcdprFromTxtFile(final File txtFile) {
        return tryCatch(null, ef -> {
            final List<String> lines = readLines(Files.newInputStream(txtFile.toPath()), StandardCharsets.UTF_8);
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
    public static Lcdpr importLcdprByFromJsonFile(final File jsonFile) {
        return tryCatch(null, ef -> new ObjectMapper().readValue(jsonFile, Lcdpr.class));
    }
}
