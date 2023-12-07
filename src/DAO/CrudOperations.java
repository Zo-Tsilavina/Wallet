package DAO;

import java.util.List;

public interface CrudOperations<T> {
    List<T> findAll();
    T save(T toSave);
}
