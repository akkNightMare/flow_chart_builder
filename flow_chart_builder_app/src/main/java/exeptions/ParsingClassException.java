/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package exeptions;

/**
 *
 * @author Denis
 */
public class ParsingClassException extends Exception {

    /**
     * Creates a new instance of <code>ParsingClassException</code> without
     * detail message.
     */
    public ParsingClassException() {
    }

    /**
     * Constructs an instance of <code>ParsingClassException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public ParsingClassException(String msg) {
        super(msg);
    }
}
