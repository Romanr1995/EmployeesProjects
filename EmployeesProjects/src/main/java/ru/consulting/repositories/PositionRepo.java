package ru.consulting.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.consulting.entitity.Position;

@Repository
public interface PositionRepo extends CrudRepository<Position, Long> {

    Position findByTitleIgnoreCase(String title);

    Position findByIdOrTitle(Long id, String title);

    Position findByTitleStartingWithIgnoreCase(String title);
    Position findByTitleEndingWithIgnoreCase(String title);
}
