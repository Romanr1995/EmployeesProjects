package ru.consulting.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.consulting.entitity.Client;

import java.util.Optional;

@Repository
public interface ClientRepo extends CrudRepository<Client, Long> {

    Client findByTitleIgnoreCase(String title);

    Optional<Client> findByPhone(String phone);

    Optional<Client> findByEmailIgnoreCase(String email);

}
