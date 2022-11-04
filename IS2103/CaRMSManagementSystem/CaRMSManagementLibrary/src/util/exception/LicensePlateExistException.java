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
public class LicensePlateExistException extends Exception {

    /**
     * Creates a new instance of <code>LicensePlateExistException</code> without
     * detail message.
     */
    public LicensePlateExistException() {
    }

    /**
     * Constructs an instance of <code>LicensePlateExistException</code> with
     * the specified detail message.
     *
     * @param msg the detail message.
     */
    public LicensePlateExistException(String msg) {
        super(msg);
    }
}
