/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.dsc.lcdpr.test;

import br.com.dsc.lcdpr.lcdpr.Lcdpr;
import br.com.dsc.lcdpr.util.ExceptionUtil;
import br.com.dsc.lcdpr.util.LcdprUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;

import java.io.File;
import java.time.LocalDate;

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
        assertTrue(LcdprUtil.exportLcdprFile(lcdpr, LocalDate.now()));
        assertTrue(LcdprUtil.exportLcdprJson(lcdpr, LocalDate.now()));
    }

    @Test
    @Order(3)
    public void importLcdprTest() {
        File file = new File("LCDPR.txt");
        Lcdpr lcdpr = LcdprUtil.importLcdprFromTxtFile(file);
        //System.out.println(lcdpr.generatePipeText());
        File jsonFile = new File("LCDPR.json");
        Lcdpr jsonLcdpr = LcdprUtil.importLcdprByFromJsonFile(jsonFile);
        //System.out.println(jsonLcdpr.generatePipeText());
        assertEquals(lcdpr, jsonLcdpr);
    }

}
