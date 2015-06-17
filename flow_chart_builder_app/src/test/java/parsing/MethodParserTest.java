/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package parsing;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author dema0614
 */
public class MethodParserTest {

    public MethodParserTest() {
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
     * Test of parse method, of class MethodParser.
     */
    @Test
    public void testParse() {
        System.out.println("parse");
        String str = "retrun \"\"; ffdfd";
        MethodParser.split(str);
        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
    }

    /**
     * Test of getTogetherForStatements method, of class MethodParser.
     */
    @Test
    public void testGetTogetherForStatements() {
        System.out.println("getTogetherForStatements");
    }

}
