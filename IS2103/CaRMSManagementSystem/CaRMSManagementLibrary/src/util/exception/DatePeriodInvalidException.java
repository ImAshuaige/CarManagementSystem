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
public class DatePeriodInvalidException extends Exception {

    /**
     * Creates a new instance of <code>DatePeriodInvaildException</code> without
     * detail message.
     */
    public DatePeriodInvalidException() {
    }

    /**
     * Constructs an instance of <code>DatePeriodInvaildException</code> with
     * the specified detail message.
     *
     * @param msg the detail message.
     */
    public DatePeriodInvalidException(String msg) {
        super(msg);
    }
}
