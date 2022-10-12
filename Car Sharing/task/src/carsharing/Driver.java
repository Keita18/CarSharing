package carsharing;

import carsharing.dao.CarDaoImpl;
import carsharing.dao.CompanyDaoImpl;
import carsharing.model.Company;

import java.util.List;
import java.util.Scanner;

public class Driver {
    // JDBC driver name and database URL
    static final String JDBC_DRIVER = "org.h2.Driver";
    static final String DB_URL = "jdbc:h2:./src/carsharing/db";
    private final Scanner scanner = new Scanner(System.in);

    private CompanyDaoImpl companyDao;
    private CarDaoImpl carDao;
    private List<Company> companyList;

    public Driver(String dbName) {

    }

    private void managerMenu() {
        boolean stop = false;
        while (!stop) {
            System.out.println("1. Log in as a manager\n0. Exit");
            int choice = scanner.nextInt();
            switch (choice) {
                case 0:
                    stop = true;
                    break;
                case 1:
                    boolean back;
                    do {
                        back = companyMenu();
                    } while (!back);
                    break;
                default:
                    System.out.println("Bad choice");
            }
        }
    }

    private boolean companyMenu() {
        System.out.println("1. Company list\n" +
                "2. Create a company\n" +
                "0. Back");
        int choice = scanner.nextInt();

        switch (choice) {
            case 0:
                return true;
            case 1:
                companyList = companyDao.getAll();
                if (companyList.isEmpty()) {
                    System.out.println("The company list is empty!\n");
                } else {
                    System.out.println("Choose the company:");
                    companyList.forEach(System.out::println);
                    System.out.println("0. Back");
                    boolean back;
                    do {
                        back = carMenu();
                    } while (!back);
                }
                return false;
            case 2:
                System.out.println("Enter the company name:\n");
                scanner.nextLine();
                String comName = scanner.nextLine();
                companyDao.save(new Company(0, comName));
                System.out.println("The company was created!\n");
                return false;
            default:
                System.out.println("bad choice 2");
                return false;
        }
    }

    private boolean carMenu() {
        int choice = scanner.nextInt();
        if (choice == 0) return true;
        else {
            assert (choice >= 0 && choice < companyList.size());
            Company company = companyList.get(choice);
            System.out.printf("'%s' company\n", company.getName());
            System.out.println("1. Car list\n" +
                    "2. Create a car\n" +
                    "0. Back");
            int choice2 = scanner.nextInt();
            boolean back = false;

        }
    }
}
