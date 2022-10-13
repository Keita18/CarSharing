package carsharing;

import carsharing.dao.CarDaoImpl;
import carsharing.dao.CompanyDaoImpl;
import carsharing.model.Car;
import carsharing.model.Company;

import java.util.List;
import java.util.Scanner;

public class Driver {
    private final Scanner scanner = new Scanner(System.in);
    private int choice;

    private final CompanyDaoImpl companyDao;
    private final CarDaoImpl carDao;

    public Driver(CompanyDaoImpl companyDao, CarDaoImpl carDao) {
        this.companyDao = companyDao;
        this.carDao = carDao;
    }

    public void start() {
        managerMenu();
    }

    private void managerMenu() {
        System.out.println("1. Log in as a manager\n0. Exit");
        choice = scanner.nextInt();
        switch (choice) {
            case 0:
                break;
            case 1:
                companyMenu();
                break;
            default:
                System.out.println("Bad choice");
                managerMenu();
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
        } else {
            System.out.println("Choose the company:");
            companyList.forEach(System.out::println);
            choice = scanner.nextInt();
            if (choice < 0 || choice > companyList.size()) {
                System.out.println("choose company from list");
            } else  {
                if (choice == 0) {
                    companyMenu();
                } else {
                    Company company = companyList.get(choice - 1);
                    System.out.printf("'%s' company", company.getName());
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
        } else {
            carList.forEach(System.out::println);
            carMenu(companyId);
        }
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
        companyMenu();
    }
}
