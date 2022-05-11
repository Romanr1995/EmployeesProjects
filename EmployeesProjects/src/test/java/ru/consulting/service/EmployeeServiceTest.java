package ru.consulting.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ru.consulting.dto.EmployeeDto;
import ru.consulting.entitity.Department;
import ru.consulting.entitity.Employee;
import ru.consulting.entitity.Position;
import ru.consulting.entitity.security.Role;
import ru.consulting.exception_handling.NoSuchEntityException;
import ru.consulting.repositories.DepartmentRepo;
import ru.consulting.repositories.EmployeeRepo;
import ru.consulting.repositories.PositionRepo;

import java.math.BigDecimal;
import java.security.Principal;
import java.time.LocalDate;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class EmployeeServiceTest {

    @Mock
    private EmployeeRepo employeeRepo;
    @Mock
    private DepartmentRepo departmentRepo;
    @Mock
    private PositionRepo positionRepo;
    private EmployeeService employeeService;
    @Mock
    private Principal principal;

    AutoCloseable mocks;

    @BeforeEach
    void setup() {
        mocks = MockitoAnnotations.openMocks(this);
        employeeService = new EmployeeService(employeeRepo, departmentRepo, positionRepo);
    }

    @Test
    void save_new_success() {
        EmployeeDto employeeDto = getCompileEmpDto();
        ArgumentCaptor<Employee> argCaptor = ArgumentCaptor.forClass(Employee.class);

        employeeService.save(employeeDto);

        verify(employeeRepo).save(argCaptor.capture());
        assertEquals("Employee1", argCaptor.getValue().getName());
    }

    @Test
    void update_success() {
        EmployeeDto employeeDto = getCompileEmpDto();
        employeeDto.setId(1L);
        Employee employee = getCompileEmp1();
        ArgumentCaptor<Employee> argCaptor = ArgumentCaptor.forClass(Employee.class);
        when(employeeRepo.findById(employeeDto.getId())).thenReturn(Optional.of(employee));

        employeeService.update(employeeDto);

        verify(employeeRepo).save(argCaptor.capture());
        assertEquals(employee.getEmail(), argCaptor.getValue().getEmail());
        assertEquals(LocalDate.of(2019, 11, 13), argCaptor.getValue().getDateOfEmployment());
        assertDoesNotThrow(() -> employeeService.update(employeeDto));
    }

    @Test
    void update_with_not_exist() {
        when(employeeRepo.findById(anyLong())).thenReturn(Optional.empty());

        Executable executable = () -> employeeService.update(new EmployeeDto());

        assertThrowsExactly(NoSuchEntityException.class, executable);
    }

    @Test
    void set_department_success() {
        Department department = new Department("Department1");
        Employee employee = getCompileEmp1();
        ArgumentCaptor<Employee> argCaptor = ArgumentCaptor.forClass(Employee.class);

        when(departmentRepo.findByTitleEqualsIgnoreCase(department.getTitle())).thenReturn(Optional.of(department));
        when(employeeRepo.findByNameIgnoreCaseAndSurnameIgnoreCase(employee.getName(), employee.getSurname()))
                .thenReturn(Optional.of(employee));

        employeeService.updateDepartment(department.getTitle(), employee.getName(), employee.getSurname(), principal);

        verify(employeeRepo).save(argCaptor.capture());
        assertEquals(department.getTitle(), argCaptor.getValue().getDepartment().getTitle());
    }

    @Test
    void update_exist_department_with_principal_admin() {
        Department department1 = new Department("Department1");
        Department department2 = new Department("Department2");
        Employee employee = getCompileEmp1();
        employee.setDepartment(department1);
        Employee empPrincipal = getCompileEmp2();
        employee.setDepartment(department1);
        empPrincipal.setRole(Set.of(Role.ADMIN));
        ArgumentCaptor<Employee> argCaptor = ArgumentCaptor.forClass(Employee.class);

        when(departmentRepo.findByTitleEqualsIgnoreCase(department2.getTitle())).thenReturn(Optional.of(department2));
        when(employeeRepo.findByNameIgnoreCaseAndSurnameIgnoreCase(employee.getName(), employee.getSurname()))
                .thenReturn(Optional.of(employee));
        when(principal.getName()).thenReturn(empPrincipal.getEmail());
        when(employeeRepo.findByEmail(principal.getName())).thenReturn(Optional.of(empPrincipal));
        when(employeeRepo.findById(employee.getId())).thenReturn(Optional.of(employee));

        employeeService.updateDepartment(department2.getTitle(), employee.getName(), employee.getSurname(), principal);

        verify(employeeRepo).save(argCaptor.capture());
        assertEquals(department2.getTitle(), argCaptor.getValue().getDepartment().getTitle());
    }

    @Test
    void insertPosition_success() {
        Position position = new Position("IT");
        Employee employee = getCompileEmp1();
        Employee empPrincipal = getCompileEmp2();
        empPrincipal.setRole(Set.of(Role.ADMIN));
        ArgumentCaptor<Employee> argCaptor = ArgumentCaptor.forClass(Employee.class);

        when(positionRepo.findByTitleIgnoreCase(position.getTitle())).thenReturn(position);
        when(employeeRepo.findByPhoneOrEmailIgnoreCase(employee.getPhone(), employee.getEmail()))
                .thenReturn(Optional.of(employee));
        when(principal.getName()).thenReturn(empPrincipal.getEmail());
        when(employeeRepo.findByEmail(principal.getName())).thenReturn(Optional.of(empPrincipal));
        when(employeeRepo.findById(employee.getId())).thenReturn(Optional.of(employee));

        employeeService.addPosition(position.getTitle(), employee.getPhone(),
                employee.getEmail(), principal);

        verify(employeeRepo).save(argCaptor.capture());
        assertEquals(position.getTitle(), argCaptor.getValue().getPosition().getTitle());
    }

    @Test
    void setSalary_access() {
        Department department1 = new Department("Department1");
        Employee employee = getCompileEmp1();
        employee.setId(1L);
        employee.setDepartment(department1);
        Employee empPrincipal = getCompileEmp2();
        empPrincipal.setRole(Set.of(Role.ADMIN));
        ArgumentCaptor<Employee> argCaptor = ArgumentCaptor.forClass(Employee.class);

        when(employeeRepo.findById(employee.getId())).thenReturn(Optional.of(employee));
        when(principal.getName()).thenReturn(empPrincipal.getEmail());
        when(employeeRepo.findByEmail(principal.getName())).thenReturn(Optional.of(empPrincipal));
        when(employeeRepo.findById(employee.getId())).thenReturn(Optional.of(employee));

        employeeService.setSalary(employee.getId(), BigDecimal.valueOf(30000), principal);

        verify(employeeRepo).save(argCaptor.capture());
        assertEquals(BigDecimal.valueOf(30000), argCaptor.getValue().getSalary());
    }

    private EmployeeDto getCompileEmpDto() {
        return new EmployeeDto("Employee1", "Testov1", "Testovich1",
                LocalDate.of(2019, 11, 13), "teat1@yandex.ru", "89924543234");
    }

    private Employee getCompileEmp1() {
        return new Employee("Employee1", "Testov1", "Testovich1", null,
                LocalDate.of(2011, 11, 15), "teat1@yandex.ru", "89924543234");
    }

    private Employee getCompileEmp2() {
        return new Employee("Employee2", "Testov2", "Testovich2", null,
                LocalDate.of(2016, 10, 13), "principal1@yandex.ru", "89953445654");
    }
}
