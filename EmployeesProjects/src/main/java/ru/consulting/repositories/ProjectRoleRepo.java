package ru.consulting.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.consulting.entitity.ProjectRole;

@Repository
public interface ProjectRoleRepo extends CrudRepository<ProjectRole, Long> {
}
