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
public class TransitDriverDispatchNotFoundException extends Exception {

    /**
     * Creates a new instance of
     * <code>TransitDriverDispatchNotFoundException</code> without detail
     * message.
     */
    public TransitDriverDispatchNotFoundException() {
    }

    /**
     * Constructs an instance of
     * <code>TransitDriverDispatchNotFoundException</code> with the specified
     * detail message.
     *
     * @param msg the detail message.
     */
    public TransitDriverDispatchNotFoundException(String msg) {
        super(msg);
    }
}
