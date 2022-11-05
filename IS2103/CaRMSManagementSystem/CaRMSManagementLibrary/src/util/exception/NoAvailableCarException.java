/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util.exception;

/**
 *
 * @author 60540
 */
public class NoAvailableCarException extends Exception {

    /**
     * Creates a new instance of <code>NoAvailableCarException</code> without
     * detail message.
     */
    public NoAvailableCarException() {
    }

    /**
     * Constructs an instance of <code>NoAvailableCarException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public NoAvailableCarException(String msg) {
        super(msg);
    }
}
