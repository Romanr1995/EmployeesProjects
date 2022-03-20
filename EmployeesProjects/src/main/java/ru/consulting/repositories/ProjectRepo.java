package ru.consulting.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.consulting.entitity.Project;

import java.util.Optional;

@Repository
public interface ProjectRepo extends CrudRepository<Project, Long> {

    Optional<Project> findByTitleIgnoreCase(String title);
}
