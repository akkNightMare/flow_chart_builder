/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package drawing;

import exeptions.InvalidQuantityBracketException;
import exeptions.ParsingClassException;
import java.io.File;
import java.util.List;
import java.util.Map;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.assertNotNull;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import parsing.JavaCodeParser;
import parsing.ParsedClass;

/**
 *
 * @author Denis
 */
public class GraphVizTest {

    private static JavaCodeParser jcp;
    private static JavaCodeParser jcp1;

    public GraphVizTest() {
    }

    @BeforeClass
    public static void setUpClass() {
        jcp = new JavaCodeParser("public class StatementsFactory {\n"
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

        jcp1 = new JavaCodeParser("public class MethodParser {"
                                  + ""
                                  + "public static List<String> split(final String source) {\n"
                                  + "        int start = 0;\n"
                                  + "        ArrayList<String> list = new ArrayList<>();\n"
                                  + "\n"
                                  + "        for (int i = 0; i < source.length(); i++) {\n"
                                  + "\n"
                                  + "            if (source.charAt(i) == ';'\n"
                                  + "                || source.charAt(i) == '{'\n"
                                  + "                || source.charAt(i) == '\\\"'\n"
                                  + "                || source.charAt(i) == '\\'') {\n"
                                  + "\n"
                                  + "                if (source.charAt(i) == '\\\"'\n"
                                  + "                    || source.charAt(i) == '\\'') {\n"
                                  + "\n"
                                  + "                    if (source.charAt(i + 1) == source.charAt(i)) {\n"
                                  + "                        i += 1;\n"
                                  + "\n"
                                  + "                    } else {\n"
                                  + "\n"
                                  + "                        for (int j = i + 2; j < source.length(); j++) {\n"
                                  + "                            if (source.charAt(j) == source.charAt(i)\n"
                                  + "                                && (source.charAt(j - 1) != '\\\\'\n"
                                  + "                                    || (source.charAt(j - 1) == '\\\\'\n"
                                  + "                                        && source.charAt(j - 2) == '\\\\'))) {\n"
                                  + "\n"
                                  + "                                i = j;\n"
                                  + "                                break;\n"
                                  + "                            }\n"
                                  + "                        }\n"
                                  + "\n"
                                  + "                    }\n"
                                  + "\n"
                                  + "                } else {\n"
                                  + "\n"
                                  + "                    String substr = source.substring(start, i).trim();\n"
                                  + "                    if (!\"\".equals(substr)) {\n"
                                  + "                        list.add(escapeQuotes(substr));\n"
                                  + "                    }\n"
                                  + "                    start = i + 1;\n"
                                  + "                    i = start;\n"
                                  + "\n"
                                  + "                }\n"
                                  + "\n"
                                  + "            }\n"
                                  + "        }\n"
                                  + "\n"
                                  + "        return list;\n"
                                  + "    }"
                                  + ""
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

    @Test
    public void testSomeMethod() {
        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
    }

    /**
     * Test of writeDotGraphToFile method, of class GraphViz1.
     */
    @Test
    public void testWriteDotGraphToFile() {
        System.out.println("writeDotGraphToFile");
        String location = "D:\\tmp";
        String name = "";
        String type = "txt";
        String graph = "";
        Map<String, String> methods;

        try {
            List<ParsedClass> pc = jcp1.parse();
            DraftsMan instance = new DraftsMan(pc.get(0));
            String graphForClassMembers = instance.getGraphForClassMembers();
            methods = instance.buildGraphsForMethods();
            assertNotNull(methods);
            assertNotNull(graphForClassMembers);

            File result1 = null;

            for (Map.Entry<String, String> entry : methods.entrySet()) {
                name = entry.getKey();
                graph = entry.getValue();
                result1 = GraphViz_1.writeDotGraphToFile("D:\\tmp", name, type, graph);
                GraphViz_1.getImageStream(result1, "svg");

            }

            File graphForClass = GraphViz_1.writeDotGraphToFile("D:\\tmp", "class", type, graphForClassMembers);
            GraphViz_1.getImageStream(graphForClass, "svg");

            assertNotNull(result1);
        } catch (ParsingClassException | InvalidQuantityBracketException e) {
            System.out.println("Error occurred while parsing:" + e.getMessage());
        }
    }

}
