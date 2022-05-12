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
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import ru.consulting.dto.ClientDto;
import ru.consulting.dto.ProjectDto;
import ru.consulting.entitity.Client;
import ru.consulting.entitity.Employee;
import ru.consulting.entitity.Project;
import ru.consulting.repositories.ClientRepo;
import ru.consulting.repositories.EmployeeRepo;
import ru.consulting.repositories.ProjectRepo;

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
public class ClientIntegration {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ClientRepo clientRepo;

    @Autowired
    ProjectRepo projectRepo;

    @Autowired
    EmployeeRepo employeeRepo;

    ObjectMapper objectMapper = JsonMapper.builder()
            .findAndAddModules()
            .build();

    Client client1;
    Employee employee1;
    Project project1;
    Project project2;

    @BeforeEach
    @Transactional
    void sutup() {
        employee1 = employeeRepo.save(new Employee("Test1", "Testov1", "Testovich1",
                BigDecimal.valueOf(30000), LocalDate.of(2019, 8, 11),
                "test@yandex.ru", "89765445344"));
        client1 = clientRepo.save(new Client("Client1", "clientest@yandex.ru", "89865543432"));
        project1 = projectRepo.save(new Project("Project1", LocalDate.of(2018, 12, 4),
                LocalDate.of(2023, 11, 11), null, employee1, client1));
        project2 = projectRepo.save(new Project("Project2", LocalDate.of(2016, 8, 5),
                LocalDate.of(2021, 12, 14),
                LocalDate.of(2020, 3, 7), employee1, client1));
    }

    @AfterEach
    @Transactional
    void cleanup() {
        employeeRepo.deleteAll();
        clientRepo.deleteAll();
        projectRepo.deleteAll();
    }
    @WithMockUser(authorities = {"employee:write"})
    @Test
    void request_save_is_ok() throws Exception {
        String body = """
                {
                "title": "Client2","email": "client2@yandex.ru","phone": "89345454543"
                }
                """;

        mockMvc.perform(
                        post("/client")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(body)
                )
                .andExpect(status().isOk());
    }
    @WithMockUser(authorities = {"employee:write"})
    @Test
    void request_save_exist_client() throws Exception {
        String body = """
                {
                "title": "Client1","email": "clientest@yandex.ru","phone": "89865543432"
                }
                """;

        mockMvc.perform(
                        post("/client")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(body)
                )
                .andExpect(status().is(400));
    }
    @WithMockUser(authorities = {"employee:write"})
    @Test
    void delete_by_phone_success() throws Exception {
        mockMvc.perform(
                        delete("/client/" + client1.getPhone())
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isNoContent());

        assertEquals(clientRepo.findByPhone(client1.getPhone()), Optional.empty());
    }
    @WithMockUser(authorities = {"employee_read"})
    @Test
    void showByTitle_success() throws Exception {
        String result = mockMvc.perform(get
                        ("/client")
                        .param("title", client1.getTitle()))
                .andExpect(status().is(200))
                .andReturn().getResponse().getContentAsString();

        ClientDto clientDto = objectMapper.readValue(result,
                new TypeReference<>() {
                });

        assertEquals(client1.getTitle(), clientDto.getTitle());
        assertEquals(client1.getEmail(), clientDto.getEmail());
        assertEquals(client1.getPhone(), clientDto.getPhone());
    }
    @WithMockUser(authorities = {"employee_read"})
    @Test
    void showAllProjects_is_success() throws Exception {
        String result = mockMvc.perform(get
                        ("/client/allprojects")
                        .param("title", client1.getTitle()))
                .andExpect(status().is(200))
                .andReturn().getResponse().getContentAsString();

        List<ProjectDto> projectDtos = objectMapper.readValue(result,
                new TypeReference<>() {
                });

        assertEquals(2, projectDtos.size());
        assertEquals(client1.getTitle(), projectDtos.get(0).getClientTitle());
        assertEquals(employee1.getSurname(), projectDtos.get(1).getProjectManagerSurname());
    }
}
