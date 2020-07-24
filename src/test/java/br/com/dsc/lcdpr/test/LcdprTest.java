/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.dsc.lcdpr.test;

import br.com.dev.engine.date.Datas;
import br.com.dsc.lcdpr.lcdpr.Lcdpr;
import br.com.dsc.lcdpr.util.ExceptionUtil;
import br.com.dsc.lcdpr.util.LcdprUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author Tiago D.
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class LcdprTest {

    @Test
    @Order(1)
    public void aberturaIdentificacaoTest() {
        ObjectMapper objectMapper = new ObjectMapper();

        Lcdpr lcdpr = DummyData.generateLcdpr();
        String pipeText = lcdpr.generatePipeText();

        System.out.println(ExceptionUtil.tryCatch("", f -> objectMapper.writeValueAsString(lcdpr)) + "\n" + pipeText);
        Assertions.assertNotNull(pipeText);
    }

    @Test
    @Order(2)
    public void exportLcdprTest() {
        Lcdpr lcdpr = DummyData.generateLcdpr();
        assertTrue(LcdprUtil.exportLcdprFile(lcdpr, Datas.stringToLocalDate("03/07/2020")));
        assertTrue(LcdprUtil.exportLcdprJson(lcdpr, Datas.stringToLocalDate("03/07/2020")));
    }

    @Test
    @Order(3)
    public void importLcdprTest() {
        File file = new File("LCDPR_03072020.txt");
        Lcdpr lcdpr = LcdprUtil.importLcdprFromTxtFile(file);
        //System.out.println(lcdpr.generatePipeText());
        File jsonFile = new File("LCDPR_03072020.json");
        Lcdpr jsonLcdpr = LcdprUtil.importLcdprByFromJsonFile(jsonFile);
        //System.out.println(jsonLcdpr.generatePipeText());
        assertEquals(lcdpr, jsonLcdpr);
    }

    //@Test
    @Order(4)
    public void validateAndCalculateTest() {
        File file = new File("LCDPR.txt");
        Lcdpr lcdpr = LcdprUtil.importLcdprFromTxtFile(file);
        lcdpr.getBlocoQ().calculate();
        System.out.println(lcdpr.getBlocoQ());
    }

    //@Test
    @Order(5)
    public void lcdprValidationTest() {
        Lcdpr lcdpr = DummyData.generateLcdpr();
        boolean validate = lcdpr.getBloco0().getIdentificacaoPessoaFisica().validate();
        assertTrue(validate);
    }

    //@Test
    @Order(6)
    public void generatePipeTextFromSubClass() {
        DocumentoLcdpr lcdpr = DummyData.generateDocumentoLcdpr();
        System.out.println(lcdpr.generatePipeText());
    }

}
