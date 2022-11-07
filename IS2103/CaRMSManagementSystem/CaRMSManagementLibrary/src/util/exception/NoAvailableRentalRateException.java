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
public class NoAvailableRentalRateException extends Exception {

    /**
     * Creates a new instance of <code>NoAvailableRentalRateException</code>
     * without detail message.
     */
    public NoAvailableRentalRateException() {
    }

    /**
     * Constructs an instance of <code>NoAvailableRentalRateException</code>
     * with the specified detail message.
     *
     * @param msg the detail message.
     */
    public NoAvailableRentalRateException(String msg) {
        super(msg);
    }
}
