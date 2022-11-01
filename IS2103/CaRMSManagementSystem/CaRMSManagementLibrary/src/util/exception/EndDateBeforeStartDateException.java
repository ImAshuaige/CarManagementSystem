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
public class EndDateBeforeStartDateException extends Exception {

    /**
     * Creates a new instance of <code>EndDateBeforeStartDateException</code>
     * without detail message.
     */
    public EndDateBeforeStartDateException() {
    }

    /**
     * Constructs an instance of <code>EndDateBeforeStartDateException</code>
     * with the specified detail message.
     *
     * @param msg the detail message.
     */
    public EndDateBeforeStartDateException(String msg) {
        super(msg);
    }
}
