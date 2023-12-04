package repository;

import java.util.List;

public interface CrudOperations<T> {
    List<T> findAll();
    T create();
    T updateById(int id);

}
