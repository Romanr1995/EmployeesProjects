package ru.consulting.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.consulting.dto.ProjectDto;
import ru.consulting.entitity.Employee;
import ru.consulting.entitity.Project;
import ru.consulting.repositories.ProjectRepo;

@Service
public class ProjectService {

    private ProjectRepo projectRepo;

    @Autowired
    public ProjectService(ProjectRepo projectRepo) {
        this.projectRepo = projectRepo;
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


    public ProjectDto convertToDto(Project project) {
        final Employee projectManager = project.getProjectManager();
        String surnameManager;
        if (projectManager == null) {
            surnameManager = "пока не задано";
        } else {
            surnameManager = projectManager.getSurname();
        }
        return new ProjectDto(project.getId(), project.getTitle(), project.getStart(), project.getPlannedEnding(),
                project.getEnding(), surnameManager);
    }
}
