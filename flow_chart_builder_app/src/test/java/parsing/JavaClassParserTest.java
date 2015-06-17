/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package parsing;

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
public class JavaClassParserTest {

    public JavaClassParserTest() {
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
     * Test of extractFields method, of class JavaClassParser.
     */
    @Test
    public void testExtractFields() {
        System.out.println("extractFields");
        JavaClassParser instance = new JavaClassParser("private int i; "
                                                       + "private int[] ii;"
                                                       + "public String value = \"5\";"
                                                       + "protected String str = \"qwerr\\\"ty\"; ");
//        List expResult = null;
        List result = instance.extractFields();
//        assertEquals(expResult, result);
        assertEquals(result.size(), 4);

    }

    /**
     * Test of extractMethods method, of class JavaClassParser.
     */
    @Test
    public void testExtractMethods() {
        System.out.println("extractMethods");
        JavaClassParser instance = new JavaClassParser("private void voidFunc(){int i = 1; i++; if(a==0){i++;} string str = \"qwerty\";}"
                                                       + "public int intFunc(){int i = 1; i++; string str = \"qwerty\";}"
                                                       + "protected String stringFunc(int[] i, int b){int i = 1; i++; string str = \"qwerty\";}"
                                                       + "private List<Int> listFunc(){int i = 1; i++; string str = \"qwerty\";}");
//        List expResult = null;
        List<Method> result = instance.extractMethods();
//        assertEquals(expResult, result);
        assertEquals(result.size(), 4);
    }

}
