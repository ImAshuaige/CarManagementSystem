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
public class CustomerEmailExistsException extends Exception {

    /**
     * Creates a new instance of <code>CustomerEmailExistsException</code>
     * without detail message.
     */
    public CustomerEmailExistsException() {
    }

    /**
     * Constructs an instance of <code>CustomerEmailExistsException</code> with
     * the specified detail message.
     *
     * @param msg the detail message.
     */
    public CustomerEmailExistsException(String msg) {
        super(msg);
    }
}
