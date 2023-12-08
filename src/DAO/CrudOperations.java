package DAO;

import java.util.List;

public interface CrudOperations<T> {
    List<T> findAll();
    T findById(int id);
    T save(T toSave);
}
