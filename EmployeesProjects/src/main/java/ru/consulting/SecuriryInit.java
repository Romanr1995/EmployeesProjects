package ru.consulting;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import ru.consulting.entitity.Role;
import ru.consulting.entitity.User;
import ru.consulting.repositories.RoleRepo;
import ru.consulting.repositories.UserRepository;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

//@Component
public class SecuriryInit implements CommandLineRunner {

    private UserRepository userRepository;
    private RoleRepo roleRepo;

    @Autowired
    public SecuriryInit(UserRepository userRepository, RoleRepo roleRepo) {
        this.userRepository = userRepository;
        this.roleRepo = roleRepo;
    }

    @Override
    public void run(String... args) throws Exception {
//        roleRepo.save(new Role("ADMIN"));
//        roleRepo.save(new Role("USER"));
//        roleRepo.save(new Role("MAINUSER"));

//        Role roleAdmin = roleRepo.findByName("ADMIN");
//        Role roleUser = roleRepo.findByName("USER");
//        Role roleMain = roleRepo.findByName("MAINUSER");
//
//        User admin = new User("admin", "100", "adm@yandex.ru",
//                Arrays.asList(roleAdmin, roleUser, roleMain));
//
//        User user = new User("user", "200", "user@mail.ru", Collections.singletonList(roleUser));
//
//        User main = new User("main", "abc", "main@yandex.ru", List.of(roleUser, roleMain));
//
//        userRepository.saveAll(() -> List.of(admin, user, main).iterator());


    }

}
