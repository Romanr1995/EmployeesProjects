package ru.consulting.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.consulting.entitity.Department;

import java.util.List;
import java.util.Optional;

@Repository
public interface DepartmentRepo extends CrudRepository<Department, Long> {

    Department findByTitle(String title);

    Optional<Department> findByTitleEqualsIgnoreCase(String title);

    Optional<Department> findByIdOrTitleIgnoreCase(Long id, String title);

    @Query(value = "Select dep From Department dep Order By dep.id")
    List<Department> getAllOrderById();

    Department findByTitleLike(String like);

    List<Department> findByTitleNotLike(String like);

    List<Department> findByTitleContaining(String title);
}
