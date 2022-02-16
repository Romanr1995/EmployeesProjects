package ru.consulting.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.consulting.entitity.Client;

@Repository
public interface ClientRepo extends CrudRepository<Client, Long> {
}
