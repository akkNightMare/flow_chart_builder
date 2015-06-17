/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package drawing;

import exeptions.InvalidQuantityBracketException;
import exeptions.ParsingClassException;
import java.util.List;
import java.util.Map;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import parsing.JavaCodeParser;
import parsing.ParsedClass;

/**
 *
 * @author Denis
 */
public class DraftsManTest {

    private static JavaCodeParser jcp;
    private static JavaCodeParser jcp1;
    private static JavaCodeParser jcp2;

    public DraftsManTest() {
    }

    @BeforeClass
    public static void setUpClass() {
        jcp = new JavaCodeParser("public abstract class AStatement {\n"
                                 + "\n"
                                 + "    protected String statement;\n"
                                 + "    protected int quantityEndBracket = 0;\n"
                                 + "    private int begPozition = 0;\n"
                                 + "\n"
                                 + "    public String getStatement() {\n"
                                 + "        return statement;\n"
                                 + "    }\n"
                                 + "\n"
                                 + "    public int quantityEndBracket() {\n"
                                 + "        return quantityEndBracket;\n"
                                 + "    }\n"
                                 + "\n"
                                 + "    public void setQuantityEndBracket(int quantityEndBracket) {\n"
                                 + "        this.quantityEndBracket = quantityEndBracket;\n"
                                 + "    }\n"
                                 + "\n"
                                 + "    protected boolean haveEndBracket(String statement) {\n"
                                 + "        return statement.matches(\"(?s)^}.+\");\n"
                                 + "    }\n"
                                 + "\n"
                                 + "    protected int countEndBracket(String statement) {\n"
                                 + "        int count = 0;\n"
                                 + "\n"
                                 + "        for (int i = 0; i < statement.length(); i++) {\n"
                                 + "\n"
                                 + "            if (statement.charAt(i) == '\\\"'\n"
                                 + "                || statement.charAt(i) == '\\'') {\n"
                                 + "\n"
                                 + "                for (int j = i + 1; j < statement.length(); j++) {\n"
                                 + "                    if (statement.charAt(j) == statement.charAt(i)\n"
                                 + "                        && statement.charAt(j - 1) != '\\\\'\n"
                                 + "                        && statement.charAt(j - 2) != '\\\\') {\n"
                                 + "                        i = j;\n"
                                 + "                        break;\n"
                                 + "                    }\n"
                                 + "                }\n"
                                 + "\n"
                                 + "            }\n"
                                 + "\n"
                                 + "            if (statement.charAt(i) == '}') {\n"
                                 + "                count++;\n"
                                 + "                this.begPozition = i + 1;\n"
                                 + "            }\n"
                                 + "\n"
                                 + "        }\n"
                                 + "        return count;\n"
                                 + "    }\n"
                                 + "\n"
                                 + "    public AStatement(String statement) {\n"
                                 + "        if (haveEndBracket(statement)) {\n"
                                 + "            quantityEndBracket = countEndBracket(statement);\n"
                                 + "        }\n"
                                 + "\n"
                                 + "        this.statement = statement.substring(this.begPozition).trim();\n"
                                 + "    }\n"
                                 + "\n"
                                 + "}");

        jcp1 = new JavaCodeParser("public class Tst"
                                  + "{public methodTest(String str){} }");

        jcp2 = new JavaCodeParser("public class StatementsFactory {\n"
                                  + "\n"
                                  + "    private final String START_STR = \"(?s)(}\\\\s*)*\";\n"
                                  + "\n"
                                  + "    private StatementsFactory() {\n"
                                  + "    }\n"
                                  + "\n"
                                  + "    public static StatementsFactory getInstance() {\n"
                                  + "        return StatementsFactoryHolder.INSTANCE;\n"
                                  + "    }\n"
                                  + "\n"
                                  + "    private static class StatementsFactoryHolder {\n"
                                  + "\n"
                                  + "        private static final StatementsFactory INSTANCE = new StatementsFactory();\n"
                                  + "    }\n"
                                  + "\n"
                                  + "    public AStatement makeStatement(String statement) {\n"
                                  + "        if (isForStatement(statement)) {\n"
                                  + "            return new ForStatement(statement);\n"
                                  + "        } else if (isIfStatement(statement)) {\n"
                                  + "            return new IfStatement(statement);\n"
                                  + "        } else if (isElseIfStatement(statement)) {\n"
                                  + "            return new ElseIfStatement(statement);\n"
                                  + "        } else if (isElseStatement(statement)) {\n"
                                  + "            return new ElseStatement(statement);\n"
                                  + "        } else if (isWhileStatement(statement)) {\n"
                                  + "            return new WhileStatement(statement);\n"
                                  + "        } else if (isEndStatement(statement)) {\n"
                                  + "            return new EndStatement(statement);\n"
                                  + "        } else if (isStartStatement(statement)) {\n"
                                  + "            return new StartStatement(statement);\n"
                                  + "        } else if (!\"\".equals(statement)) {\n"
                                  + "            return new SimpleStatement(statement);\n"
                                  + "        }\n"
                                  + "\n"
                                  + "        return null;\n"
                                  + "    }\n"
                                  + "\n"
                                  + "    private boolean isForStatement(String statement) {\n"
                                  + "        return statement.matches(START_STR + \"for\\\\s*\\\\(.+\\\\)\");\n"
                                  + "    }\n"
                                  + "\n"
                                  + "    private boolean isIfStatement(String statement) {\n"
                                  + "        return statement.matches(START_STR + \"if\\\\s*\\\\(.+\\\\)\");\n"
                                  + "    }\n"
                                  + "\n"
                                  + "    private boolean isElseIfStatement(String statement) {\n"
                                  + "        return statement.matches(START_STR + \"else\\\\s+if\\\\s*\\\\(.+\\\\)\");\n"
                                  + "    }\n"
                                  + "\n"
                                  + "    private boolean isElseStatement(String statement) {\n"
                                  + "        return statement.matches(START_STR + \"(else)\");\n"
                                  + "    }\n"
                                  + "\n"
                                  + "    private boolean isWhileStatement(String statement) {\n"
                                  + "        return statement.matches(START_STR + \"while\\\\s*\\\\(.+\\\\)\");\n"
                                  + "    }\n"
                                  + "\n"
                                  + "    private boolean isEndStatement(String statement) {\n"
                                  + "        return statement.matches(START_STR + \"(\\\\$end\\\\$)\");\n"
                                  + "    }\n"
                                  + "\n"
                                  + "    private boolean isStartStatement(String statement) {\n"
                                  + "        return statement.matches(START_STR + \"(\\\\$start\\\\$)\");\n"
                                  + "    }\n"
                                  + "}");
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
     * Test of getGraphForClassMembers method, of class DraftsMan.
     */
    @Test
    public void testGetGraphForClassMembers() {
        System.out.println("getGraphForClassMembers");

        try {
            List<ParsedClass> pc = jcp.parse();
            DraftsMan instance = new DraftsMan(pc.get(0));
            String result = instance.getGraphForClassMembers();
            assertNotNull(result);
        } catch (Exception e) {
        }
    }

    @Test
    public void testGetGraphForMethods() {
        System.out.println("getGraphForMethods");

        try {
            List<ParsedClass> pc = jcp.parse();
            DraftsMan instance = new DraftsMan(pc.get(0));
            String result = instance.getGraphForClassMembers();
            Map<String, String> methods = instance.buildGraphsForMethods();
            assertNotNull(methods);
            assertNotNull(result);
        } catch (ParsingClassException | InvalidQuantityBracketException e) {
            System.out.println("fail:" + e.getMessage());
        }
    }

}
