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
import ru.consulting.entitity.security.Role;
import ru.consulting.repositories.DepartmentRepo;
import ru.consulting.repositories.EmployeeRepo;
import ru.consulting.repositories.PositionRepo;
import ru.consulting.repositories.specifications.EmployeeSpecifications;

import java.math.BigDecimal;
import java.security.Principal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
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

    public ResponseEntity<?> delete(Long id, Principal principal) {

        try {
            if (canDo(id, principal)) {
                employeeRepo.deleteById(id);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
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

    public void updateDepartment(String title, String name, String surname, Principal principal) {
        Department department = departmentRepo.findByTitleEqualsIgnoreCase(title).orElseThrow();
        Employee employee = employeeRepo
                .findByNameIgnoreCaseAndSurnameIgnoreCase(name, surname).orElseThrow();
        if (employee.getDepartment() != null) {
            try {
                if (canDo(employee.getId(), principal)) {
                    employee.setDepartment(department);
                    employeeRepo.save(employee);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            employee.setDepartment(department);
            employeeRepo.save(employee);
        }
    }

    public void setSalary(Long id, BigDecimal salary, Principal principal) {

        final Employee employee = employeeRepo.findById(id).get();
        if (employee.getDepartment() == null) {
            throw new RuntimeException("Изменить зарплату может только руководитель департамента," +
                    "в котором работает сотрудник.Необходимо сначала установить департамент");
        }

        Employee empPrincipal = employeeRepo.findByEmail(principal.getName()).get();
        if (isEmployeeUnderManager(empPrincipal, employee)) {
            employee.setSalary(salary);
            employeeRepo.save(employee);
        } else {
            throw new RuntimeException("Вы не можете изменить зарплату сотруднику, так как " +
                    "он работает не в Вашем отделе.");
        }

    }

//    public void removeList(List<Long> id) {
//        employeeRepo.deleteAllById(id::iterator);
//    }
//
//    public void removesMap(Map<String, String> namesAndEmails) {
//        for (Map.Entry<String, String> stringStringEntry : namesAndEmails.entrySet()) {
//            Employee byNameAndEmail = employeeRepo.findByNameAndEmail(stringStringEntry.getKey(),
//                    stringStringEntry.getValue());
//            if (byNameAndEmail != null) {
//                employeeRepo.delete(byNameAndEmail);
//            }
//        }
//    }

    public void addPosition(String title, String phone, String email, Principal principal) {
        Position position = positionRepo.findByTitleIgnoreCase(title);
        if (Objects.isNull(position)) {
            throw new RuntimeException("Position с title: " + title + " не существует.");
        }

        Employee employee = employeeRepo.findByPhoneOrEmailIgnoreCase(phone, email).orElseThrow(() ->
                new RuntimeException("Employee с phone: " + phone + " и email: " + email + " не найден."));
        try {
            if (canDo(employee.getId(), principal)) {
                employee.setPosition(position);
                employeeRepo.save(employee);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void editPassword(String newPassword, Principal principal) {
        String userEmail = principal.getName();
        Employee employee = employeeRepo.findByEmail(userEmail).orElseThrow();
        employee.setPassword(newPassword);
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

    public boolean canDo(Long id, Principal principal) throws Exception {
        String emailPrincipal = principal.getName();
        Employee empPrincipal = employeeRepo.findByEmail(emailPrincipal).orElseThrow();
        Employee employee = employeeRepo.findById(id).orElseThrow();

        if (empPrincipal.getRole().contains(Role.ADMIN)) {
            return true;
        } else {
            if (isEmployeeUnderManager(empPrincipal, employee)) {
                return true;
            } else {
                throw new Exception("У вас нет прав для действий с данным сотрудником." +
                        "Сотрудик работает не в Вашем отделе.");
            }
        }
    }

    public static boolean isEmployeeUnderManager(Employee manager, Employee employee) {
        Department underManagementDepartment = manager.getUnderManagement();
        List<Employee> employeesOfUnderManagementDepartment = underManagementDepartment.getEmployeesOfDepartment();

        if (employeesOfUnderManagementDepartment.contains(employee)) {
            return true;
        } else {
            List<Department> subDepartments = underManagementDepartment.getSubDepartments();
            for (Department subDepartment : subDepartments) {
                if (subDepartment.getEmployeesOfDepartment().contains(employee)) {
                    return true;
                }
            }
        }
        return false;
    }

    public List<Department> recursionDepartment(Department department) {
        List<Department> departments1 = department.getSubDepartments();
        if (departments1.size() > 0) {
            for (Department department1 : departments1) {
                departments1.addAll(recursionDepartment(department1));
            }
        }
        return departments1;
    }
}
