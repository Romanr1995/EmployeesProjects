package ru.consulting.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.consulting.dto.ProjectDto;
import ru.consulting.entitity.Client;
import ru.consulting.entitity.Employee;
import ru.consulting.entitity.Project;
import ru.consulting.repositories.ClientRepo;
import ru.consulting.repositories.ProjectRepo;

@Service
public class ProjectService {

    private ProjectRepo projectRepo;
    private ClientRepo clientRepo;

    @Autowired
    public ProjectService(ProjectRepo projectRepo, ClientRepo clientRepo) {
        this.projectRepo = projectRepo;
        this.clientRepo = clientRepo;
    }

    public void saveNew(Project project) {
        projectRepo.save(project);
    }

    public void remove(Long id) {
        projectRepo.deleteById(id);
    }

    public ProjectDto getById(Long id) {
        return convertToDto(projectRepo.findById(id).orElseThrow());
    }


    public ProjectDto getByTitle(String title) throws Exception {
        return convertToDto(projectRepo.findByTitleIgnoreCase(title).orElseThrow(() ->
                new Exception("Project с title: " + title + " не найден")));
    }

    public void addClient(String clientEmail, String title) {
        final Client client = clientRepo.findByEmailIgnoreCase(clientEmail).orElseThrow();
        final Project project = projectRepo.findByTitleIgnoreCase(title).orElseThrow();
        projectRepo.save(project.setClient(client));
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
