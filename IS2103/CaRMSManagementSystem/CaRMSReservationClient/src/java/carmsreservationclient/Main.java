/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package carmsreservationclient;

import ejb.session.stateless.CarCategorySessionBeanRemote;
import ejb.session.stateless.CarModelSessionBeanRemote;
import ejb.session.stateless.CustomerSessionBeanRemote;
import ejb.session.stateless.OutletSessionBeanRemote;
import ejb.session.stateless.ReservationSessionBeanRemote;
import javax.ejb.EJB;
import util.exception.InputDataValidationException;
import util.exception.InvalidLoginException;

/**
 *
 * @author 60540
 */
public class Main {

    @EJB
    private static ReservationSessionBeanRemote reservationSessionBeanRemote;

    @EJB
    private static CarModelSessionBeanRemote carModelSessionBeanRemote;

    @EJB
    private static CarCategorySessionBeanRemote carCategorySessionBeanRemote;

    @EJB
    private static CustomerSessionBeanRemote customerSessionBeanRemote;

    @EJB
    private static OutletSessionBeanRemote outletSessionBeanRemote;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws InvalidLoginException, InputDataValidationException {
        MainApp mainApp = new MainApp(customerSessionBeanRemote, carCategorySessionBeanRemote,
                reservationSessionBeanRemote, carModelSessionBeanRemote, outletSessionBeanRemote);
        mainApp.runApp();
    }

}
