/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package parsing;

import java.util.List;

/**
 *
 * @author Admin
 */
public class ParsedClass {

    private String className;
    private List<String> fields;
    private List<Method> methods;
    private ParsedClass reference;

    public ParsedClass getReference() {
        return reference;
    }

    public void setReference(ParsedClass reference) {
        this.reference = reference;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public List<String> getFields() {
        return fields;
    }

    public void setFields(List<String> fields) {
        this.fields = fields;
    }

    public List<Method> getMethods() {
        return methods;
    }

    public void setMethods(List<Method> methods) {
        this.methods = methods;
    }

    public Method getMethod(int index){
        return methods.get(index);
    }
}
