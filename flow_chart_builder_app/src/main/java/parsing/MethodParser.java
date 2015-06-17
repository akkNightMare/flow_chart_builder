/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package parsing;

import java.util.ArrayList;
import java.util.List;
import statement.StatementsFactory;
import statement.AStatement;
import statement.ElseStatement;

/**
 *
 * @author dema0614
 */
public class MethodParser {

    private static List<String> getTogetherForStatements(List<String> sourceSplittedMethod) {
        final List<String> newMethod = new ArrayList<String>();
        newMethod.add("$start$");

        for (int i = 0; i < sourceSplittedMethod.size(); i++) {
            StringBuilder sb = new StringBuilder(sourceSplittedMethod.get(i));

            if (sourceSplittedMethod.get(i).matches("(?s)(}\\s*)*for\\s*\\(.+")
                && sourceSplittedMethod.get(i + 1).matches("(?s).+(<|>|=).+")) {
                sb.append("; ").append(sourceSplittedMethod.get(i + 1) + "; ").append(sourceSplittedMethod.get(i + 2));
                i += 2;
            }

            newMethod.add(sb.toString());
        }

        newMethod.add("$end$");
        return newMethod;
    }

    public static List<String> split(final String source) {
        int start = 0;
        ArrayList<String> list = new ArrayList<>();

        for (int i = 0; i < source.length(); i++) {

            if (source.charAt(i) == ';'
                || source.charAt(i) == '{'
                //|| source.charAt(i) == '}'
                || source.charAt(i) == '\"'
                || source.charAt(i) == '\'') {

                if (source.charAt(i) == '\"'
                    || source.charAt(i) == '\'') {

                    if (source.charAt(i + 1) == source.charAt(i)) {

                        i += 1;

                    } else {

                        for (int j = i + 2; j < source.length(); j++) {
                            //Find pair qoute
                            if (source.charAt(j) == source.charAt(i)
                                && (source.charAt(j - 1) != '\\'
                                    || (source.charAt(j - 1) == '\\'
                                        && source.charAt(j - 2) == '\\'))) {

                                i = j;
                                break;
                            }
                        }

                    }

                } else {

                    String substr = source.substring(start, i).trim();

                    if (!"".equals(substr)) {
                        list.add(escapeQuotes(substr));
                    }
                    
                    start = i + 1;
                    i = start;

                }

            }
        }

        return list;
    }

    private static String escapeQuotes(final String str) {
        return str.replaceAll("\\\\", "\\\\\\\\").replaceAll("\"", "\\\\\"");
    }

    public static List<AStatement> parse(final String methodBody) {
        List<String> splittedMethod = split(methodBody);
        List<String> statements = getTogetherForStatements(splittedMethod);
        StatementsFactory statementsFactory = StatementsFactory.getInstance();
        final List<AStatement> list = new ArrayList<>();

        int count = 0;
        for (String statement : statements) {
            AStatement st = statementsFactory.makeStatement(statement);
            if (isElseStatement(st)) {
                count = st.quantityEndBracket();
                continue;
            }
            st.setQuantityEndBracket(count + st.quantityEndBracket());
            count = 0;
            list.add(st);
        }

        return list;
    }

    private static boolean isElseStatement(AStatement statement){
        return statement.getClass().equals(ElseStatement.class);
    }

}
