package carsharing.dao;

import carsharing.model.Car;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CarDaoImpl implements Dao<Car> {
    private final Connection conn;
    private int companyId;

    public CarDaoImpl(Connection conn) {
        this.conn = conn;
    }

    public void setCompanyId(int companyId) {
        this.companyId = companyId;
    }

    @Override
    public List<Car> getAll() {
        String sql = "SELECT * FROM CAR WHERE COMPANY_ID = " + companyId;
        List<Car> carList = new ArrayList<>();
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int id = rs.getInt(1);
                String name = rs.getString(2);
                int companyId = rs.getInt(3);
                carList.add(new Car(id, name, companyId));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return carList;
    }

    public Car getById(int id) {
        String sql = "SELECT * FROM CAR WHERE ID=" +id;
        Car car = null;
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                int carId = rs.getInt(1);
                String name = rs.getString(2);
                int companyId = rs.getInt(3);
                car = new Car(carId, name, companyId);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return car;
    }

    @Override
    public void save(Car car) {
        String sql = "INSERT INTO CAR (NAME, COMPANY_ID) VALUES(?,?)";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, car.getName());
            stmt.setInt(2, car.getCompanyId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(Car car, String[] params) {

    }

    @Override
    public void delete(Car car) {

    }
}
