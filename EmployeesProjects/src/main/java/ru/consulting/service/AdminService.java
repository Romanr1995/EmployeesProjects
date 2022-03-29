package ru.consulting.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.consulting.entitity.Employee;
import ru.consulting.entitity.security.Role;
import ru.consulting.entitity.security.Status;
import ru.consulting.repositories.EmployeeRepo;

@Service
public class AdminService {

    private EmployeeRepo employeeRepo;

    @Autowired
    public AdminService(EmployeeRepo employeeRepo) {
        this.employeeRepo = employeeRepo;
    }

    public void changeService(Long id, boolean isActive) {
        final Employee employee = employeeRepo.findById(id).orElseThrow();
        if (isActive) {
            employee.setStatus(Status.ACTIVE);
        } else {
            employee.setStatus(Status.BANNED);
        }

        employeeRepo.save(employee);
    }

    public Boolean editUserRole(Long id, Role role) {
        final Employee employee = employeeRepo.findById(id).orElseThrow();
        final boolean add = employee.getRole().add(role);
        employeeRepo.save(employee);
        return add;
    }

    public Boolean deleteUserRole(Long id, Role role) {
        final Employee employee = employeeRepo.findById(id).orElseThrow();
        final boolean remove = employee.getRole().remove(role);
        employeeRepo.save(employee);
        return remove;
    }
}
