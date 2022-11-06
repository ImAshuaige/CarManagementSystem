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
public class TransitDriverDispatchRecordNotFoundException extends Exception {

    /**
     * Creates a new instance of
     * <code>TransitDriverDispatchRecordNotFoundException</code> without detail
     * message.
     */
    public TransitDriverDispatchRecordNotFoundException() {
    }

    /**
     * Constructs an instance of
     * <code>TransitDriverDispatchRecordNotFoundException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public TransitDriverDispatchRecordNotFoundException(String msg) {
        super(msg);
    }
}
