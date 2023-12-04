package repository;

import java.util.List;

public interface CrudOperations<T> {
    List<T> findAll();
    void create(T t);
    void updateById(T t, int id);
}
