package ru.consulting.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.consulting.entitity.TestEntity;

@Repository
public interface TestRepo extends CrudRepository<TestEntity, Long> {
}
