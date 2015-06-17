/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package parsing;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author dema0614
 */
public class JavaClassParser {

    private final String classText;

    private final Pattern patternForFields = Pattern.compile("(private\\s+|public\\s+|protected\\s+)?"
                                                             + "(static\\s+|final\\s+|final\\s+static\\s+|static\\s+final\\s+)?"
                                                             + "[A-z]\\w*"
                                                             + "(\\s+|"
                                                             + "\\s*\\[\\s*\\]\\s+|"
                                                             + "\\s*\\[\\s*\\]\\s*\\[\\s*\\]\\s+|"
                                                             + "\\s*<\\s*([A-z]\\w*\\s*)?>\\s+)"
                                                             + "[A-z]\\w*\\s*"
                                                             + "(\\[\\s*\\]\\s*|"
                                                             + "\\[\\s*\\]\\s*\\[\\s*\\]\\s*)?"
                                                             + "(;|(=\\s*"
                                                             + "(\".*?(\")|\\{.+\\}|"
                                                             + "[A-z].*\\(.*\\)|\\d+|"
                                                             + "[A-z]\\w*)"
                                                             + "\\s*;)"
                                                             + ")", Pattern.COMMENTS);

    private final Pattern patternForMethods = Pattern.compile("(private\\s+|public\\s+|protected\\s+)?\n"
                                                              + "(static\\s+|final\\s+|final\\s+static\\s+|static\\s+final\\s+)?\n"
                                                              + "[A-z]\\w*(((\\s*\\[\\s*\\]\\s*)*)|(<\\s*[A-z]\\w*\\s*>)?)\\s+\n"
                                                              + "[A-z]\\w*(\\s*\\[\\s*\\]\\s*)*\\s*\n"
                                                              + "[(][^)]*[)]\\s*(?=\\{)", Pattern.COMMENTS);

    private final Pattern patternForReference = Pattern.compile("!referece to class (\\d+);");

    public JavaClassParser(String classText) {
        this.classText = classText;
    }

    public List<String> extractFields() {
        Matcher matcher = patternForFields.matcher(classText);
        List<String> fields = new ArrayList<>();

        while (matcher.find()
               && !matcher.group().startsWith("return ")
               && !matcher.group().startsWith("throws ")
               && !matcher.group().startsWith("throw ")) {
            fields.add(escapeQuotes(matcher.group()));
        }

        return fields;
    }

    public List<Method> extractMethods() {
        Matcher matcher = patternForMethods.matcher(classText);
        List<Method> methods = new ArrayList<>();

        while (matcher.find()) {
            Method method = new Method(matcher.group().trim());
            String methodBody = classText.substring(matcher.end() + 1, findCoupledBracket(matcher.end() + 1)).replaceAll("//.*\n", "").trim();

            method.setBody(methodBody);
            methods.add(method);
        }

        return methods;
    }

    public int extractReference() {
        Matcher matcher = patternForReference.matcher(classText);
        int index = -1;

        if (matcher.find()) {
            index = Integer.parseInt(matcher.group(1));
        }

        return index;
    }

    private int findCoupledBracket(int begIndex) {
        char[] charSC = classText.toCharArray();
        Stack stack = new Stack();
        stack.push('{');

        for (int i = begIndex; i < charSC.length; i++) {

            if (charSC[i] == '\"' || charSC[i] == '\'') {

                if (charSC[i + 1] == charSC[i]) {
                    i += 2;
                } else {
                    for (int j = i + 2; j < charSC.length; j++) {
                        //Find pair qoute
                        if (charSC[j] == charSC[i]
                            && (charSC[j - 1] != '\\'
                                || (charSC[j - 1] == '\\'
                                    && charSC[j - 2] == '\\'))) {

                            i = j;
                            break;
                        }
                    }
                }

            }

            if (charSC[i] == '{') {
                stack.push('{');
            } else if (charSC[i] == '}') {
                stack.pop();
            }

            if (stack.isEmpty()) {
                return i;
            }
        }
        return -1;
    }

    private String escapeQuotes(final String str) {
        return str.replaceAll("\\\\", "\\\\\\\\").replaceAll("\"", "\\\\\"");
    }

}
