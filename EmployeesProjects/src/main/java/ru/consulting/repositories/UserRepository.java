package ru.consulting.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.consulting.entitity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findOneByEmail(String username);
}
