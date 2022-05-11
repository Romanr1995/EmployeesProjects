package ru.consulting.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.mockito.ArgumentCaptor;
import ru.consulting.dto.ClientDto;
import ru.consulting.dto.ProjectDto;
import ru.consulting.entitity.Client;
import ru.consulting.entitity.Employee;
import ru.consulting.entitity.Project;
import ru.consulting.exception_handling.NoSuchEntityException;
import ru.consulting.repositories.ClientRepo;
import ru.consulting.repositories.ProjectRepo;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ClientServiceTest {

    public static final String CLIENT_TITLE = "Client1";
    public static final String CLIENT_EMAIL = "test@yandex.ru";
    public static final String CLIENT_PHONE = "89998754433";
    ClientRepo clientRepo;
    ProjectRepo projectRepo;
    ClientService clientService;

    Client client;

    @BeforeEach
    void setup() {
        clientRepo = mock(ClientRepo.class);
        projectRepo = mock(ProjectRepo.class);
        clientService = new ClientService(clientRepo, projectRepo);
        client = new Client(CLIENT_TITLE, CLIENT_EMAIL, CLIENT_PHONE);
    }

    @AfterEach
    void cleanup() {
        clientRepo.deleteAll();
    }

    @Test
    void save_correct_client() {
        ArgumentCaptor<Client> argCaptor = ArgumentCaptor.forClass(Client.class);

        clientService.saveNew(client);

        verify(clientRepo).save(argCaptor.capture());
        assertEquals(CLIENT_TITLE, argCaptor.getValue().getTitle());
        assertEquals(CLIENT_PHONE, argCaptor.getValue().getPhone());
    }

    @Test
    void delete_by_phone_exist() {
        when(clientRepo.findByPhone(CLIENT_PHONE)).thenReturn(Optional.of(client));

        clientService.deleteByPhone(CLIENT_PHONE);

        verify(clientRepo).delete(client);
    }

    @Test
    void delete_by_phone_non_exist() {
        when(clientRepo.findByPhone(CLIENT_PHONE)).thenReturn(Optional.empty());

        Executable executable = () -> clientService.deleteByPhone(CLIENT_PHONE);

        assertThrows(NoSuchEntityException.class, executable);
    }

    @Test
    void show_by_title_exist() {
        Project project1 = new Project("Project1");
        Project project2 = new Project("Project2");
        client.setProjects(List.of(project1, project2));
        when(clientRepo.findByTitleIgnoreCase(client.getTitle())).thenReturn(client);

        ClientDto clientDto = clientService.getByTitle(client.getTitle());

        assertEquals(clientDto.getTitle(), client.getTitle());
        assertEquals(clientDto.getEmail(), client.getEmail());
        assertEquals(clientDto.getPhone(), client.getPhone());
        assertEquals(clientDto.getProjectTitle().size(), client.getProjects().size());

    }

    @Test
    void show_by_title_non_exist() {
        when(clientRepo.findByTitleIgnoreCase(anyString())).thenReturn(null);

        Executable executable = () -> clientService.getByTitle(anyString());

        assertThrowsExactly(NullPointerException.class, executable);
    }

    @Test
    void showAllProjects_by_title_exist() {
        Employee projectManager = new Employee("Test1", "Testov1", "Tesovich1",
                BigDecimal.valueOf(10000), LocalDate.of(2020, 11, 11),
                "test@yandex.ru", "89975456677");
        Project project1 = new Project("Project1", LocalDate.of(2021, 12, 12),
                LocalDate.of(2023, 11, 11), null, projectManager, client);

        Project project2 = new Project("Project2");
        project2.setClient(client);
        client.setProjects(List.of(project1, project2));

        when(clientRepo.findByTitleIgnoreCase(client.getTitle())).thenReturn(client);

        List<ProjectDto> allProjects = clientService.getAllProjects(client.getTitle());

        assertDoesNotThrow(() -> clientService.getAllProjects(client.getTitle()));
        assertEquals(client.getProjects().size(), allProjects.size());
    }

    @Test
    void showAllProjects_by_title_non_exist() {
        when(clientRepo.findByTitleIgnoreCase(anyString())).thenReturn(null);

        List<ProjectDto> projectDtos = clientService.getAllProjects(anyString());

        assertEquals(projectDtos.size(), 0);
        assertDoesNotThrow(() -> clientService.getAllProjects(anyString()));
    }
}
