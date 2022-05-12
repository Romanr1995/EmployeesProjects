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
import ru.consulting.dto.ProjectRoleDto;
import ru.consulting.entitity.ProjectRole;
import ru.consulting.repositories.ProjectRoleRepo;

import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class ProjectRoleIntegration {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ProjectRoleRepo projectRoleRepo;

    ObjectMapper objectMapper = JsonMapper.builder()
            .findAndAddModules()
            .build();

    ProjectRole projectRole1;
    ProjectRole projectRole2;

    @BeforeEach
    @Transactional
    void sutup() {
        projectRole1 = projectRoleRepo.save(new ProjectRole("ProjectRole1"));
        projectRole2 = projectRoleRepo.save(new ProjectRole("ProjectRole2"));
    }

    @AfterEach
    @Transactional
    void cleanup() {
        projectRoleRepo.deleteAll();
    }

    @WithMockUser(authorities = {"employee:write"})
    @Test
    void add_is_Ok() throws Exception {
        String body = """
                {
                "title": "ProjectRole3"
                }
                """;

        mockMvc.perform(
                        post("/role/project")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(body)
                )
                .andExpect(status().isOk());

        assertFalse(projectRoleRepo.findByTitleIgnoreCase("ProjectRole3").get().isDeleted());
    }

    @WithMockUser(authorities = {"employee:write"})
    @Test
    void delete_by_title_success() throws Exception {
        mockMvc.perform(
                        delete("/role/project/" + projectRole1.getTitle())
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk());

        assertTrue(projectRoleRepo.findByTitleIgnoreCase(projectRole1.getTitle()).get().isDeleted());
    }

    @WithMockUser(authorities = {"project_write"})
    @Test
    void delete_by_title_with_role_not_exist() throws Exception {
        mockMvc.perform(
                        delete("/role/project/NotExistProjectRole")
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().is(404));
    }

    @WithMockUser(authorities = {"project_write"})
    @Test
    void showAll() throws Exception {
        String result = mockMvc.perform(get
                        ("/role/project/"))
                .andExpect(status().is(200))
                .andReturn().getResponse().getContentAsString();

        Iterable<ProjectRoleDto> projectRoleDtos = objectMapper.readValue(result,
                new TypeReference<>() {
                });

        List<ProjectRoleDto> projectRoleDtoList = (List<ProjectRoleDto>) projectRoleDtos;
        assertEquals(2, projectRoleDtoList.size());
        List<String> strings = projectRoleDtoList.stream().map(ProjectRoleDto::getTitle).collect(Collectors.toList());
        assertTrue(strings.contains(projectRole1.getTitle()));
        assertTrue(strings.contains(projectRole2.getTitle()));
    }
}
