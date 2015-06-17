/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package parsing;

import exeptions.ParsingClassException;
import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author dema0614
 */
public class JavaCodeParserTest {

    public JavaCodeParserTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of parse method, of class JavaCodeParser.
     */
    @Test
    public void testParse() {
        System.out.println("parse");
        JavaCodeParser instance = new JavaCodeParser("public class A{ public int a; private String str = \"123\";"
                                                     + "private class B{ int a = 0; private int method(String str){int a=0;}; "
                                                     + "protected class C { int cInner; }}}");

        try {
            List<ParsedClass> result = instance.parse();
            assertNotNull(result);
        } catch (ParsingClassException ex) {

        }

    }

}
