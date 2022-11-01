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
public class RentalDateDeletionException extends Exception{

    /**
     * Creates a new instance of <code>RentalDateDeletionException</code>
     * without detail message.
     */
    public RentalDateDeletionException() {
    }

    /**
     * Constructs an instance of <code>RentalDateDeletionException</code> with
     * the specified detail message.
     *
     * @param msg the detail message.
     */
    public RentalDateDeletionException(String msg) {
        super(msg);
    }
}
