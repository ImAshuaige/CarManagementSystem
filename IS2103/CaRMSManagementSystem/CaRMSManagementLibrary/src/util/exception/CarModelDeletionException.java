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
public class CarModelDeletionException extends Exception{

    /**
     * Creates a new instance of <code>CarModelDeletionException</code> without
     * detail message.
     */
    public CarModelDeletionException() {
    }

    /**
     * Constructs an instance of <code>CarModelDeletionException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public CarModelDeletionException(String msg) {
        super(msg);
    }
}
