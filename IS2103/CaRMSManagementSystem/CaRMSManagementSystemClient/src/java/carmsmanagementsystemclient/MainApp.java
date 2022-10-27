/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package carmsmanagementsystemclient;

import ejb.session.stateless.EmployeeSessionBeanRemote;
import ejb.session.stateless.OutletSessionBeanRemote;
import entity.Employee;
import java.util.Scanner;
import util.exception.InvalidLoginException;

/**
 *
 * @author 60540
 */
public class MainApp {

    private EmployeeSessionBeanRemote employeeSessionBeanRemote;
    private OutletSessionBeanRemote outletSessionBeanRemote;
    private Employee currEmployee;

    public MainApp() {

    }

    public MainApp(EmployeeSessionBeanRemote employeeSessionBeanRemote, OutletSessionBeanRemote outletSessionBeanRemote) {
        this();
        this.employeeSessionBeanRemote = employeeSessionBeanRemote;
        this.outletSessionBeanRemote = outletSessionBeanRemote;
    }

    public void runApp() throws InvalidLoginException {
        Scanner sc = new Scanner(System.in);
        Integer input = 0;

        while (true) {
            System.out.println("***Welcome! CaRMS Management Client! ***");
            System.out.println("Input 1 to Login");
            System.out.println("Input 2 to Manually Allocate Cars");
            System.out.println("Input 3 to Exit");

            input = 0;//set input/default input to 0
            while (input == 0) {//everytime want to do a new command
                System.out.print("Enter your input: ");
                input = sc.nextInt();

                if (input == 1) {
                    try {
                    login();
                    System.out.println("***Login Successfully***");
                    } catch (InvalidLoginException ex) {
                        System.out.println("Invalid Login" + ex.getMessage());
                    }
                } else {
                    break;
                }
            }
            if (input == 3) {
                break;//break the main true loop
            }
        }

    }
    
    private void login() throws InvalidLoginException {
        Scanner sc = new Scanner(System.in);
        String employeeUsername = "";
        String password = "";
        
        System.out.println("***Enter Login Details***");
        System.out.println("Enter Your Employee Username: ");
        employeeUsername = sc.nextLine().trim();
        System.out.println("Enter Your Password: ");
        password = sc.nextLine().trim();
        
        if (employeeUsername.length() > 0 && password.length() > 0) {
            currEmployee = employeeSessionBeanRemote.login(employeeUsername,password);          
        } else {
            throw new InvalidLoginException("Wrong Password!");
        }
        
        
    }

}
