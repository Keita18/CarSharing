package carsharing;

import carsharing.dao.CarDaoImpl;
import carsharing.dao.CompanyDaoImpl;
import carsharing.dao.CustomerDaoImpl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;

public class Main {

    // JDBC driver name and database URL
    static final String JDBC_DRIVER = "org.h2.Driver";
    static final String DB_URL = "jdbc:h2:./src/carsharing/db";

    static final String createCompany = "CREATE TABLE IF NOT EXISTS COMPANY\n" +
            "(\n" +
            "    ID   INT          NOT NULL AUTO_INCREMENT PRIMARY KEY,\n" +
            "    NAME VARCHAR(255) NOT NULL UNIQUE\n" +
            ");";
    static final String createCar = "CREATE TABLE IF NOT EXISTS CAR\n" +
            "(\n" +
            "    ID   INT          NOT NULL AUTO_INCREMENT PRIMARY KEY,\n" +
            "    NAME VARCHAR(255) NOT NULL UNIQUE,\n" +
            "    COMPANY_ID INT NOT NULL,\n" +
            "    CONSTRAINT fk_company FOREIGN KEY (COMPANY_ID) REFERENCES COMPANY(ID)\n" +
            ");";
    static final String createCustomer = "CREATE TABLE IF NOT EXISTS CUSTOMER\n" +
            "(\n" +
            "    ID            INT          NOT NULL AUTO_INCREMENT PRIMARY KEY,\n" +
            "    NAME          VARCHAR(255) NOT NULL UNIQUE,\n" +
            "    RENTED_CAR_ID INT,\n" +
            "    CONSTRAINT fk_car FOREIGN KEY (RENTED_CAR_ID) REFERENCES CAR (ID)\n" +
            ");";

    public static void main(String[] args) {
        String dbName = Optional.ofNullable(args[1]).orElse("carS");
        try {
            Class.forName(JDBC_DRIVER);
            try (Connection conn = DriverManager.getConnection(DB_URL + "/" + dbName)) {
                conn.setAutoCommit(true);
                try (Statement stmt = conn.createStatement()) {

                    stmt.executeUpdate(createCompany);
                    stmt.executeUpdate(createCar);
                    stmt.executeUpdate(createCustomer);
                } catch (SQLException e) {
                    System.out.println("Error in stmt " + e);
                }

                Driver driver = new Driver(
                        new CompanyDaoImpl(conn),
                        new CarDaoImpl(conn),
                        new CustomerDaoImpl(conn));
                driver.start();

            } catch (SQLException e) {
                System.out.println("Error in conn " + e);
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}