package ru.consulting.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import ru.consulting.dto.ProjectRoleDto;
import ru.consulting.entitity.ProjectRole;
import ru.consulting.repositories.ProjectRoleRepo;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ProjectRoleServiceTest {

    public static final String ROLE_1 = "ProjectRole1";
    ProjectRoleRepo projectRoleRepo;
    EntityManager entityManager;
    ProjectRoleService projectRoleService;

    ProjectRole projectRole;

    @BeforeEach
    void setup() {
        projectRoleRepo = mock(ProjectRoleRepo.class);
        entityManager = mock(EntityManager.class);
        projectRoleService = new ProjectRoleService(projectRoleRepo, entityManager);

        projectRole = new ProjectRole(ROLE_1);
    }

    @AfterEach
    void cleanup() {
        projectRoleRepo.deleteAll();
    }

    @Test
    void save_new_access() {
        ArgumentCaptor<ProjectRole> argCaptor = ArgumentCaptor.forClass(ProjectRole.class);

        projectRoleService.addNewProjectRole(projectRole);

        verify(projectRoleRepo).save(argCaptor.capture());
        assertEquals(ROLE_1, argCaptor.getValue().getTitle());
        assertFalse(argCaptor.getValue().isDeleted());
    }

    @Test
    void delete() {
        when(projectRoleRepo.findByTitleIgnoreCase(ROLE_1)).thenReturn(Optional.of(projectRole));

        projectRoleService.deleteByTitle(ROLE_1);

        assertDoesNotThrow(() -> projectRoleService.deleteByTitle(ROLE_1));
    }

    @Test
    void findAll() {
        ProjectRole projectRole2 = new ProjectRole("ProjectRole2");
        List<ProjectRole> projectRoleList = List.of(projectRole, projectRole2);
        when(projectRoleRepo.findAll()).thenReturn(projectRoleList);

        Iterable<ProjectRoleDto> all = projectRoleService.findAll(null);

        for (ProjectRoleDto projectRoleDto : all) {
            assertTrue(projectRoleDto.getTitle().equals(projectRole.getTitle())
                    || projectRoleDto.getTitle().equals(projectRole2.getTitle()));
        }
    }
}
