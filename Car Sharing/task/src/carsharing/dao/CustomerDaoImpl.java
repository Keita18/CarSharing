package carsharing.dao;

import carsharing.model.Customer;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CustomerDaoImpl implements Dao<Customer> {
    private final Connection conn;

    public CustomerDaoImpl(Connection conn) {
        this.conn = conn;
    }

    public Customer getById(int id) {
        String sql = "SELECT * FROM CUSTOMER WHERE ID=" +id;
        Customer customer = null;
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                int customerId = rs.getInt(1);
                String name = rs.getString(2);
                int rentedCarId = Optional.of(rs.getInt(3)).orElse(-1);
                if (rentedCarId == 0) rentedCarId = -1;
                customer = new Customer(customerId, name, rentedCarId);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return customer;
    }

    @Override
    public List<Customer> getAll() {
        String sql = "SELECT * FROM CUSTOMER ORDER BY id ";
        List<Customer> customers = new ArrayList<>();
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                int id = rs.getInt(1);
                String name = rs.getString(2);
                int rentedCarId = Optional.of(rs.getInt(3)).orElse(-1);
                customers.add(new Customer(id, name, rentedCarId));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return customers;
    }

    @Override
    public void save(Customer customer) {
        String sql = "INSERT INTO CUSTOMER (NAME) VALUES(?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, customer.getName());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(Customer customer, String[] params) {
        String sql = "UPDATE CUSTOMER SET RENTED_CAR_ID=? WHERE ID=?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            if (customer.getRentedCarId() == -1)
                stmt.setNull(1, Types.INTEGER);
            else
                stmt.setInt(1, customer.getRentedCarId());

            stmt.setInt(2, customer.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(Customer customer) {

    }
}
