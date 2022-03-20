package ru.consulting.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.consulting.dto.ProjectDto;
import ru.consulting.entitity.Client;
import ru.consulting.entitity.Project;
import ru.consulting.repositories.ClientRepo;
import ru.consulting.repositories.ProjectRepo;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ClientService {

    private ClientRepo clientRepo;
    private ProjectRepo projectRepo;

    @Autowired
    public ClientService(ClientRepo clientRepo, ProjectRepo projectRepo) {
        this.clientRepo = clientRepo;
        this.projectRepo = projectRepo;
    }

    public void saveNew(@Valid Client client) {
        clientRepo.save(client);
    }

    public Client getByTitle(String title) {
        return clientRepo.findByTitleIgnoreCase(title);
    }

    public void deleteByPhone(String phone) {
        Client client = clientRepo.findByPhone(phone).orElseThrow(() ->
                new RuntimeException("Client с phone: " + phone + " не найден."));
        for (Project project : client.getProjects()) {
            project.setClient(null);
            projectRepo.save(project);
        }
        clientRepo.delete(client);
    }

    public List<ProjectDto> getAllProjects(String title) {
        Client cl = clientRepo.findByTitleIgnoreCase(title);
        if (cl != null) {
            return cl.getProjects().stream().map(ProjectService::convertToDto).collect(Collectors.toList());
        } else {
            return List.of();
        }
    }
}
