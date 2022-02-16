package ru.consulting.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.consulting.entitity.EmployeeOnProject;

@Repository
public interface EmployeeOnProjectRepo extends CrudRepository<EmployeeOnProject, Long> {
}
