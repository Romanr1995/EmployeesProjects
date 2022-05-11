package ru.consulting.service.integration;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import ru.consulting.dto.EmployeeDto;
import ru.consulting.entitity.Department;
import ru.consulting.entitity.Employee;
import ru.consulting.entitity.Position;
import ru.consulting.entitity.security.Role;
import ru.consulting.repositories.DepartmentRepo;
import ru.consulting.repositories.EmployeeRepo;
import ru.consulting.repositories.PositionRepo;
import ru.consulting.service.EmployeeService;

import java.math.BigDecimal;
import java.security.Principal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class EmployeeIntegration {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    EmployeeRepo employeeRepo;

    @Autowired
    PositionRepo positionRepo;

    @Autowired
    DepartmentRepo departmentRepo;

    ObjectMapper objectMapper = JsonMapper.builder()
            .findAndAddModules()
            .build();

    private EmployeeService employeeService;

    Position pos1;
    Department dep1;
    Employee emp1;
    Employee emp2;

    Employee emp3;

    Department dep2;

    @BeforeEach
    @Transactional
    void sutup() {
        employeeService = mock(EmployeeService.class);

        pos1 = positionRepo.save(new Position("IT"));
        dep1 = departmentRepo.save(new Department("Department1"));
        emp1 = employeeRepo.save(new Employee("Employee1", "Testov1", "Testovich1", null,
                LocalDate.of(2011, 11, 15), "teat1@yandex.ru", "89924543234")
                .setSalary(BigDecimal.valueOf(50000)));

        emp2 = employeeRepo.save(new Employee("Employee2", "Testov2", "Testovich2", null,
                LocalDate.of(2016, 10, 10), "teat2@yandex.ru", "89924543255")
                .setSalary(BigDecimal.valueOf(30000)));

        emp3 = employeeRepo.save(new Employee("Employee3", "Testov3", "Testovich3", null,
                LocalDate.of(2013, 11, 9), "teat3@yandex.ru", "89928743211"));
        dep2 = departmentRepo.save(new Department("Department2"));
    }

    @AfterEach
    @Transactional
    void cleanup() {
        employeeRepo.deleteAll();
        positionRepo.deleteAll();
        departmentRepo.deleteAll();
    }

    @WithMockUser(authorities = "employee:partial_write")
    @Test
    void request_saveNew_success() throws Exception {
        String body = """
                {
                "name": "test1", "surname": "testov1", "dateOfEmployment": "2017-11-11", 
                "email": "test1@yandex.ru", "phone": "89922223455"
                }
                """;

        mockMvc.perform(
                        post("/employee")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(body)
                )
                .andExpect(status().is(201));


    }

    @WithMockUser(roles = {"MAINUSER"})
    @Test
    void request_saveNew_with_phone_is_not_correct() throws Exception {
        String body = """
                {
                "name": "test1", "surname": "testov1", "dateOfEmployment": "2017-11-11", 
                "email": "test3@yandex.ru", "phone": "8992222345"
                }
                """;
        mockMvc.perform(
                        post("/employee")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(body)
                )
                .andExpect(status().is(400));
    }

    @WithMockUser(authorities = "employee:read")
    @Test
    void showAllEmployee_with_filter_min_salary() throws Exception {
        String result = mockMvc.perform(get
                        ("/employee/?minSalary=40000"))
                .andExpect(status().is(200))
                .andReturn().getResponse().getContentAsString();

        List<EmployeeDto> employeeDtos = objectMapper.readValue(result,
                new TypeReference<>() {
                });


        assertEquals(1, employeeDtos.size());
        assertNotEquals(emp2.getSurname(), employeeDtos.get(0).getSurname());
        assertNotEquals(emp2.getPhone(), employeeDtos.get(0).getPhone());
    }

    @WithMockUser(authorities = "main_user_write", username = "teat2@yandex.ru")
    @Test
    void set_Salary_is_Ok() throws Exception {
        departmentRepo.save(dep1.setDepartmentHead(emp2));
        employeeRepo.save(emp1.setDepartment(dep1));
        Principal principal = SecurityContextHolder.getContext().getAuthentication();

        mockMvc.perform(
                        post("/employee/salary/" + emp1.getId())
                                .param("salary", "80000")
                                .principal(principal)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk());

        assertEquals(employeeRepo.findById(emp1.getId()).get().getSalary().setScale(0), new BigDecimal(80000));
    }

    @WithMockUser(authorities = "employee:partial_write", username = "teat3@yandex.ru")
    @Test
    void delete_exist_emp_with_manager_through_the_department() throws Exception {
        employeeRepo.save(emp3.setRole(Set.of(Role.MAINUSER)));
        departmentRepo.save(dep2.setDepartmentHead(emp3));

        departmentRepo.save(dep1.setHigherDepartment(dep2));
        employeeRepo.save(emp1.setDepartment(dep1));
        Principal principal = SecurityContextHolder.getContext().getAuthentication();

        mockMvc.perform(
                        delete("/employee/" + emp1.getId())
                                .contentType(MediaType.APPLICATION_JSON)
                                .principal(principal)
                )
                .andExpect(status().isNoContent());

        assertEquals(employeeRepo.findById(emp1.getId()), Optional.empty());
    }

    @WithMockUser(authorities = "employee:partial_write", username = "teat2@yandex.ru")
    @Test
    void update_department_success() throws Exception {
        departmentRepo.save(dep1.setDepartmentHead(emp2));

        Principal principal = SecurityContextHolder.getContext().getAuthentication();
        mockMvc.perform(
                        put("/employee/update/department/" + dep1.getTitle())
                                .param("name", emp1.getName())
                                .param("surname", emp1.getSurname())
                                .principal(principal)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk());

        assertEquals(employeeRepo.findById(emp1.getId()).get().getDepartment().getTitle(), dep1.getTitle());
    }

    @WithMockUser(authorities = "employee:partial_write", username = "teat2@yandex.ru")
    @Test
    void update_position_success() throws Exception {
        employeeRepo.save(emp2.setRole(Set.of(Role.MAINUSER)));
        departmentRepo.save(dep1.setDepartmentHead(emp2));
        employeeRepo.save(emp1.setDepartment(dep1));

        Principal principal = SecurityContextHolder.getContext().getAuthentication();
        mockMvc.perform(
                        post("/employee/insert/position/" + pos1.getTitle())
                                .param("phone", emp1.getPhone())
                                .param("email", emp1.getEmail())
                                .principal(principal)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk());
    }
}
