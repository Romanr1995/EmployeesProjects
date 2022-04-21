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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class DepartmentServiceTest {

    private DepartmentService departmentService;
    private DepartmentRepo departmentRepo;
    private EmployeeRepo employeeRepo;

    @BeforeEach
    void setup() {
        departmentRepo = mock(DepartmentRepo.class);
        employeeRepo = mock(EmployeeRepo.class);
        departmentService = new DepartmentService(departmentRepo, employeeRepo);
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
        Long id = 5l;
        when(departmentRepo.existsById(id)).thenReturn(true);

        departmentService.removeById(id);

        verify(departmentRepo).deleteById(id);
    }


    @Test
    void delete_non_exist_department() {
        Long id = 5l;
        when(departmentRepo.existsById(id)).thenReturn(false);

        Executable executable = () -> departmentService.removeById(id);

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
        Long depId = 1L;
        Department department = new Department();
        department.setId(depId);
        when(departmentRepo.findById(depId)).thenReturn(Optional.of(department));

        String empPhone = "89997776654";
        String empEmail = "emptest@yandex.ru";
        Employee employee = new Employee();
        employee.setPhone(empPhone).setEmail(empEmail);
        when(employeeRepo.findByPhoneOrEmailIgnoreCase(empPhone, empEmail)).thenReturn(Optional.of(employee));
        department.setDepartmentHead(employee);
        ArgumentCaptor<Department> argCaptor = ArgumentCaptor.forClass(Department.class);

        departmentService.updateDepartmentHead(depId, empPhone, empEmail);

        verify(departmentRepo).save(argCaptor.capture());
        assertEquals("89997776654", argCaptor.getValue().getDepartmentHead().getPhone());
        assertEquals("emptest@yandex.ru", argCaptor.getValue().getDepartmentHead().getEmail());
    }


}
