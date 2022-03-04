package ru.consulting.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.consulting.entitity.Department;

import java.util.List;

@Repository
public interface DepartmentRepo extends CrudRepository<Department, Long> {

    Department findByTitle(String title);

    Department findByTitleEqualsIgnoreCase(String title);

    @Query(value = "Select dep From Department dep Order By dep.id")
    List<Department> getAllOrderById();

    Department findByTitleLike(String like);

    List<Department> findByTitleNotLike(String like);

    List<Department> findByTitleContaining(String title);
}
