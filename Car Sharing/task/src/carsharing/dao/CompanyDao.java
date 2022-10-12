package carsharing.dao;

import java.util.List;

public interface CompanyDao<T> {
    List<T> getAll();

    void save(T t);

    void update(T t, String[] params);

    void delete(T t);

}
