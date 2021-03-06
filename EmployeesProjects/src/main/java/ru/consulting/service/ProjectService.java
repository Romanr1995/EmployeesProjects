package ru.consulting.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.consulting.dto.ProjectDto;
import ru.consulting.entitity.Client;
import ru.consulting.entitity.Employee;
import ru.consulting.entitity.Project;
import ru.consulting.exception_handling.NoSuchEntityException;
import ru.consulting.repositories.ClientRepo;
import ru.consulting.repositories.EmployeeRepo;
import ru.consulting.repositories.ProjectRepo;

import java.util.Optional;

@Service
public class ProjectService {

    private ProjectRepo projectRepo;
    private ClientRepo clientRepo;
    private EmployeeRepo employeeRepo;

    @Autowired
    public ProjectService(ProjectRepo projectRepo, ClientRepo clientRepo, EmployeeRepo employeeRepo) {
        this.projectRepo = projectRepo;
        this.clientRepo = clientRepo;
        this.employeeRepo = employeeRepo;
    }

    public void saveNew(Project project) {
        projectRepo.save(project);
    }

    public void remove(Long id) {
        projectRepo.deleteById(id);
    }

    public ProjectDto getById(Long id) {
        final Optional<Project> byId = projectRepo.findById(id);
        return convertToDto(byId.orElseThrow(() -> new NoSuchEntityException(id, Project.class)));
    }


    public ProjectDto getByTitle(String title) {
        return convertToDto(projectRepo.findByTitleIgnoreCase(title).orElseThrow(() ->
                new NoSuchEntityException("Project с title: " + title + " не найден")));
    }

    public void addClient(String clientEmail, String title) {
        final Client client = clientRepo.findByEmailIgnoreCase(clientEmail).orElseThrow(
                () -> new NoSuchEntityException("Client with email: " + clientEmail + " not found."));
        final Project project = projectRepo.findByTitleIgnoreCase(title).orElseThrow(
                () -> new NoSuchEntityException("Project with title: " + title + " not found."));
        projectRepo.save(project.setClient(client));
    }

    public void addProjectManager(Long projectId, Long managerId) {
        final Project project = projectRepo.findById(projectId).orElseThrow(
                () -> new NoSuchEntityException(projectId, Project.class));
        final Employee employee = employeeRepo.findById(managerId).orElseThrow(
                () -> new NoSuchEntityException(managerId, Employee.class));
        projectRepo.save(project.setProjectManager(employee));
    }

    public static ProjectDto convertToDto(Project project) {
        final Employee projectManager = project.getProjectManager();
        String surnameManager;
        if (projectManager == null) {
            surnameManager = "пока не задано";
        } else {
            surnameManager = projectManager.getSurname();
        }
        final Client client = project.getClient();
        String clientTitle;
        if (client == null) {
            clientTitle = "пока не задано";
        } else {
            clientTitle = client.getTitle();
        }
        return new ProjectDto(project.getId(), project.getTitle(), project.getStart(), project.getPlannedEnding(),
                project.getEnding(), surnameManager, clientTitle);
    }

}
