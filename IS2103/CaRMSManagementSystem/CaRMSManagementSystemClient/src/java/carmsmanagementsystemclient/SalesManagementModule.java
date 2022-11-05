/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package carmsmanagementsystemclient;

import ejb.session.stateless.CarCategorySessionBeanRemote;
import ejb.session.stateless.CarModelSessionBeanRemote;
import ejb.session.stateless.CarSessionBeanRemote;
import ejb.session.stateless.EmployeeSessionBeanRemote;
import ejb.session.stateless.OutletSessionBeanRemote;
import ejb.session.stateless.RentalRateSessionBeanRemote;
import ejb.session.stateless.TransitDriverDispatchSessionBeanRemote;
import entity.Car;
import entity.CarModel;
import entity.Employee;
import entity.RentalRate;
import entity.TransitDriverDispatch;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import javax.persistence.PersistenceException;
import util.enumeration.CarStatusEnum;
import util.enumeration.EmployeeRoleEnum;
import util.exception.CarCategoryNotFoundException;
import util.exception.CarModelDeletionException;
import util.exception.CarModelNotFoundException;
import util.exception.CarNotFoundException;
import util.exception.EndDateBeforeStartDateException;
import util.exception.InputDataValidationException;
import util.exception.InvalidAccessRightException;
import util.exception.InvalidModelException;
import util.exception.LicensePlateExistException;
import util.exception.ModelDisabledException;
import util.exception.OutletNotFoundException;
import util.exception.RentalDateDeletionException;
import util.exception.RentalRateNotFoundException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author Mehak
 */
public class SalesManagementModule {

    private RentalRateSessionBeanRemote rentalRateSessionBeanRemote;
    private EmployeeSessionBeanRemote employeeSessionBeanRemote;
    private OutletSessionBeanRemote outletSessionBeanRemote;
    private CarModelSessionBeanRemote carModelSessionBeanRemote;
    private CarCategorySessionBeanRemote carCategorySessionBeanRemote;
    private CarSessionBeanRemote carSessionBeanRemote;
    private TransitDriverDispatchSessionBeanRemote transitDriverDispatchSessionBeanRemote;
    private Employee currEmployee;

    public SalesManagementModule() {
    }

    public SalesManagementModule(RentalRateSessionBeanRemote rentalRateSessionBeanRemote, EmployeeSessionBeanRemote employeeSessionBeanRemote, OutletSessionBeanRemote outletSessionBeanRemote, CarModelSessionBeanRemote carModelSessionBeanRemote, CarCategorySessionBeanRemote carCategorySessionBeanRemote, CarSessionBeanRemote carSessionBeanRemote, TransitDriverDispatchSessionBeanRemote transitDriverDispatchSessionBeanRemote, Employee currEmployee) {
        this.rentalRateSessionBeanRemote = rentalRateSessionBeanRemote;
        this.employeeSessionBeanRemote = employeeSessionBeanRemote;
        this.outletSessionBeanRemote = outletSessionBeanRemote;
        this.carModelSessionBeanRemote = carModelSessionBeanRemote;
        this.carCategorySessionBeanRemote = carCategorySessionBeanRemote;
        this.carSessionBeanRemote = carSessionBeanRemote;
        this.transitDriverDispatchSessionBeanRemote = transitDriverDispatchSessionBeanRemote;
        this.currEmployee = currEmployee;
    }

    /**
     * @param args the command line arguments
     * @throws util.exception.InvalidAccessRightException
     */
    public void menuSalesManagement() throws InvalidAccessRightException {
        System.out.println("*** Welcome to CarMS Sales Management Menu ***\n");
        if (currEmployee.getEmployeeRole() == EmployeeRoleEnum.SALES_MANAGER) {
            salesManagerMenu();
        } else if (currEmployee.getEmployeeRole() == EmployeeRoleEnum.OPERATIONS_MANAGER) {
            operationManagerMenu();
        } else {
            throw new InvalidAccessRightException("You Don't Have the Right to Access the Sales Management Module.");
        }
    }

    // TODO code application logic here
    public void salesManagerMenu() {
        Scanner sc = new Scanner(System.in);
        Integer input = 0;

        while (true) {
            System.out.println("Welcome! Sales Manager: " + currEmployee.getEmployeeFirstName() + " " + currEmployee.getEmployeeLastName());

            System.out.println("[1] Create New Rental Rate");
            System.out.println("[2] View All Rental Rates");
            System.out.println("[3] View Rental Rate Details");
            System.out.println("[4] Update Rental Rate");
            System.out.println("[5] Delete Rental Rate");
            System.out.println("[6] Exit");

            input = 0;

            while (input < 1 || input > 6) {
                System.out.print("Your input: ");
                input = sc.nextInt();

                if (input == 1) {
                    createNewRentalRate();
                    break;
                } else if (input == 2) {
                    viewAllRentalRates();
                    break;
                } else if (input == 3) {
                    viewRentalRateDetails();
                    break;
                } else if (input == 4) {
                    updateRentalRate();
                    break;
                } else if (input == 5) {
                    deleteRentalRate();
                    break;
                } else if (input == 6) {
                    break;
                } else {
                    System.out.println("Invalid input, please try again!");
                }
            }

            if (input == 6) {
                break;
            }
        }

    }

    public void operationManagerMenu() {

        Scanner sc = new Scanner(System.in);
        Integer input = 0;

        while (true) {
            System.out.println("Welcome! Operations Manager: " + currEmployee.getEmployeeFirstName() + " " + currEmployee.getEmployeeLastName());
            System.out.println("[1] Create New Model");
            System.out.println("[2] View All Models");
            System.out.println("[3] Update Model");
            System.out.println("[4] Delete Model");
            System.out.println("");
            System.out.println("[5] Create New Car");
            System.out.println("[6] View All Cars");
            System.out.println("[7] View a Car's Details");
            System.out.println("");
            System.out.println("[8] View Transit Driver Dispatch Record for Current Day Reservation");
            System.out.println("[9] Assign Transit Driver");
            System.out.println("[10] Update Transit As Completed");
            System.out.println("[11] Exit"); //Remember to make changes to the while loop
            //Issues are hard to resolve

            input = 0;

            while (input < 1 || input > 11) {
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
                    createNewCar();
                    break;
                } else if (input == 6) {
                    viewAllCars();
                    break;
                } else if (input == 7) {
                    viewCarDetails();
                    break;
                } else if (input == 8) {
                    viewTransitDriverDispatchRecordsForCurrentDayReservations();
                } else if (input == 9) {
                    assignTransitDriver();
                } else if (input == 10) {
                    updateTransitAsCompleted();
                } else if (input == 11) {
                    break;
                } else {
                    System.out.println("Invalid input, please try again!");
                }
            }

            if (input == 11) {
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

        try {
            r.setCarCategory(carCategorySessionBeanRemote.retrieveCarCategoryByCarCategoryId(carCategoryId));

            System.out.print("Enter the name of the rental rate: ");
            String rentalName = sc.nextLine().trim();
            r.setRentalName(rentalName);

            System.out.print("Enter the daily rental rate: ");
            BigDecimal dailyRate = sc.nextBigDecimal();
            r.setDailyRate(dailyRate);
            sc.nextLine();

            System.out.print("Enter validity period? (Enter 'Y' to set validity period> ");
            String validity = sc.nextLine().trim();

            if (validity.equals("Y")) {
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
            } else {
                System.out.println("Validity period not entered!");
            }

            Long rentalRateId = rentalRateSessionBeanRemote.createNewRentalRate(carCategoryId, r);
            System.out.println("Rental Rate ID: " + rentalRateId + " sucessfully created!");

            System.out.println();
            System.out.println("Press any key to continue.");
            System.out.println();

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

    private void createNewCar() {
        Scanner sc = new Scanner(System.in);
        Car car = new Car();
        System.out.println("*** Create new Car ***\n");
        System.out.print("Enter license plate number> ");
        car.setCarLicensePlateNum(sc.nextLine().trim());
        System.out.print("Enter colour> ");
        car.setColour(sc.nextLine().trim());
        System.out.print("Enter model ID> ");
        Long modelId = sc.nextLong();
        System.out.print("Enter outlet ID> ");
        Long outletId = sc.nextLong();
        sc.nextLine();
        try {
            Long carId = carSessionBeanRemote.createNewCar(modelId, outletId, car);
            System.out.println("New Car succesfully created with ID " + carId);
        } catch (CarModelNotFoundException ex) {
            System.out.println(ex.getMessage());
        } catch (OutletNotFoundException ex) {
            System.out.println(ex.getMessage());
        } catch (LicensePlateExistException ex) {
            System.out.println("License Plate : + " + car.getCarLicensePlateNum() + " already exists!");
        } catch (UnknownPersistenceException ex) {
            System.out.println("Unknown persistence exception");
        } catch (InputDataValidationException ex) {
            System.out.println("Input data validation exception");
        } catch (ModelDisabledException ex) {
            System.out.println("Model is disabled and new car record should not be created with the disabled model");
        }
        System.out.print("Press any key to continue...> ");
        sc.nextLine();
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
            if (r.getRateEndDate() != null) {
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

    private void viewAllCars() {
        Scanner sc = new Scanner(System.in);
        System.out.println("*** View All Cars ***");
        List<Car> cars = carSessionBeanRemote.retrieveCars();
        System.out.printf("%4s%32s%16s%16s%22s\n", "ID", "Car Category", "Make", "Model", "License Plate Number");
        for (Car car : cars) {
            System.out.printf("%4s%32s%16s%16s%22s\n", car.getCarId(),
                    car.getCarModel().getBelongsCategory().getCarCategoryName(),
                    car.getCarModel().getMake(), car.getCarModel().getModel(),
                    car.getCarLicensePlateNum());
        }
        System.out.print("Press any key to continue...> ");
        sc.nextLine();
    }

    private void viewCarDetails() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("*** View Car Details ***");
        System.out.print("Enter Car ID> ");
        Long carId = scanner.nextLong();
        scanner.nextLine();
        try {
            Car car = carSessionBeanRemote.retrieveCarByCarId(carId);
            System.out.printf("%4s%32s%16s%16s%22s\n", "ID", "Car Category", "Make", "Model", "License Plate Number");
            System.out.printf("%4s%32s%16s%16s%22s\n", car.getCarId(), car.getCarModel().getBelongsCategory().getCarCategoryName(),
                    car.getCarModel().getMake(), car.getCarModel().getModel(),
                    car.getCarLicensePlateNum());

            System.out.println("------------------------");
            System.out.println("1: Update Car");
            System.out.println("2: Delete Car");
            System.out.println("3: Back\n");
            System.out.print("> ");
            int response = scanner.nextInt();

            if (response == 1) {
                updateCar(car);
            } else if (response == 2) {
                deleteCar(car);
            }

        } catch (CarNotFoundException ex) {
            System.out.println("Car not found for ID: " + carId);
        }
        System.out.print("Press any key to continue...> ");
        scanner.nextLine();
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
        } catch (CarModelDeletionException ex) {
            System.out.println("This model is deleted! Please Enter an Valid Car Category Id.");
            return;
        }

        System.out.println("*** The car model is successfully updated ***");
        System.out.println();
        System.out.println("Press any key to continue.");
        System.out.println();
    }

    private void updateRentalRate() {
        Scanner sc = new Scanner(System.in);
        RentalRate r = new RentalRate();
        System.out.println("*** Update Rental Rate ***");

        System.out.print("Enter the rental rate id: ");
        long rentalRateId = sc.nextLong();
        sc.nextLine();

        try {

            System.out.println("");
            System.out.print("Do you want to update the rental name? [1]YES/[2]NO : ");
            int nameInput = sc.nextInt();
            if (nameInput == 1) {
                sc.nextLine();
                System.out.print("Enter the new rental rate name: ");
                String updatedRentalRateName = sc.nextLine();
                r.setRentalName(updatedRentalRateName);
            }

            r = rentalRateSessionBeanRemote.retrieveRentalRateByRentalRateId(rentalRateId);
            System.out.print("Do you want to update the car category? [1]YES/[2]NO : ");
            int catInput = sc.nextInt();
            if (catInput == 1) {
                sc.nextLine();
                System.out.print("Enter the new category ID: ");
                Long updatedCategoryId = sc.nextLong();
                r.setCarCategory(carCategorySessionBeanRemote.retrieveCarCategoryByCarCategoryId(updatedCategoryId));

            }

            System.out.println("");
            System.out.print("Do you want to update the rate per day? [1]YES/[2]NO : ");
            int rateInput = sc.nextInt();
            if (rateInput == 1) {
                sc.nextLine();
                System.out.print("Enter the new rate: ");
                BigDecimal updateRate = sc.nextBigDecimal();
                r.setDailyRate(updateRate);
            }

            System.out.println("");
            System.out.print("Do you want to update the validity period's start date? [1]YES/[2]NO : ");
            int startDateInput = sc.nextInt();
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
            if (startDateInput == 1) {
                sc.nextLine();
                System.out.print("Enter the new start date (DD/MM/YYYY HH:MM)> ");
                Date updatedStartDate = sdf.parse(sc.nextLine().trim());
                r.setRateStartDate(updatedStartDate);
            }

            System.out.println("");
            System.out.print("Do you want to update  the validity period's end date? [1]YES/[2]NO : ");
            int endDateInput = sc.nextInt();
            if (endDateInput == 1) {
                sc.nextLine();
                System.out.print("Enter the new end date (DD/MM/YYYY HH:MM)> ");
                Date updatedEndDate = sdf.parse(sc.nextLine().trim());
                r.setRateEndDate(updatedEndDate);
            }

            rentalRateSessionBeanRemote.updateRentalRate(r);
            System.out.println("Rental Rate ID: " + rentalRateId + " updated!");
        } catch (RentalRateNotFoundException ex) {
            System.out.println("No Rental Rate of ID: " + rentalRateId);
            return;
        } catch (InputDataValidationException ex) {
            System.out.println("Rental Rate name already exists in the database! " + r.getRentalName());
            return;
        } catch (ParseException ex) {
            System.out.println("Invalid Date/Time Format!");
            return;
        } catch (CarCategoryNotFoundException ex) {
            System.out.println("Car Category Not Found! Please Enter an Valid Car Category Id.");
            return;
        } catch (RentalDateDeletionException ex) {
            System.out.println("This rental rate has been deleted! Please Enter a Valid Rental Rate Id.");
            return;
        }

        System.out.println("Press any key to continue.");
        System.out.println();

    }

    private void updateCar(Car car) {
        Scanner sc = new Scanner(System.in);
        System.out.println("");
        System.out.println("*** Update Car ***");

        System.out.print("Do you want to update the plate number? [1]YES/[2]NO : ");
        int plateNumberInput = sc.nextInt();
        if (plateNumberInput == 1) {
            sc.nextLine();
            System.out.print("Enter the plate number: ");
            String newPlateNumber = sc.nextLine().trim();
            car.setCarLicensePlateNum(newPlateNumber);
        }

        System.out.print("Do you want to update the colour? [1]YES/[2]NO : ");
        int colorInput = sc.nextInt();
        if (colorInput == 1) {
            sc.nextLine();
            System.out.print("Enter the new colour: ");
            String newColor = sc.nextLine().trim();
            car.setColour(newColor);
        }

        CarStatusEnum carStatus;

        System.out.println("");
        System.out.println("[1] OUTLET");
        System.out.println("[2] ON RENTAL");
        System.out.println("[3] REPAIR");

        int statusNum = 0;

        while (true) {
            System.out.print("Choose the new status: ");
            statusNum = sc.nextInt();
            sc.nextLine();
            if (statusNum == 1) {
                carStatus = CarStatusEnum.AVAILABLE;
                car.setDisabled(false);
                break;
            } else if (statusNum == 2) {
                carStatus = CarStatusEnum.IN_RENT;
                car.setDisabled(false);
                break;
            } else if (statusNum == 3) {
                carStatus = CarStatusEnum.REPAIR_SERVICE;
                car.setDisabled(true);
                break;
            } else {
                System.out.println("Invalid input! Please input again!");
            }
        }

        long modelId = car.getCarModel().getModelId();
        long outletId = car.getLatestOutlet().getOutletId();

        while (true) {
            System.out.println("Do you want to update the Model? [1]YES/[2]NO : ");
            int updateModelInput = sc.nextInt();
            if (updateModelInput == 1) {
                sc.nextLine();
                System.out.print("Enter the new model id > ");
                modelId = sc.nextLong();
                sc.nextLine();
                break;
            }

            System.out.println("Do you want to update the Outlet? [1]YES/[2]NO : ");
            int updateOutletInput = sc.nextInt();
            if (updateOutletInput == 1) {
                sc.nextLine();
                System.out.print("Enter the new Outlet ID > ");
                outletId = sc.nextLong();
                sc.nextLine();
                break;
            }
        }

        car.setCarStatus(carStatus);
        long newId;

        try {
            newId = carSessionBeanRemote.updateCar(car, outletId, modelId);
            if (newId == -1) {
                System.out.println("An error occurred while updating car!");
                System.out.println("Plate Number/Color too long!\n");
                return;
            }
        } catch (CarModelNotFoundException ex) {
            System.out.println(ex.getMessage());
            System.out.println("Update denied!\n");
            return;
        } catch (InvalidModelException ex) {
            System.out.println("Model is invalid!");
            System.out.println("Update denied!\n");
            return;
        } catch (OutletNotFoundException ex) {
            System.out.println(ex.getMessage());
            //System.out.println("Outlet is invalid!");
            System.out.println("Update denied\n");
            return;
        }

        System.out.println("");
        System.out.println("Update is successful!");
        System.out.println("Press enter to continue!");
        sc.nextLine();
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
        System.out.println("*** Delete Rental Rate ***");

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

    private void deleteCar(Car car) {
        Scanner sc = new Scanner(System.in);
        System.out.println("*** Delete Car ***");
        Long carId = car.getCarId();
        try {
            carSessionBeanRemote.deleteCar(carId);
            System.out.println("Car ID: " + carId + " sucessfully deleted!");
        } catch (CarNotFoundException ex) {
            System.out.println("Model Rate not found for ID: " + carId);
        }
        System.out.print("Press any key to continue.");
        sc.nextLine();
    }

    private void viewTransitDriverDispatchRecordsForCurrentDayReservations() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("*** View Transit Driver Dispatch Records for Current Day Reservations ***\n");
        System.out.print("Enter Date (DD/MM/YYYY) > ");
        String inputDate = scanner.nextLine().trim();
        try {
            SimpleDateFormat inputFormat = new SimpleDateFormat("DD/MM/YYYY");
            Date date = inputFormat.parse(inputDate);

            System.out.println("Transit Driver Dispatch records for current Outlet:" + currEmployee.getOutlet().getOutletName() + " on " + inputDate + "\n");
            List<TransitDriverDispatch> transitDriverDispatchs = transitDriverDispatchSessionBeanRemote.retrieveTransitDriverDispatchByOutletId(date, currEmployee.getOutlet().getOutletId());
            System.out.printf("%15s%30s%20s%20s%20s\n", "Record ID", "Destination Outlet", "Driver", "Status", "Transit Time");
            SimpleDateFormat sdf = new SimpleDateFormat("DD/MM/YYYY HH:MM");

            for (TransitDriverDispatch transitDriverDispatch : transitDriverDispatchs) {
                String completionStatus = "Not Completed";
                if (transitDriverDispatch.isIsCompleted()) {
                    completionStatus = "Completed";
                }
                String dispatchDriverName = "Unassigned";
                if (transitDriverDispatch.getDispatchDriver() != null) {
                    dispatchDriverName = transitDriverDispatch.getDispatchDriver().getEmployeeFirstName() + " "
                            + transitDriverDispatch.getDispatchDriver().getEmployeeLastName();
                }
                String transitDate = sdf.format(transitDriverDispatch.getTransitDate());
                System.out.printf("%15s%30s%20s%20s%20s\n",
                        transitDriverDispatch.getTransitDriverDispatchId(),
                        transitDriverDispatch.getDestinationOutlet().getOutletName(),
                        dispatchDriverName, completionStatus, transitDate);
            }
        } catch (ParseException ex) {
            System.out.println("Invalid input! Please input again!");
        }
        System.out.print("Press any key to continue. ");
        scanner.nextLine();
    }

    private void assignTransitDriver() {

    }

    private void updateTransitAsCompleted() {

    }
}
