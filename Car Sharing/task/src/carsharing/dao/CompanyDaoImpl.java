package carsharing.dao;

import carsharing.model.Company;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CompanyDaoImpl implements Dao<Company> {
    private final Connection conn;

    public CompanyDaoImpl(Connection conn) {
        this.conn = conn;
    }

    @Override
    public List<Company> getAll() {
        String sql = "SELECT * FROM COMPANY ORDER BY id ";
        List<Company> companyList = new ArrayList<>();
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                int id = rs.getInt(1);
                String name = rs.getString(2);
                companyList.add(new Company(id, name));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return companyList;
    }

    public Company getById(int id) {
        String sql = "SELECT * FROM COMPANY WHERE ID=" +id;
        Company company = null;
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                int companyId = rs.getInt(1);
                String name = rs.getString(2);
                company = new Company(companyId, name);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return company;
    }

    @Override
    public void save(Company company) {
        String sql = "INSERT INTO COMPANY (NAME) VALUES(?)";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, company.getName());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(Company company) {

    }

    @Override
    public void delete(Company company) {

    }
}
