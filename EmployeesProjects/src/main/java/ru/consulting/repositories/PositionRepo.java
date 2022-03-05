package ru.consulting.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.consulting.entitity.Position;

import java.util.List;
import java.util.Optional;

@Repository
public interface PositionRepo extends CrudRepository<Position, Long> {

    Position findByTitleIgnoreCase(String title);

    Position findByIdOrTitle(Long id, String title);

    Optional<Position> findByTitleContainingIgnoreCase(String title);

    Position findByTitleEndingWithIgnoreCase(String title);

    @Query("Select p From Position p Order By p.id")
    List<Position> getOrderById();

    @Query("Select p From Position p Order By p.id Desc")
    List<Position> getOrderByIdDesc();

    @Query("Select p From Position p Order By p.title")
    List<Position> getOrderByTitle();
}
