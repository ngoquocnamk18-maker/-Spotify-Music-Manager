package dao;

import java.util.List;
import java.util.Optional;

public interface BaseDAO<T> {
    Optional<T> findById(String id);
    List<T> findAll();
    List<T> findAll(int page, int pageSize);
    boolean save(T entity);
    boolean update(T entity);
    boolean delete(String id);
}