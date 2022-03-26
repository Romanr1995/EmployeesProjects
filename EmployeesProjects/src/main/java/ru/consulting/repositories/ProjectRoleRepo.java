package ru.consulting.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.consulting.entitity.ProjectRole;

import java.util.Optional;

@Repository
public interface ProjectRoleRepo extends CrudRepository<ProjectRole, Long> {

    Optional<ProjectRole> findByTitleIgnoreCase(String title);
}
