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
public class DriverNotWorkingInSameOutletException extends Exception {

    /**
     * Creates a new instance of
     * <code>DriverNotWorkingInSameOutletException</code> without detail
     * message.
     */
    public DriverNotWorkingInSameOutletException() {
    }

    /**
     * Constructs an instance of
     * <code>DriverNotWorkingInSameOutletException</code> with the specified
     * detail message.
     *
     * @param msg the detail message.
     */
    public DriverNotWorkingInSameOutletException(String msg) {
        super(msg);
    }
}
