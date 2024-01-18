package DAO;

import java.sql.SQLException;
import java.util.List;

public interface CrudOperations<T> {
    List<T> findAll() throws SQLException;
    T findById(int id) throws SQLException;
    T save(T toSave) throws SQLException;
}
