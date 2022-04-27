package ru.consulting.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import ru.consulting.dto.DepartmentDto;
import ru.consulting.entitity.Department;
import ru.consulting.entitity.Employee;
import ru.consulting.repositories.DepartmentRepo;
import ru.consulting.repositories.EmployeeRepo;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class DepartmentIntegration {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    DepartmentRepo departmentRepo;

    @Autowired
    EmployeeRepo employeeRepo;

    ObjectMapper objectMapper = new ObjectMapper();

    Department department1;
    Employee employee1;

    @BeforeEach
    @Transactional
    void sutup() {
        department1 = departmentRepo.save(new Department().setTitle("Department1"));
        employee1 = employeeRepo.save(new Employee("Test1", "Testov1", "Testovich1",
                BigDecimal.valueOf(30000), LocalDate.of(2019, 8, 11),
                "test@yandex.ru", "89765445344"));
    }

    @AfterEach
    @Transactional
    void cleanup() {
        departmentRepo.deleteAll();
        employeeRepo.deleteAll();
    }

    @Test
    void request_saveNew_success() throws Exception {
        String body = "{\"title\":\"Department2\"}";
        mockMvc.perform(
                        post("/department")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(body)
                )
                .andExpect(status().is(200));
    }

    @Test
    void reques_showAll_success() throws Exception {
        String result = mockMvc.perform(get
                        ("/department"))
                .andExpect(status().is(200))
                .andReturn().getResponse().getContentAsString();

        List<DepartmentDto> readValue = objectMapper.readValue(result,
                new TypeReference<>() {
                });

        assertEquals(department1.getTitle(), readValue.get(0).getTitle());
    }

    @Test
    void request_deleteById_success() throws Exception {
        mockMvc.perform(
                        delete("/department/" + department1.getId())
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isNoContent());

        assertEquals(departmentRepo.findById(department1.getId()), Optional.empty());
    }

    @Test
    void reauest_updateDepartmentHead_success() throws Exception {
        mockMvc.perform(
                        put("/department/update/departmenthead/" + department1.getId())
                                .param("phone", employee1.getPhone())
                                .param("email", employee1.getEmail())
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk());
    }

    @Test
    void request_setHigherDepartment() throws Exception {
        Department department = departmentRepo.save(new Department("dep1"));
        Department departmentHigher = departmentRepo.save(new Department("dep2"));

        String body = """
                {
                "department": {"title": "dep1"},
                "higherDepartment": {"title": "dep2"}
                }
                """;

        mockMvc.perform(
                        post("/department/savehigherdepartment")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(body)
                )
                .andExpect(status().isOk());
    }

}
