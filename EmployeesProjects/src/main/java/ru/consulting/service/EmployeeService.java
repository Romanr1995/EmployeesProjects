package ru.consulting.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.consulting.dto.EmployeeDto;
import ru.consulting.entitity.Department;
import ru.consulting.entitity.Employee;
import ru.consulting.entitity.Position;
import ru.consulting.repositories.DepartmentRepo;
import ru.consulting.repositories.EmployeeRepo;
import ru.consulting.repositories.PositionRepo;
import ru.consulting.repositories.specifications.EmployeeSpecifications;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
public class EmployeeService {

    private EmployeeRepo employeeRepo;
    private DepartmentRepo departmentRepo;
    private PositionRepo positionRepo;

    @Autowired
    public EmployeeService(EmployeeRepo employeeRepo, DepartmentRepo departmentRepo, PositionRepo position) {
        this.employeeRepo = employeeRepo;
        this.departmentRepo = departmentRepo;
        this.positionRepo = position;
    }

    public List<EmployeeDto> getAllEmployeeDtoWithFiltering(Integer page,
                                                            BigDecimal minSalary,
                                                            BigDecimal maxSalary,
                                                            LocalDate dateAfter,
                                                            LocalDate dateBefore,
                                                            String sort,
                                                            Boolean direction,
                                                            String department,
                                                            String surname) {
        List<EmployeeDto> employeesDto = new ArrayList<>();

        if (page == null) {
            page = 0;
        }

        Specification<Employee> specification = Specification.where(null);
        if (minSalary != null) {
            specification = specification.and(EmployeeSpecifications.minSalary(minSalary));
        }
        if (maxSalary != null) {
            specification = specification.and(EmployeeSpecifications.maxSalary(maxSalary));
        }
        if (dateAfter != null) {
            specification = specification.and(EmployeeSpecifications.dateAfter(dateAfter));
        }
        if (dateBefore != null) {
            specification = specification.and(EmployeeSpecifications.dateBefore(dateBefore));
        }
        if (department != null) {
            specification = specification.and(EmployeeSpecifications.departmentTitle(department));
        }
        if (surname != null) {
            specification = specification.and(EmployeeSpecifications.departmentHeadSurname(surname));
        }
        int sizePage = 2;
        Sort.Direction dir = Sort.Direction.ASC;
        if (direction != null && !direction) {
            dir = Sort.Direction.DESC;
        }
        if (sort == null) {
            sort = "id";
        }
        employeeRepo.findAll(specification, PageRequest.of(page, sizePage, Sort.by(dir, sort))).getContent()
                .forEach(employee -> employeesDto.add(convertEmployeeToEmployeeDto(employee)));
        return employeesDto;
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

    public ResponseEntity<?> delete(Long id) {
        try {
            employeeRepo.deleteById(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    public void update(EmployeeDto employeeDto) {
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

    public void updateDepartment(String title, String name, String surname) {
        Department department = departmentRepo.findByTitleEqualsIgnoreCase(title).orElseThrow();
        Employee employee = employeeRepo
                .findByNameIgnoreCaseAndSurnameIgnoreCase(name, surname).orElseThrow();
        employee.setDepartment(department);
        employeeRepo.save(employee);
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

    public void addPosition(String title, String phone, String email) {
        Position position = positionRepo.findByTitleIgnoreCase(title);
        if (Objects.isNull(position)) {
            throw new RuntimeException("Position с title: " + title + " не существует.");
        }

        Employee employee = employeeRepo.findByPhoneOrEmailIgnoreCase(phone, email).orElseThrow(() ->
                new RuntimeException("Employee с phone: " + phone + " и email: " + email + " не найден."));
        employee.setPosition(position);

        employeeRepo.save(employee);
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
