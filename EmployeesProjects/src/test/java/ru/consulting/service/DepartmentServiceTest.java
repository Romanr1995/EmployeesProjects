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

import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class DepartmentServiceTest {

    private static final long DEPARTMENT_ID1 = 5L;
    private static final long DEPARTMENT_ID2 = 6L;
    private static final String EMP_PHONE = "89997776654";
    private static final String EMP_EMAIL = "emptest@yandex.ru";
    private static final String DEPARTMENT_TITLE = "dep1";
    private static final String HIGHER_DEPARTMENT_TITLE = "dep2";
    public static final String KEY_DEPARTMENT = "department";
    public static final String KEY_HIGHER_DEPARTMENT = "higherDepartment";
    private DepartmentService departmentService;
    private DepartmentRepo departmentRepo;
    private EmployeeRepo employeeRepo;

    @BeforeEach
    void setup() {
        departmentRepo = mock(DepartmentRepo.class);
        employeeRepo = mock(EmployeeRepo.class);
        departmentService = new DepartmentService(departmentRepo, employeeRepo);

        when(departmentRepo.findById(DEPARTMENT_ID1)).thenReturn(Optional.of(getDepartment()));
    }

    @Test
    void save_new_department() {
        DepartmentDto departmentDto = new DepartmentDto("Department1");
        ArgumentCaptor<Department> argCaptor = ArgumentCaptor.forClass(Department.class);

        departmentService.save(departmentDto);

        verify(departmentRepo).save(argCaptor.capture());
        assertEquals("Department1", argCaptor.getValue().getTitle());
    }

    @Test
    void delete_exist_department() {
        when(departmentRepo.existsById(DEPARTMENT_ID1)).thenReturn(true);

        departmentService.removeById(DEPARTMENT_ID1);

        verify(departmentRepo).deleteById(DEPARTMENT_ID1);
    }


    @Test
    void delete_non_exist_department() {
        when(departmentRepo.existsById(DEPARTMENT_ID1)).thenReturn(false);

        Executable executable = () -> departmentService.removeById(DEPARTMENT_ID1);

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

        departmentService.updateDepartmentHead(DEPARTMENT_ID1, EMP_PHONE, EMP_EMAIL);

        verify(departmentRepo).save(argCaptor.capture());
        assertEquals(EMP_PHONE, argCaptor.getValue().getDepartmentHead().getPhone());
        assertEquals(EMP_EMAIL, argCaptor.getValue().getDepartmentHead().getEmail());
    }

    @Test
    void update_department_head_with_depId_no_exist() {
        when(departmentRepo.findById(3L)).thenReturn(Optional.empty());

        Executable executable = () -> departmentService.updateDepartmentHead(DEPARTMENT_ID1, EMP_PHONE, EMP_EMAIL);

        verifyNoInteractions(employeeRepo);
        assertThrows(NoSuchEntityException.class, executable);
    }

    @Test
    void update_department_head_with_empPhone_and_empEmail_is_null() {
        when(employeeRepo.findByPhone(null)).thenThrow(new IllegalArgumentException());

        Executable executable = () -> departmentService.updateDepartmentHead(DEPARTMENT_ID1, null, null);

        assertThrows(IllegalArgumentException.class, executable);
    }

    @Test
    void update_department_head_only_empEmail_null() {
        Employee employee = getEmployee(EMP_PHONE, null);
        when(employeeRepo.findByPhone(EMP_PHONE)).thenReturn(employee);
        ArgumentCaptor<Department> argCaptor = ArgumentCaptor.forClass(Department.class);

        departmentService.updateDepartmentHead(DEPARTMENT_ID1, EMP_PHONE, null);

        verify(departmentRepo).save(argCaptor.capture());
        assertEquals(EMP_PHONE, argCaptor.getValue().getDepartmentHead().getPhone());
        assertNull(argCaptor.getValue().getDepartmentHead().getEmail());
    }

    @Test
    void set_higher_department() {
        DepartmentDto departmentDto = getDepartmentDto(DEPARTMENT_ID1, DEPARTMENT_TITLE);
        Department department = getDepartment(departmentDto);
        DepartmentDto higherDepartmentDto = getDepartmentDto(DEPARTMENT_ID2, HIGHER_DEPARTMENT_TITLE);
        Department higherDepartment = getDepartment(higherDepartmentDto);
        Map<String, DepartmentDto> departmentDtoMap = getMap(departmentDto, higherDepartmentDto);
        ArgumentCaptor<Department> argCaptor = ArgumentCaptor.forClass(Department.class);

        when(departmentRepo.findByIdOrTitleIgnoreCase(higherDepartmentDto.getId(), higherDepartmentDto.getTitle()))
                .thenReturn(Optional.of(higherDepartment));
        when(departmentRepo.findByIdOrTitleIgnoreCase(departmentDto.getId(), departmentDto.getTitle()))
                .thenReturn(Optional.of(department));

        departmentService.setHigherDepartment(departmentDtoMap);

        verify(departmentRepo).save(argCaptor.capture());
        assertEquals(DEPARTMENT_ID2, argCaptor.getValue().getHigherDepartment().getId());
        assertEquals(HIGHER_DEPARTMENT_TITLE, argCaptor.getValue().getHigherDepartment().getTitle());
    }

    @Test
    void set_new_higher_department() {
        DepartmentDto departmentDto = getDepartmentDto(DEPARTMENT_ID1, DEPARTMENT_TITLE);
        Department department = getDepartment(departmentDto);
        DepartmentDto higherDepartmentDto = getDepartmentDto(DEPARTMENT_ID2, HIGHER_DEPARTMENT_TITLE);
        Map<String, DepartmentDto> departmentDtoMap = getMap(departmentDto, higherDepartmentDto);
        ArgumentCaptor<Department> argCaptor = ArgumentCaptor.forClass(Department.class);

        when(departmentRepo.findByIdOrTitleIgnoreCase(higherDepartmentDto.getId(), higherDepartmentDto.getTitle()))
                .thenReturn(Optional.empty());
        when(departmentRepo.findByIdOrTitleIgnoreCase(departmentDto.getId(), departmentDto.getTitle()))
                .thenReturn(Optional.of(department));

        departmentService.setHigherDepartment(departmentDtoMap);

        verify(departmentRepo, times(2)).save(argCaptor.capture());
    }

    @Test
    void set_exist_higher_department_with_id_not_set() {
        DepartmentDto departmentDto = getDepartmentDto(null, DEPARTMENT_TITLE);
        Department department = getDepartment(departmentDto);
        DepartmentDto higherDepartmentDto = getDepartmentDto(null, HIGHER_DEPARTMENT_TITLE);
        Department higherDepartment = getDepartment(higherDepartmentDto);
        Map<String, DepartmentDto> departmentDtoMap = getMap(departmentDto, higherDepartmentDto);
        ArgumentCaptor<Department> argCaptor = ArgumentCaptor.forClass(Department.class);

        when(departmentRepo.findByIdOrTitleIgnoreCase(higherDepartmentDto.getId(), higherDepartmentDto.getTitle()))
                .thenReturn(Optional.of(higherDepartment));
        when(departmentRepo.findByIdOrTitleIgnoreCase(departmentDto.getId(), departmentDto.getTitle()))
                .thenReturn(Optional.of(department));

        departmentService.setHigherDepartment(departmentDtoMap);

        verify(departmentRepo).save(argCaptor.capture());
        assertEquals("dep2", argCaptor.getValue().getHigherDepartment().getTitle());
        assertNull(argCaptor.getValue().getHigherDepartment().getId());
    }

    @Test
    void set_higher_department_with_key_map_is_bad() {
        DepartmentDto departmentDto = getDepartmentDto(DEPARTMENT_ID1, DEPARTMENT_TITLE);
        DepartmentDto higherDepartmentDto = getDepartmentDto(DEPARTMENT_ID2, HIGHER_DEPARTMENT_TITLE);
        Map<String, DepartmentDto> departmentDtoMap = getMap("badkey", departmentDto,
                KEY_HIGHER_DEPARTMENT, higherDepartmentDto);

        Executable executable = () -> departmentService.setHigherDepartment(departmentDtoMap);

        assertThrows(RuntimeException.class, executable);

    }

    @Test
    void set_higher_department_with_department_non_exist() {
        DepartmentDto departmentDto = getDepartmentDto(DEPARTMENT_ID1, DEPARTMENT_TITLE);
        DepartmentDto higherDepartmentDto = getDepartmentDto(DEPARTMENT_ID2, HIGHER_DEPARTMENT_TITLE);
        Department higherDepartment = getDepartment(higherDepartmentDto);
        Map<String, DepartmentDto> departmentDtoMap = getMap(departmentDto, higherDepartmentDto);

        when(departmentRepo.findByIdOrTitleIgnoreCase(higherDepartmentDto.getId(), higherDepartmentDto.getTitle()))
                .thenReturn(Optional.of(higherDepartment));
        when(departmentRepo.findByIdOrTitleIgnoreCase(departmentDto.getId(), departmentDto.getTitle()))
                .thenReturn(Optional.empty());

        Executable executable = () -> departmentService.setHigherDepartment(departmentDtoMap);

        assertThrows(NoSuchEntityException.class, executable);

    }


    private Department getDepartment() {
        Department department = new Department();
        department.setId(DEPARTMENT_ID1);
        return department;
    }

    private Department getDepartment(DepartmentDto departmentDto) {
        return new Department().setId(departmentDto.getId()).setTitle(departmentDto.getTitle());
    }

    private DepartmentDto getDepartmentDto(Long id, String title) {
        DepartmentDto departmentDto = new DepartmentDto();
        departmentDto.setId(id);
        departmentDto.setTitle(title);
        return departmentDto;
    }

    private Map<String, DepartmentDto> getMap(DepartmentDto departmentDto,
                                              DepartmentDto higherDepartmentDto) {
        return Map.of(KEY_DEPARTMENT, departmentDto,
                KEY_HIGHER_DEPARTMENT, higherDepartmentDto);
    }

    private Map<String, DepartmentDto> getMap(String departmentKey, DepartmentDto departmentDto,
                                              String higherDepartmentKey, DepartmentDto higherDepartmentDto) {
        return Map.of(departmentKey, departmentDto,
                higherDepartmentKey, higherDepartmentDto);
    }

    private Employee getEmployee(String phone, String email) {
        Employee employee = new Employee();
        employee.setPhone(phone).setEmail(email);
        return employee;
    }
}
