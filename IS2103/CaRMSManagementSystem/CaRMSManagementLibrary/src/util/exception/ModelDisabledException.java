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
public class ModelDisabledException extends Exception {

    /**
     * Creates a new instance of <code>ModelDisabledException</code> without
     * detail message.
     */
    public ModelDisabledException() {
    }

    /**
     * Constructs an instance of <code>ModelDisabledException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public ModelDisabledException(String msg) {
        super(msg);
    }
}
