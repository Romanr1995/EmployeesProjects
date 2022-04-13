package ru.consulting;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import ru.consulting.entitity.Employee;
import ru.consulting.entitity.security.Role;
import ru.consulting.repositories.EmployeeRepo;

import java.time.LocalDate;
import java.util.Set;

@Component
public class SecuriryInit implements CommandLineRunner {
    private EmployeeRepo employeeRepo;

    @Autowired
    public SecuriryInit(EmployeeRepo employeeRepo) {
        this.employeeRepo = employeeRepo;
    }

    @Override
    public void run(String... args) throws Exception {
        String admin = "ADMIN";
        if (employeeRepo.findByNameIgnoreCaseAndSurnameIgnoreCase(admin, admin).isEmpty()) {
            Employee employee = new Employee();
            employee.setName(admin);
            employee.setSurname(admin);
            employee.setDateOfEmployment(LocalDate.of(1970, 1, 1));
            employee.setEmail("admin.admin@yandex.ru");
            employee.setPhone("89777777777");
            employee.setRole(Set.of(Role.USER, Role.ADMIN));
            employeeRepo.save(employee);
        }
    }
}
