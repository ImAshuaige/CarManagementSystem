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
public class OutsideOutletOperatingHourException extends Exception{

    /**
     * Creates a new instance of
     * <code>OutsideOutletOperatingHourException</code> without detail message.
     */
    public OutsideOutletOperatingHourException() {
    }

    /**
     * Constructs an instance of
     * <code>OutsideOutletOperatingHourException</code> with the specified
     * detail message.
     *
     * @param msg the detail message.
     */
    public OutsideOutletOperatingHourException(String msg) {
        super(msg);
    }
}
