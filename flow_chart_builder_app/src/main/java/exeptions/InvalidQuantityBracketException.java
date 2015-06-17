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
public class InvalidQuantityBracketException extends Exception {

    /**
     * Creates a new instance of <code>InvalidQuantityBracketException</code>
     * without detail message.
     */
    public InvalidQuantityBracketException() {
    }

    /**
     * Constructs an instance of <code>InvalidQuantityBracketException</code>
     * with the specified detail message.
     *
     * @param msg the detail message.
     */
    public InvalidQuantityBracketException(String msg) {
        super(msg);
    }
}
