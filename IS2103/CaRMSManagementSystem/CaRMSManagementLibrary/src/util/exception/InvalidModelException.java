/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util.exception;

/**
 *
 * @author Mehak
 */
public class InvalidModelException extends Exception {

    /**
     * Creates a new instance of <code>InvalidModelException</code> without
     * detail message.
     */
    public InvalidModelException() {
    }

    /**
     * Constructs an instance of <code>InvalidModelException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public InvalidModelException(String msg) {
        super(msg);
    }
}
