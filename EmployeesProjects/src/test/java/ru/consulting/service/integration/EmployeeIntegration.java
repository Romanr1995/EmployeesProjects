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
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import ru.consulting.dto.EmployeeDto;
import ru.consulting.entitity.Department;
import ru.consulting.entitity.Employee;
import ru.consulting.entitity.Position;
import ru.consulting.repositories.DepartmentRepo;
import ru.consulting.repositories.EmployeeRepo;
import ru.consulting.repositories.PositionRepo;
import ru.consulting.service.EmployeeService;

import java.math.BigDecimal;
import java.security.Principal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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

    private Principal principal;
    private EmployeeService employeeService;

    Position pos1;
    Department dep1;
    Employee emp1;
    Employee emp2;

    @BeforeEach
    @Transactional
    void sutup() {
        principal = mock(Principal.class);
        employeeService = mock(EmployeeService.class);

        pos1 = positionRepo.save(new Position("IT"));
        dep1 = departmentRepo.save(new Department("Department1"));
        emp1 = employeeRepo.save(new Employee("Employee1", "Testov1", "Testovich1", null,
                LocalDate.of(2011, 11, 15), "teat1@yandex.ru", "89924543234")
                .setSalary(BigDecimal.valueOf(50000)));

        emp2 = employeeRepo.save(new Employee("Employee2", "Testov2", "Testovich2", null,
                LocalDate.of(2016, 10, 10), "teat2@yandex.ru", "89924543255")
                .setSalary(BigDecimal.valueOf(30000)));
    }

    @AfterEach
    @Transactional
    void cleanup() {
        employeeRepo.deleteAll();
        positionRepo.deleteAll();
        departmentRepo.deleteAll();
    }

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

    @Test
    void request_saveNew_with_phone_is_not_correct() throws Exception {
        String body = """
                {
                "name": "test1", "surname": "testov1", "dateOfEmployment": "2017-11-11", 
                "email": "test1@yandex.ru", "phone": "8992222345"
                }
                """;

        mockMvc.perform(
                        post("/employee")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(body)
                )
                .andExpect(status().is(400));
    }

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

}
