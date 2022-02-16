package ru.consulting.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.consulting.entitity.Department;
import ru.consulting.entitity.Employee;
import ru.consulting.entitity.Position;
import ru.consulting.repositories.*;

@Service
public class ServiceClass {
    private ClientRepo clientRepo;
    private DepartmentRepo departmentRepo;
    private EmployeeOnProjectRepo employeeOnProjectRepo;
    private EmployeeRepo employeeRepo;
    private PositionRepo positionRepo;
    private ProjectRepo projectRepo;
    private ProjectRoleRepo projectRoleRepo;

    @Autowired
    public ServiceClass(ClientRepo clientRepo, DepartmentRepo departmentRepo,
                        EmployeeOnProjectRepo employeeOnProjectRepo, EmployeeRepo employeeRepo,
                        PositionRepo positionRepo, ProjectRepo projectRepo, ProjectRoleRepo projectRoleRepo,
                        TestRepo testRepo) {
        this.clientRepo = clientRepo;
        this.departmentRepo = departmentRepo;
        this.employeeOnProjectRepo = employeeOnProjectRepo;
        this.employeeRepo = employeeRepo;
        this.positionRepo = positionRepo;
        this.projectRepo = projectRepo;
        this.projectRoleRepo = projectRoleRepo;
    }

    public void addDepartment(Department department) {
        departmentRepo.save(department);
    }

    public void removeDepartment(Department department) {
        departmentRepo.delete(department);
    }

    public void addPosition(Position position) {
        positionRepo.save(position);
    }

    public void removePosition(Position position) {
        positionRepo.delete(position);
    }

    public void addEmployee(Employee employee) {
        employeeRepo.save(employee);
    }

    public void removeEmployee(Employee employee) {
        employeeRepo.delete(employee);
    }

    public Employee getEmployeeByNameAndSurname(String name, String surname) {
        Employee employee = employeeRepo.findByNameIgnoreCaseAndSurnameIgnoreCase(name, surname);
        if (employee == null) {
            throw new RuntimeException("Employee с name: " + name + " и surname: " + surname + " не найден.");
        } else {
            return employee;
        }
    }

    public Department getDepartmentByTitle(String title) {
        Department departmentRepoByTitle = departmentRepo.findByTitleEqualsIgnoreCase(title);
        if (departmentRepoByTitle == null) {
            throw new RuntimeException("Department c title: " + title + " не найден.");
        } else {
            return departmentRepoByTitle;
        }
    }

    public Position getPositionByTitle(String title) {
        Position position = positionRepo.findByTitleIgnoreCase(title);
        if (position == null) {
            throw new RuntimeException("Position c title: " + title + " не найден.");
        } else {
            return position;
        }
    }

    public void setDepartmentByEmployee(Employee employee, Department department) {
        employee.setDepartment(department);
        employeeRepo.save(employee);
    }

    public void setPositionByEmployee(Employee employee, Position position) {
        employee.setPosition(position);
        employeeRepo.save(employee);
    }

    public void setDepartmentHeadByDepartment(Department department, Employee headDepartment) {
        department.setDepartmentHead(headDepartment);
        departmentRepo.save(department);
    }

    public void setHigherDepartmentByDepartment(Department department, Department higher) {
        department.setHigherDepartment(higher);
        departmentRepo.save(department);
    }
}
