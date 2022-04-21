package ru.consulting.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.mockito.ArgumentCaptor;
import ru.consulting.dto.DepartmentDto;
import ru.consulting.entitity.Department;
import ru.consulting.entitity.Employee;
import ru.consulting.exception_handling.NoSuchEntityException;
import ru.consulting.repositories.DepartmentRepo;
import ru.consulting.repositories.EmployeeRepo;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class DepartmentServiceTest {

    private static final long DEPARTMENT_ID = 5l;
    private static final String EMP_PHONE = "89997776654";
    private static final String EMP_EMAIL = "emptest@yandex.ru";
    private DepartmentService departmentService;
    private DepartmentRepo departmentRepo;
    private EmployeeRepo employeeRepo;

    @BeforeEach
    void setup() {
        departmentRepo = mock(DepartmentRepo.class);
        employeeRepo = mock(EmployeeRepo.class);
        departmentService = new DepartmentService(departmentRepo, employeeRepo);

        when(departmentRepo.findById(DEPARTMENT_ID)).thenReturn(Optional.of(getDepartment()));
    }

    @Test
    void save_new_department() {
        DepartmentDto departmentDto = new DepartmentDto();
        departmentDto.setTitle("Department1");
        ArgumentCaptor<Department> argCaptor = ArgumentCaptor.forClass(Department.class);

        departmentService.save(departmentDto);

        verify(departmentRepo).save(argCaptor.capture());
        assertEquals("Department1", argCaptor.getValue().getTitle());
    }

    @Test
    void delete_exist_department() {
        when(departmentRepo.existsById(DEPARTMENT_ID)).thenReturn(true);

        departmentService.removeById(DEPARTMENT_ID);

        verify(departmentRepo).deleteById(DEPARTMENT_ID);
    }


    @Test
    void delete_non_exist_department() {
        when(departmentRepo.existsById(DEPARTMENT_ID)).thenReturn(false);

        Executable executable = () -> departmentService.removeById(DEPARTMENT_ID);

        assertThrows(NoSuchEntityException.class, executable);
    }

    @Test
    void delete_with_id_null() {
        Long id = null;
        when(departmentRepo.existsById(id)).thenThrow(IllegalArgumentException.class);

        Executable executable = () -> departmentService.removeById(id);

        assertThrows(IllegalArgumentException.class, executable);
    }

    @Test
    void update_department_head() {
        Employee employee = getEmployee(EMP_PHONE, EMP_EMAIL);
        when(employeeRepo.findByPhoneOrEmailIgnoreCase(EMP_PHONE, EMP_EMAIL)).thenReturn(Optional.of(employee));
        ArgumentCaptor<Department> argCaptor = ArgumentCaptor.forClass(Department.class);

        departmentService.updateDepartmentHead(DEPARTMENT_ID, EMP_PHONE, EMP_EMAIL);

        verify(departmentRepo).save(argCaptor.capture());
        assertEquals(EMP_PHONE, argCaptor.getValue().getDepartmentHead().getPhone());
        assertEquals(EMP_EMAIL, argCaptor.getValue().getDepartmentHead().getEmail());
    }

    @Test
    void update_department_head_with_depId_no_exist() {
        when(departmentRepo.findById(3L)).thenReturn(Optional.ofNullable(null));

        Executable executable = () -> departmentService.updateDepartmentHead(DEPARTMENT_ID, EMP_PHONE, EMP_EMAIL);

        verifyNoInteractions(employeeRepo);
        assertThrows(NoSuchEntityException.class, executable);
    }

    @Test
    void update_department_head_with_empPhone_and_empEmail_is_null() {
        when(employeeRepo.findByPhone(null)).thenThrow(new IllegalArgumentException());

        Executable executable = () -> departmentService.updateDepartmentHead(DEPARTMENT_ID, null, null);

        assertThrows(IllegalArgumentException.class, executable);
    }

    @Test
    void update_department_head_only_empEmail_null() {
        Employee employee = getEmployee(EMP_PHONE, null);
        when(employeeRepo.findByPhone(EMP_PHONE)).thenReturn(employee);
        ArgumentCaptor<Department> argCaptor = ArgumentCaptor.forClass(Department.class);

        departmentService.updateDepartmentHead(DEPARTMENT_ID, EMP_PHONE, null);

        verify(departmentRepo).save(argCaptor.capture());
        assertEquals(EMP_PHONE, argCaptor.getValue().getDepartmentHead().getPhone());
        assertNull(argCaptor.getValue().getDepartmentHead().getEmail());
    }


    private Department getDepartment() {
        Department department = new Department();
        department.setId(DEPARTMENT_ID);
        return department;
    }

    private Employee getEmployee(String phone, String email) {
        Employee employee = new Employee();
        employee.setPhone(phone).setEmail(email);
        return employee;
    }
}
