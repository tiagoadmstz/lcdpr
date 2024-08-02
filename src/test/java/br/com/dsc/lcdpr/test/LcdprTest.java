package br.com.dsc.lcdpr.test;

import br.com.dsc.lcdpr.lcdpr.Lcdpr;
import br.com.dsc.lcdpr.util.ExceptionUtil;
import br.com.dsc.lcdpr.util.LcdprUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.time.LocalDate;

import static java.time.format.DateTimeFormatter.ofPattern;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author Tiago D.
 */
class LcdprTest {

    private static File txtFile;
    private static File jsonFile;

    @BeforeAll
    static void beforeAll() {
        txtFile = new File("src/test/resources/LCDPR_03072020.txt");
        jsonFile = new File("src/test/resources/LCDPR_03072020.json");
    }

    @AfterAll
    static void afterAll() {
        txtFile = null;
        jsonFile = null;
    }

    @Test
    @Order(1)
    void aberturaIdentificacaoTest() {
        final ObjectMapper objectMapper = new ObjectMapper();
        final Lcdpr lcdpr = DummyData.generateLcdpr();
        final String pipeText = lcdpr.generatePipeText();
        System.out.println(ExceptionUtil.tryCatch("", f -> objectMapper.writeValueAsString(lcdpr)) + "\n" + pipeText);
        assertNotNull(pipeText);
    }

    @Test
    @Order(2)
    void exportLcdprTest() {
        final Lcdpr lcdpr = DummyData.generateLcdpr();
        final LocalDate localDate = LocalDate.parse("03/07/2020", ofPattern("dd/MM/yyyy"));
        assertTrue(LcdprUtil.exportLcdprFile(lcdpr, localDate));
        assertTrue(LcdprUtil.exportLcdprJson(lcdpr, localDate));
    }

    //@Test
    @Order(3)
    void importLcdprTest() {
        final Lcdpr lcdpr = LcdprUtil.importLcdprFromTxtFile(txtFile);
        final Lcdpr jsonLcdpr = LcdprUtil.importLcdprByFromJsonFile(jsonFile);
        assertEquals(lcdpr, jsonLcdpr);
    }

    @Test
    @Order(4)
    void validateAndCalculateTest() {
        final Lcdpr lcdpr = LcdprUtil.importLcdprFromTxtFile(txtFile);
        lcdpr.getBlocoQ().calculate();
        assertNotNull(lcdpr.getBlocoQ());
    }

    @Test
    @Order(5)
    void lcdprValidationTest() {
        final Lcdpr lcdpr = DummyData.generateLcdpr();
        boolean validate = lcdpr.getBloco0().getIdentificacaoPessoaFisica().validate();
        assertTrue(validate);
    }

    @Test
    @Order(6)
    void generatePipeTextFromSubClass() {
        final DocumentoLcdpr lcdpr = DummyData.generateDocumentoLcdpr();
        assertNotNull(lcdpr);
    }
}
