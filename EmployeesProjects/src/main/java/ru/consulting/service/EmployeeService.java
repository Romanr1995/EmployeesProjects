package ru.consulting.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.consulting.dto.EmployeeDto;
import ru.consulting.entitity.Employee;
import ru.consulting.repositories.EmployeeRepo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class EmployeeService {

    private EmployeeRepo employeeRepo;

    @Autowired
    public void setEmployeeRepo(EmployeeRepo employeeRepo) {
        this.employeeRepo = employeeRepo;
    }

    public List<EmployeeDto> getAllEmployeeDto() {
        List<EmployeeDto> employees = new ArrayList<>();
        employeeRepo.findAll().iterator().forEachRemaining(employee ->
                employees.add(convertEmployeeToEmployeeDto(employee)));
        return employees;
    }

    public EmployeeDto getEmployeeDtoById(Long id) {
        Employee employee = employeeRepo.findById(id).orElseThrow(() ->
                new RuntimeException("Employee с id: " + id + " не существует."));
        return convertEmployeeToEmployeeDto(employee);
    }

    public long getCountEmployees() {
        return employeeRepo.count();
    }

    public void save(EmployeeDto employeeDto) {
        employeeRepo.save(convertEmployeeDtoToEmployee(employeeDto));
    }

    public void delete(Long id) {
        employeeRepo.deleteById(id);
    }

    public void update(EmployeeDto employeeDto) {
        if (employeeDto.getId() == null) {
            throw new RuntimeException("Неверно задан id");
        }
        Employee employeeById = employeeRepo.findById(employeeDto.getId()).orElseThrow();
        if (employeeDto.getName() != null) {
            employeeById.setName(employeeDto.getName());
        }
        if (employeeDto.getSurname() != null) {
            employeeById.setSurname(employeeDto.getSurname());
        }
        if (employeeDto.getPatronymic() != null) {
            employeeById.setPatronymic(employeeDto.getPatronymic());
        }
        if (employeeDto.getSalary() != null) {
            employeeById.setSalary(employeeDto.getSalary());
        }
        if (employeeDto.getDateOfEmployment() != null) {
            employeeById.setDateOfEmployment(employeeDto.getDateOfEmployment());
        }
        if (employeeDto.getEmail() != null) {
            employeeById.setEmail(employeeDto.getEmail());
        }
        if (employeeDto.getPhone() != null) {
            employeeById.setPhone(employeeDto.getPhone());
        }

        employeeRepo.save(employeeById);
    }

    public void removeList(List<Long> id) {
        employeeRepo.deleteAllById(id::iterator);
    }

    public void removesMap(Map<String, String> namesAndEmails) {
        for (Map.Entry<String, String> stringStringEntry : namesAndEmails.entrySet()) {
            Employee byNameAndEmail = employeeRepo.findByNameAndEmail(stringStringEntry.getKey(),
                    stringStringEntry.getValue());
            if (byNameAndEmail != null) {
                employeeRepo.delete(byNameAndEmail);
            }
        }
    }

    public EmployeeDto convertEmployeeToEmployeeDto(Employee employee) {
        return new EmployeeDto(employee.getId(),
                employee.getName(), employee.getSurname(), employee.getPatronymic(),
                employee.getSalary(), employee.getDateOfEmployment(), employee.getEmail(), employee.getPhone());
    }

    public Employee convertEmployeeDtoToEmployee(EmployeeDto employeeDto) {
        return new Employee(employeeDto.getName(), employeeDto.getSurname(), employeeDto.getPatronymic(),
                employeeDto.getSalary(), employeeDto.getDateOfEmployment(), employeeDto.getEmail(), employeeDto.getPhone());
    }
}
