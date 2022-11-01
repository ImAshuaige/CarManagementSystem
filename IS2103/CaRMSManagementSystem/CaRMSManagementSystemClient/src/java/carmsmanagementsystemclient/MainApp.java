/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package carmsmanagementsystemclient;

import ejb.session.stateless.CarCategorySessionBeanRemote;
import ejb.session.stateless.CarModelSessionBeanRemote;
import ejb.session.stateless.EmployeeSessionBeanRemote;
import ejb.session.stateless.OutletSessionBeanRemote;
import ejb.session.stateless.RentalRateSessionBeanRemote;
import entity.CarCategory;
import entity.CarModel;
import entity.Employee;
import entity.RentalRate;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.PersistenceException;
import util.exception.CarCategoryNotFoundException;
import util.exception.CarModelNotFoundException;
import util.exception.EndDateBeforeStartDateException;
import util.exception.InputDataValidationException;
import util.exception.InvalidLoginException;
import util.exception.RentalRateNotFoundException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author 60540
 */
public class MainApp {

    private RentalRateSessionBeanRemote rentalRateSessionBeanRemote;
    private EmployeeSessionBeanRemote employeeSessionBeanRemote;
    private OutletSessionBeanRemote outletSessionBeanRemote;
    private CarModelSessionBeanRemote carModelSessionBeanRemote;
    private CarCategorySessionBeanRemote carCategorySessionBeanRemote;
    private Employee currEmployee;

    public MainApp() {

    }

    public MainApp(EmployeeSessionBeanRemote employeeSessionBeanRemote, OutletSessionBeanRemote outletSessionBeanRemote, CarCategorySessionBeanRemote carCategorySessionBeanRemote, CarModelSessionBeanRemote carModelSessionBeanRemote, RentalRateSessionBeanRemote rentalRateSessionBeanRemote) {
        this();
        this.employeeSessionBeanRemote = employeeSessionBeanRemote;
        this.outletSessionBeanRemote = outletSessionBeanRemote;
        this.carModelSessionBeanRemote = carModelSessionBeanRemote;
        this.carCategorySessionBeanRemote = carCategorySessionBeanRemote;
        this.rentalRateSessionBeanRemote = rentalRateSessionBeanRemote;
    }

    public void runApp() throws InvalidLoginException, InputDataValidationException {
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
                        operationsMenu();
                    } catch (InvalidLoginException ex) {
                        System.out.println("Invalid Login: " + ex.getMessage());
                    }
                } else if (input == 2) {
                    break;//to be written
                } else if (input == 3) {
                    break;
                } else {
                    System.out.println("Invalid input, please try again!");
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

        System.out.println("*** Enter Login Details ***");
        System.out.println("Enter Your Employee Username: ");
        employeeUsername = sc.nextLine().trim();
        System.out.println("Enter Your Password: ");
        password = sc.nextLine().trim();

        if (employeeUsername.length() > 0 && password.length() > 0) {
            currEmployee = employeeSessionBeanRemote.login(employeeUsername, password);
        } else {
            throw new InvalidLoginException("Wrong Password!");
        }

    }

    private void operationsMenu() {
        Scanner sc = new Scanner(System.in);
        Integer input = 0;

        while (true) {
            System.out.println("*** Welcome to the Operations Menu ***");
            System.out.println("Hello! " + currEmployee.getEmployeeFirstName() + " " + currEmployee.getEmployeeLastName());
            System.out.println("Your role is: " + currEmployee.getEmployeeRole().toString());
            System.out.println("Input 1 to the Sales Management Menu");
            System.out.println("Input 2 to the Customer Service Menu");
            System.out.println("Input 3 to log out");

            input = 0;

            while (input < 1 || input > 3) {
                System.out.print("Your input: ");
                input = sc.nextInt();

                //try {
                if (input == 1) {
                    salesManagementMenu();
                    break;//to be written
                } else if (input == 2) {
                    break;//to be writtem
                } else if (input == 3) {
                    break;
                } else {
                    System.out.println("Invalid input, please try again!");
                }
                //to be uncomment
                //} catch (InvalidAccessRightException ex) {
                //    System.out.println("Invalid option, please try again!: " + ex.getMessage() + "\n");
                //} catch (UnpaidRentalReservationException ex) {
                //    System.out.println("Customer has not paid for the car rental reservation!");
                //}
            }
            if (input == 3) {
                System.out.println("***Logout Successfully***");
                break;
            }
        }
    }

    private void salesManagementMenu() {
        Scanner sc = new Scanner(System.in);
        Integer input = 0;

        while (true) {
            System.out.println("*** Welcome to the Sales Management Menu ***");
            System.out.println("[1] Create New Model");
            System.out.println("[2] View All Models");
            System.out.println("[3] Update Model");
            System.out.println("[4] Delete Model");
            System.out.println("[5] Create New Rental Rate");
            System.out.println("[6] View All Rental Rates");
            System.out.println("[7] View Rental Rate Details");
            System.out.println("[8] Update Rental Rate");
            System.out.println("[9] Delete Rental Rate");
            System.out.println("[10] Exit"); //Remember to make changes to the while loop
            //Issues are hard to resolve

            input = 0;

            while (input < 1 || input > 10) {
                System.out.print("Your input: ");
                input = sc.nextInt();

                if (input == 1) {
                    createNewModel();
                    break;
                } else if (input == 2) {
                    viewAllModels();
                    break;
                } else if (input == 3) {
                    updateModel();
                    break;
                } else if (input == 4) {
                    deleteModel();
                    break;
                } else if (input == 5) {
                    createNewRentalRate(); 
                    break;
                } else if (input == 6) {
                    viewAllRentalRates();
                    break;
                } else if (input == 7) {
                    viewRentalRateDetails();
                    break;
                } else if (input == 8) {
                    updateRentalRate();
                    break;
                } else if (input == 9) {
                    deleteRentalRate();
                    break;
                } else if (input == 10) {
                    break;
                } else {
                    System.out.println("Invalid input, please try again!");
                }
            }

            if (input == 10) {
                break;
            }
        }

    }

    private void createNewModel() {
        Scanner sc = new Scanner(System.in);

        System.out.println("*** Create New Model ***");

        CarModel m = new CarModel();
        System.out.print("Enter the name of the model: ");
        String model = sc.nextLine().trim();
        m.setModel(model);
        System.out.print("Enter the make of the model: ");
        String make = sc.nextLine().trim();
        m.setMake(make);

        System.out.print("Enter the belonged car category Id: ");
        long categoryId = sc.nextLong();
        long modelId = 0;
        try {
            modelId = carModelSessionBeanRemote.createNewCarModel(categoryId, m);

            if (modelId == -1) {
                System.out.print("An Persistence Error Occured While Creating a New Car Model.");
                return;
            }

        } catch (CarCategoryNotFoundException ex) {
            System.out.println("Car Category Not Found! Please Enter an Valid Car Category Id.");
            return;
        } catch (PersistenceException ex) {
            System.out.print("An Error Occured While Creating a New Car Model: ");
            System.out.println(ex.getMessage());
            return;
        }

        System.out.println("*** The new car model is successfully created ***");
        System.out.println("The new car model id is:[ " + modelId + " ]");

        System.out.println();
        System.out.println("Press any key to continue.");
        System.out.println();
    }
    
    private void createNewRentalRate() {
        Scanner sc = new Scanner(System.in);

        System.out.println("*** Create New Rental Rate ***");

        RentalRate r = new RentalRate();
  
       
        System.out.print("Enter the car category ID of this rental rate: ");
        long carCategoryId = sc.nextLong();
        sc.nextLine();
        
        //SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        /*System.out.print("Enter the start date of this rental rate (DD/MM/YYYY HH:MM) > ");
        String stringStartDate = sc.nextLine().trim();
        */
        
        try {
            r.setCarCategory(carCategorySessionBeanRemote.retrieveCarCategoryByCarCategoryId(carCategoryId));
            
            System.out.print("Enter the name of the rental rate: ");
            String rentalName = sc.nextLine().trim();
            r.setRentalName(rentalName);
             
            System.out.print("Enter the daily rental rate: ");
            BigDecimal dailyRate = sc.nextBigDecimal();
            r.setDailyRate(dailyRate);
            sc.nextLine();
       
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
            System.out.print("Enter start date (DD/MM/YYYY HH:MM)> ");
            Date startDate = sdf.parse(sc.nextLine().trim());
            System.out.print("Enter end date (DD/MM/YYYY HH:MM)> ");
            Date endDate = sdf.parse(sc.nextLine().trim());
                if (endDate.before(startDate)) {
                    throw new EndDateBeforeStartDateException();
                }
                r.setRateStartDate(startDate);
                r.setRateEndDate(endDate);
            
            Long rentalRateId = rentalRateSessionBeanRemote.createNewRentalRate(carCategoryId, r);
            System.out.println("Rental Rate ID: " + rentalRateId + " sucessfully created!");
        } catch (ParseException ex) {
            System.out.println("Invalid Date/Time Format!");
        } catch (CarCategoryNotFoundException ex) {
            System.out.println("No such Car Category of ID: " + carCategoryId);
        } catch (UnknownPersistenceException ex) {
            System.out.println("UnknownPersistenceException when creating new Rental Rate");
        } catch (InputDataValidationException ex) {
            System.out.println("Invalid fields for the rental rate");
        } catch (EndDateBeforeStartDateException ex) {
            System.out.println("End date is before start date!");
        }


        
    }
    
    private void viewAllModels() {
        Scanner sc = new Scanner(System.in);
        System.out.println("*** View All Models ***");
        List<CarModel> models = carModelSessionBeanRemote.retrieveAllCarModels();
        System.out.printf("%8s%30s%30s%30s\n", "Model Id", "Name", "Make", "Category");
        for (CarModel m : models) {
            System.out.printf("%8s%30s%30s%30s\n", m.getModelId(), m.getModel(), m.getMake(), m.getBelongsCategory().getCarCategoryName());
        }
        System.out.println();
    }

      private void viewAllRentalRates() {
        //Scanner sc = new Scanner(System.in);
        System.out.println("*** View All Rental Rates ***");
        
        List<RentalRate> rentalRates = rentalRateSessionBeanRemote.retrieveAllRentalRates();
        
        System.out.printf("%4s%32s%32s%16s%16s%20s%20s\n", "ID", "Rental Rate Name", "Car Category", "Rate Per Day", "Is Applied?", "Start Period", "End Period");
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        for (RentalRate rentalRate : rentalRates) {
            String startDate = "null";
            if (rentalRate.getRateStartDate() != null) {
                startDate = sdf.format(rentalRate.getRateStartDate());
            }
            String endDate = "null";
            if (rentalRate.getRateEndDate() != null) {
                endDate = sdf.format(rentalRate.getRateEndDate());
            }
            System.out.printf("%4s%32s%32s%16s%16s%20s%20s\n", rentalRate.getRentalRateId(),
                    rentalRate.getRentalName(), rentalRate.getCarCategory().getCarCategoryName(),
                    rentalRate.getDailyRate(), true, startDate, endDate); //Change true to the isApplied attribute, although it should be the isEnabled attribute
        }
        System.out.println();

    }
      
      private void viewRentalRateDetails() {
        Scanner sc = new Scanner(System.in);
          
        System.out.print("Enter Rental Rate ID> ");
        Long rentalRateId = sc.nextLong();
        
        try {
            RentalRate r = rentalRateSessionBeanRemote.retrieveRentalRateByRentalRateId(rentalRateId);
            
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
            String startDate = "Always Available";
            if (r.getRateStartDate() != null) {
                startDate = sdf.format(r.getRateStartDate());
            }
            
            String endDate = "";
            if (r.getRateEndDate()!= null) {
                endDate = sdf.format(r.getRateEndDate());
            }
            System.out.printf("%4s%32s%32s%16s%16s%20s%20s\n", "ID", "Rental Rate Name", "Car Category", "Rate Per Day", "Is Enabled?", "Start Period", "End Period");
            System.out.printf("%4s%32s%32s%16s%16s%20s%20s\n", r.getRentalRateId(),
                    r.getRentalName(), r.getCarCategory().getCarCategoryName(),
                    r.getDailyRate(), true, startDate, endDate);
        } catch (RentalRateNotFoundException ex) {
            System.out.println("Rental Rate not found for ID: " + rentalRateId);
        }
        System.out.println();
      }
      
    private void updateModel() {
        Scanner sc = new Scanner(System.in);
        System.out.println("*** Update Model ***");

        System.out.print("Enter the model id: ");
        long modelId = sc.nextLong();
        sc.nextLine();
        CarModel model = carModelSessionBeanRemote.retrieveCarModelById(modelId);

        if (model == null) {
            System.out.println("The model is not found! Please enter an valid model id.");
            System.out.println("");
            return;
        }

        System.out.println("");
        System.out.print("Do you want to update the model name? [1]YES/[2]NO : ");
        int modelInput = sc.nextInt();
        if (modelInput == 1) {
            System.out.print("Enter the new model name: ");
            String updatedModel = sc.next();
            model.setModel(updatedModel);
        }

        System.out.println("");
        System.out.print("Do you want to update the make name? [1]YES/[2]NO : ");
        int makeInput = sc.nextInt();
        if (makeInput == 1) {
            System.out.print("Enter the new make name: ");
            String updatedMake = sc.next();
            model.setMake(updatedMake);
        }
        long categoryId = model.getBelongsCategory().getCategoryId();
        System.out.println("");
        System.out.print("Do you want to update the belonged car category? [1]YES/[2]NO : ");
        int categoryInput = sc.nextInt();
        if (categoryInput == 1) {
            System.out.print("Enter the new car category id: ");
            categoryId = sc.nextLong();
            sc.nextLine();
        }
        long updatedModelId;
        try {
            updatedModelId = carModelSessionBeanRemote.updateModel(model, categoryId);
            if (updatedModelId == -1) {
                System.out.println("An Persistence Error Occured While Creating a New Car Model.\n");
                return;
            }
        } catch (CarCategoryNotFoundException ex) {
            System.out.println("Car Category Not Found! Please Enter an Valid Car Category Id.");
            return;
        }

        System.out.println("*** The car model is successfully updated ***");
        System.out.println();
        System.out.println("Press any key to continue.");
        System.out.println();
    }
    
    private void updateRentalRate() {
        Scanner sc = new Scanner(System.in);
        System.out.println("*** Update Rental Rate ***");

        System.out.print("Enter the rental rate id: ");
        long rentalRateId = sc.nextLong();
        sc.nextLine();
        RentalRate r = new RentalRate();
        try {
            r = rentalRateSessionBeanRemote.retrieveRentalRateByRentalRateId(rentalRateId);
        } catch (RentalRateNotFoundException ex) {
            System.out.println("Rental Rate Not Found! Please Enter an Valid Rental Rate Id.");
        }
        try {
            System.out.print("Do you want to update the car category? [1]YES/[2]NO : ");
            int catInput = sc.nextInt();
            if (catInput == 1) {
                System.out.print("Enter the new category ID: ");
                Long updatedCategoryId = sc.nextLong();
                sc.nextLine();
                try {
                    r.setCarCategory(carCategorySessionBeanRemote.retrieveCarCategoryByCarCategoryId(updatedCategoryId));
                } catch (CarCategoryNotFoundException ex) {
                    System.out.println("Car Category Not Found! Please Enter an Valid Car Category Id.");
                }
            }
            
            System.out.println("");
            System.out.print("Do you want to update the rate per day? [1]YES/[2]NO : ");
            int rateInput = sc.nextInt();
            if (rateInput == 1) {
                System.out.print("Enter the new rate: ");
                BigDecimal updateRate = sc.nextBigDecimal();
                sc.nextLine();
                r.setDailyRate(updateRate);
            }
            
            System.out.println("");
            System.out.print("Do you want to update the start date? [1]YES/[2]NO : ");
            int startDateInput = sc.nextInt();
            if (startDateInput == 1) {
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                System.out.print("Enter the new start date (DD/MM/YYYY HH:MM)> ");
                Date startDate = sdf.parse(sc.nextLine().trim());
                sc.nextLine();
                r.setRateStartDate(startDate);
            }
        
            System.out.println("");
            System.out.print("Do you want to update the end date? [1]YES/[2]NO : ");
            int endDateInput = sc.nextInt();
            if (endDateInput == 1) {
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                System.out.print("Enter the new end date (DD/MM/YYYY HH:MM)> ");
                Date endDate = sdf.parse(sc.nextLine().trim());
                sc.nextLine();
                r.setRateStartDate(endDate);
            }
         
            rentalRateSessionBeanRemote.updateRentalRate(r);
            System.out.println("Rental Rate ID: " + rentalRateId + " updated!");
        } catch (RentalRateNotFoundException ex) {
            System.out.println("No Rental Rate of ID: " + rentalRateId);
        } catch (InputDataValidationException ex) {
            System.out.println("Rental Rate name already exists in the database! " + r.getRentalName());
        } catch (ParseException ex) {
            System.out.println("Invalid Date/Time Format!");
        }
        
        System.out.println("Press any key to continue.");
        System.out.println();
        
    }
    

    private void deleteModel() {
        Scanner sc = new Scanner(System.in);
        System.out.println("***Delete Model ***");

        System.out.print("Enter the model id: ");
        long modelId = sc.nextLong();
        sc.nextLine();

        try {
            carModelSessionBeanRemote.deleteModel(modelId);
        } catch (CarModelNotFoundException ex) {
            System.out.println("The model is not found! Please enter an valid model id.");
            return;
        }

        System.out.println("*** The car model is successfully deleted ***");
        System.out.println("The deleted car model id is:[ " + modelId + " ]");

        System.out.println();
        System.out.println("Press any key to continue.");
        System.out.println();
    }
    
    private void deleteRentalRate() {
        Scanner sc = new Scanner(System.in);
        System.out.println("***Delete Rental Rate ***");
        
        System.out.print("Enter the rental rate id: ");
        long rateId = sc.nextLong();
        sc.nextLine();
        
        try {
            rentalRateSessionBeanRemote.deleteRentalRate(rateId);
        } catch (RentalRateNotFoundException ex) {
            System.out.println("The rental rate is not found! Please enter an valid model id.");
            return;
        }
        
        System.out.println("*** The rental rate has been successfully deleted ***");
        System.out.println("The deleted rental rate id is:[ " + rateId + " ]");

        System.out.println();
        System.out.println("Press any key to continue.");
        System.out.println();
        
    }
}
