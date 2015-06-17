/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package parsing;

import exeptions.ParsingClassException;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author dema0614
 */
public class JavaCodeParser extends ATextParser {

    private final String sourseCode;
    private final Pattern pattern = Pattern.compile("((public\\s+|private\\s+|protected\\s+)?(abstract\\s+)?class\\s+(\\D\\w*)[^{]*)\\{");

    public JavaCodeParser(String sourseCode) {
        this.sourseCode = sourseCode;
    }

    public List<ParsedClass> parse() throws ParsingClassException {
        Matcher matcher = pattern.matcher(sourseCode);
        List<String> classes = new ArrayList<String>();
        List<ParsedClass> parsedClasses = new ArrayList<ParsedClass>();
        //Extracting classes
        while (matcher.find()) {
            classes.add(sourseCode.substring(matcher.start(), findCoupledBracket(matcher.end()) + 1));
            ParsedClass pc = new ParsedClass();
            pc.setClassName(matcher.group(1).trim());
            parsedClasses.add(pc);
        }

        if (parsedClasses.isEmpty()) {
            throw new ParsingClassException("Source code have errors");
        }

        if (classes.size() > 1) {
            String changedClass = null;
            for (int i = classes.size() - 1; i > 0; i--) {
                String innerClass = (changedClass == null) ? classes.get(i) : changedClass;
                String externalClass = classes.get(i-1);
                changedClass  = externalClass;
                externalClass = externalClass.replace(innerClass, "!referece to class " + i + ";");
                classes.set(i - 1, externalClass);
            }
        }
        //Parsing of classes
        for (int i = 0; i < parsedClasses.size(); i++) {
            JavaClassParser classParser = new JavaClassParser(classes.get(i)); //Class for extracting methods, fields and referencies of class
            ParsedClass pc = parsedClasses.get(i);
            ParsedClass refClass;
            pc.setFields(classParser.extractFields());    //Extracts class fields
            pc.setMethods(classParser.extractMethods());  //Extracts class methods
            refClass = classParser.extractReference() == -1 ? null : parsedClasses.get(classParser.extractReference()); //Extracts reference
            pc.setReference(refClass);
            parsedClasses.set(i, pc);
        }

        return parsedClasses;
    }

    private int findCoupledBracket(int begIndex) {
        char[] charSC = sourseCode.toCharArray();
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

}
