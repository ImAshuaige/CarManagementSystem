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
public class PartnerNameExistException extends Exception {

    /**
     * Creates a new instance of <code>PartnerNameExistException</code> without
     * detail message.
     */
    public PartnerNameExistException() {
    }

    /**
     * Constructs an instance of <code>PartnerNameExistException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public PartnerNameExistException(String msg) {
        super(msg);
    }
}
