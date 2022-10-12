package carsharing;

import carsharing.dao.CompanyDaoImpl;
import carsharing.model.Company;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;
import java.util.Scanner;

public class Main {

    // JDBC driver name and database URL
    static final String JDBC_DRIVER = "org.h2.Driver";
    static final String DB_URL = "jdbc:h2:./src/carsharing/db";

    public static void main(String[] args) {
        String dbName = Optional.ofNullable(args[1]).orElse("carS");
        CompanyDaoImpl companyDao;

        String createCompany = "CREATE TABLE IF NOT EXISTS COMPANY\n" +
                "(\n" +
                "    ID   INT          NOT NULL AUTO_INCREMENT PRIMARY KEY,\n" +
                "    NAME VARCHAR(255) NOT NULL UNIQUE\n" +
                ");";
        String createCar = "CREATE TABLE IF NOT EXISTS CAR\n" +
                "(\n" +
                "    ID   INT          NOT NULL AUTO_INCREMENT PRIMARY KEY,\n" +
                "    NAME VARCHAR(255) NOT NULL UNIQUE,\n" +
                "    K_ID INT NOT NULL,\n" +
                "    CONSTRAINT fk_company FOREIGN KEY (ID) REFERENCES COMPANY(ID)\n" +
                ");";

        try {
            Class.forName(JDBC_DRIVER);
            try(Connection conn = DriverManager.getConnection(DB_URL + "/" + dbName)) {
                conn.setAutoCommit(true);
                try(Statement stmt = conn.createStatement()) {
                    String sql = "CREATE TABLE IF NOT EXISTS COMPANY " +
                            "(id INTEGER NOT NULL, " +
                            " name VARCHAR(255))";
                    stmt.executeUpdate(sql);

                    String constraints = "ALTER TABLE COMPANY\n" +
                            "ADD PRIMARY KEY (ID);\n" +
                            "ALTER TABLE COMPANY\n" +
                            "ALTER COLUMN ID INTEGER AUTO_INCREMENT;\n" +
                            "ALTER TABLE COMPANY\n" +
                            "ADD UNIQUE (NAME);\n" +
                            "ALTER TABLE COMPANY\n" +
                            "ALTER COLUMN NAME SET NOT NULL;";
                    stmt.execute(constraints);
                } catch (SQLException e) {
                    System.out.println("Error in stmt " + e);
                }

                companyDao = new CompanyDaoImpl(conn);
                start(companyDao);

            } catch (SQLException e) {
                System.out.println("Error in conn " + e);
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void start(CompanyDaoImpl companyDao) {
        boolean exit = false;
        boolean back = false;
        Scanner sc = new Scanner(System.in);
        int choice;
        while (!exit) {
            System.out.println("1. Log in as a manager\n0. Exit");
            choice = sc.nextInt();
            switch (choice) {
                case 0:
                    exit = true;
                    break;
                case 1:
                    while (!back) {
                        System.out.println("1. Company list\n" +
                                "2. Create a company\n" +
                                "0. Back");
                        choice = sc.nextInt();
                        switch (choice) {
                            case 1:
                                var list = companyDao.getAll();
                                if (list.isEmpty()) {
                                    System.out.println("The company list is empty!\n");
                                } else {
                                    list.forEach(System.out::println);
                                }
                                break;
                            case 2:
                                System.out.println("Enter the company name:\n");
                                sc.nextLine();
                                String comName = sc.nextLine();
                                companyDao.save(new Company(0, comName));
                                System.out.println("The company was created!\n");
                                break;
                            case 0:
                                back = true;
                                break;
                        }
                    }
                    break;
            }
        }
    }
}