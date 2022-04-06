package ru.consulting.service;

import org.hibernate.Filter;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.consulting.dto.ProjectRoleDto;
import ru.consulting.entitity.EmployeeOnProject;
import ru.consulting.entitity.ProjectRole;
import ru.consulting.exception_handling.NoSuchEntityException;
import ru.consulting.repositories.ProjectRoleRepo;

import javax.persistence.EntityManager;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@Service
public class ProjectRoleService {
    private ProjectRoleRepo projectRoleRepo;
    private EntityManager entityManager;

    @Autowired
    public ProjectRoleService(ProjectRoleRepo projectRoleRepo, EntityManager entityManager) {
        this.projectRoleRepo = projectRoleRepo;
        this.entityManager = entityManager;
    }

    public void addNewProjectRole(ProjectRole projectRole) {
        projectRoleRepo.save(projectRole);
    }


    public void deleteByTitle(String title) {
        final ProjectRole projectRole = projectRoleRepo.findByTitleIgnoreCase(title).orElseThrow(
                () -> new NoSuchEntityException("ProjectRole with title: " + title + " not found."));
        projectRoleRepo.delete(projectRole);
    }

    public Iterable<ProjectRoleDto> findAll(Boolean isDeleted) {
        if (isDeleted == null) {
            return convertProjectRoleToDtoIterable(projectRoleRepo.findAll());
        }
        final Session session = entityManager.unwrap(Session.class);
        final Filter filter = session.enableFilter("deleteProjectRole");
        filter.setParameter("isDeleted", isDeleted);
        final Iterable<ProjectRoleDto> projectRoleDtos = convertProjectRoleToDtoIterable(projectRoleRepo.findAll());
        session.disableFilter("deleteProjectRole");
        return projectRoleDtos;
    }


    private Iterable<ProjectRoleDto> convertProjectRoleToDtoIterable(Iterable<ProjectRole> projectRoleIterable) {

        final Iterator<ProjectRole> iterator = projectRoleIterable.iterator();
        return () -> new Iterator<>() {
            @Override
            public boolean hasNext() {
                return iterator.hasNext();
            }

            @Override
            public ProjectRoleDto next() {
                final ProjectRole next = iterator.next();
                Map<String, String> empSurnameAndProjectTitle = new HashMap<>();
                for (EmployeeOnProject employeeOnProject : next.getEmployeeOnProjects()) {
                    empSurnameAndProjectTitle.put(employeeOnProject.getEmployee().getSurname()
                            , employeeOnProject.getProject().getTitle());
                }
                return new ProjectRoleDto(next.getTitle(), empSurnameAndProjectTitle);
            }
        };
    }
}
