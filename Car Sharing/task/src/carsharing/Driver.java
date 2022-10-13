package carsharing;

import carsharing.dao.CarDaoImpl;
import carsharing.dao.CompanyDaoImpl;
import carsharing.dao.CustomerDaoImpl;
import carsharing.model.Car;
import carsharing.model.Company;
import carsharing.model.Customer;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Driver {
    private final Scanner scanner = new Scanner(System.in);
    private final CompanyDaoImpl companyDao;
    private final CarDaoImpl carDao;
    private final CustomerDaoImpl customerDao;
    private int choice;

    public Driver(CompanyDaoImpl companyDao, CarDaoImpl carDao, CustomerDaoImpl customerDao) {
        this.companyDao = companyDao;
        this.carDao = carDao;
        this.customerDao = customerDao;
    }

    public void start() {
        managerMenu();
    }

    private void managerMenu() {
        System.out.println("1. Log in as a manager\n" +
                "2. Log in as a customer\n" +
                "3. Create a customer\n" +
                "0. Exit");
        choice = scanner.nextInt();
        switch (choice) {
            case 0:
                break;
            case 1:
                companyMenu();
                break;
            case 2:
                customerMenu();
                break;
            case 3:
                createCustomer();
                break;
            default:
                System.out.println("Bad choice");
                managerMenu();
        }
    }

    private void customerMenu() {
        List<Customer> customers = customerDao.getAll();
        if (customers.isEmpty()) {
            System.out.println("The customer list is empty!");
            managerMenu();
        } else {
            System.out.println("Customer list:");
            customers.forEach(System.out::println);
            System.out.println("0. Back");
            choice = scanner.nextInt();
            if (choice == 0) {
                managerMenu();
            } else {
                customerCarMenu(choice);
            }
        }
    }

    private void customerCarMenu(int customerId) {
        System.out.println("1. Rent a car\n" +
                "2. Return a rented car\n" +
                "3. My rented car\n" +
                "0. Back");
        choice = scanner.nextInt();
        Customer customer;
        switch (choice) {
            case 0:
                managerMenu();
                break;
            case 1:
                customer = customerDao.getById(customerId);
                if (customer.getRentedCarId() == -1) {
                    availableCompanies(customerId);
                } else {
                    System.out.println("You've already rented a car!");
                    customerCarMenu(customerId);
                }
                break;
            case 2:
                customer = customerDao.getById(customerId);
                if (customer.getRentedCarId() == -1) {
                    System.out.println("You didn't rent a car!");
                } else {
                    customer.setRentedCarId(-1);
                    customerDao.update(customer, null);
                    System.out.println("You've returned a rented car!");
                }
                customerCarMenu(customerId);
                break;
            case 3:
                customer = customerDao.getById(customerId);
                if (customer.getRentedCarId() == -1) {
                    System.out.println("You didn't rent a car!");
                } else {
                    Car car = carDao.getById(customer.getRentedCarId());
                    Company company = companyDao.getById(car.getCompanyId());
                    System.out.println("Your rented car:\n"
                            + car.getName() + "\nCompany:\n" + company.getName());
                }
                customerCarMenu(customerId);
                break;
            default:
                System.out.println("Bad choice");
                customerCarMenu(customerId);
        }
    }

    private void availableCompanies(int customerId) {
        List<Company> companyList = companyDao.getAll();
        if (companyList.isEmpty()) {
            System.out.println("The company list is empty!");
            customerCarMenu(customerId);
        } else {
            System.out.println("Choose the company:");
            var index = 1;
            for (Company company : companyList) {
                System.out.println(index + ". " + company.getName());
                index++;
            }
            System.out.println("0. Back");
            choice = scanner.nextInt();
            if (choice == 0) {
                customerCarMenu(customerId);
            } else {
                Company company = companyList.get(choice - 1);
                buyCar(customerId, company);
            }
        }
    }

    private void buyCar(int customerId, Company company) {
        carDao.setCompanyId(company.getId());
        List<Car> carList = carDao.getAll();
        List<Customer> customers = customerDao.getAll();
        var rentedListId = customers.stream().map(Customer::getRentedCarId)
                .filter(it -> it != -1).collect(Collectors.toList());
        if (carList.isEmpty()) {
            System.out.println("No available cars in the '"+company.getName()+"' company");
            availableCompanies(customerId);
        } else {
            var index = 1;
            for (Car car : carList) {
                if (!rentedListId.contains(car.getId())) {
                    System.out.println(index + ". " + car.getName());
                    index++;
                }
            }
            choice = scanner.nextInt();
            if (choice == 0) {
                availableCompanies(customerId);
            } else {
                Car car = carList.get(choice - 1);
                Customer customer = customerDao.getById(customerId);
                customer.setRentedCarId(car.getId());
                customerDao.update(customer, null);
                System.out.printf("You rented '%s'\n", car.getName());
                customerCarMenu(customerId);
            }
        }
    }

    private void companyMenu() {
        System.out.println("1. Company list\n" +
                "2. Create a company\n" +
                "0. Back");
        choice = scanner.nextInt();
        switch (choice) {
            case 0:
                managerMenu();
                break;
            case 1:
                printCompanyList();
                break;
            case 2:
                createCompany();
                break;
            default:
                System.out.println("Bad choice, Try again:");
                companyMenu();
        }
    }

    private void printCompanyList() {
        List<Company> companyList = companyDao.getAll();
        if (companyList.isEmpty()) {
            System.out.println("The company list is empty!\n");
            companyMenu();
        } else {
            System.out.println("Choose the company:");
            var index = 1;
            for (Company company : companyList) {
                System.out.println(index + ". " + company.getName());
                index++;
            }
            System.out.println("0. Back");
            choice = scanner.nextInt();
            if (choice < 0 || choice > companyList.size()) {
                System.out.println("choose company from list");
            } else {
                if (choice == 0) {
                    companyMenu();
                } else {
                    Company company = companyList.get(choice - 1);
                    System.out.printf("'%s' company\n", company.getName());
                    carMenu(company.getId());
                }
            }
        }
    }


    private void carMenu(int companyId) {
        System.out.println("1. Car list\n" +
                "2. Create a car\n" +
                "0. Back");
        choice = scanner.nextInt();
        switch (choice) {
            case 0:
                companyMenu();
                break;
            case 1:
                printCarList(companyId);
                break;
            case 2:
                createCar(companyId);
                break;
            default:
                System.out.println("Bad choice, Try again:");
                companyMenu();
        }
    }

    private void printCarList(int companyId) {
        carDao.setCompanyId(companyId);
        List<Car> carList = carDao.getAll();
        if (carList.isEmpty()) {
            System.out.println("The car list is empty!");
            carMenu(companyId);
        } else {
            var index = 1;
            for (Car car : carList) {
                System.out.println(index + ". " + car.getName());
                index++;
            }
            carMenu(companyId);
        }
    }

    private void createCustomer() {
        System.out.println("Enter the customer name:");
        scanner.nextLine();
        String comName = scanner.nextLine();
        customerDao.save(new Customer(comName));
        System.out.println("The customer was created!\n");
        managerMenu();
    }

    private void createCompany() {
        System.out.println("Enter the company name:\n");
        scanner.nextLine();
        String comName = scanner.nextLine();
        companyDao.save(new Company(0, comName));
        System.out.println("The company was created!\n");
        companyMenu();
    }

    private void createCar(int companyId) {
        System.out.println("Enter the car name:\n");
        scanner.nextLine();
        String carName = scanner.nextLine();
        carDao.save(new Car(0, carName, companyId));
        System.out.println("The car was created!\n");
        carMenu(companyId);
    }
}