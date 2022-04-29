package ru.consulting.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.consulting.dto.ClientDto;
import ru.consulting.dto.ProjectDto;
import ru.consulting.entitity.Client;
import ru.consulting.entitity.Project;
import ru.consulting.exception_handling.NoSuchEntityException;
import ru.consulting.repositories.ClientRepo;
import ru.consulting.repositories.ProjectRepo;

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

    public void saveNew(Client client) {
        clientRepo.save(client);
    }

    public ClientDto getByTitle(String title) {
        return convertToDto(clientRepo.findByTitleIgnoreCase(title));
    }

    public void deleteByPhone(String phone) {
        Client client = clientRepo.findByPhone(phone).orElseThrow(() ->
                new NoSuchEntityException("Client с phone: " + phone + " не найден."));
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

    public static ClientDto convertToDto(Client client) {
        final List<String> collectProjectTitle = client.getProjects().stream().map(Project::getTitle).collect(Collectors.toList());
        return new ClientDto(client.getTitle(), client.getEmail(), client.getPhone(), collectProjectTitle);
    }
}
