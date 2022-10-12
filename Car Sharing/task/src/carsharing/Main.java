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
        try {
            Class.forName(JDBC_DRIVER);
            try(Connection conn = DriverManager.getConnection(DB_URL + "/" + dbName)) {
                conn.setAutoCommit(true);
                try(Statement stmt = conn.createStatement()) {
                    String sql = "CREATE TABLE   COMPANY " +
                            "(id INTEGER not NULL, " +
                            " name VARCHAR(255))";
                    stmt.executeUpdate(sql);
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

    static void m1(String[] args) {
        String dbName = Optional.ofNullable(args[1]).orElse("carS");
        Connection conn = null;
        Statement stmt = null;

        try {
            Class.forName(JDBC_DRIVER);

            System.out.println("Connecting to database...");
            conn = DriverManager.getConnection(DB_URL + "/" + dbName);
            conn.setAutoCommit(true);

            System.out.println("Creating table in given database...");
            stmt = conn.createStatement();
            String sql = "CREATE TABLE   COMPANY " +
                    "(id INTEGER not NULL, " +
                    " name VARCHAR(255))";
            stmt.executeUpdate(sql);
            System.out.println("Created table in given database...");

            // STEP 4: Clean-up environment
            stmt.close();
            conn.close();
        } catch (Exception se) {
            //Handle errors for JDBC
            se.printStackTrace();
        }//Handle errors for Class.forName
        finally {
            //finally block used to close resources
            try {
                if (stmt != null) stmt.close();
            } catch (SQLException se2) {
                se2.printStackTrace();
            } // nothing we can do
            try {
                if (conn != null) conn.close();
            } catch (SQLException se) {
                se.printStackTrace();
            } //end finally try
        } //end try
        System.out.println("Goodbye!");
    }


    static void ex1(String[] args) {
        String dbName = Optional.ofNullable(args[1]).orElse("carS");
        try {
            Class.forName(JDBC_DRIVER);
            try (Connection conn = DriverManager.getConnection(DB_URL + "/" + dbName)) {
                conn.setAutoCommit(true);
                CompanyDaoImpl companyDao = new CompanyDaoImpl(conn);
                start(companyDao);

            } catch (SQLException e) {
                e.printStackTrace();
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