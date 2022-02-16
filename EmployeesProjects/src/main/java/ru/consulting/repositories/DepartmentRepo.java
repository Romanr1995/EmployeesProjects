package ru.consulting.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.consulting.entitity.Department;

import java.util.List;

@Repository
public interface DepartmentRepo extends CrudRepository<Department, Long> {

    Department findByTitle(String title);

    Department findByTitleEqualsIgnoreCase(String title);

    Department findByTitleLike(String like);

    List<Department> findByTitleNotLike(String like);

    List<Department> findByTitleContaining(String title);
}
