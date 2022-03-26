package ru.consulting.service;

import lombok.NoArgsConstructor;
import org.hibernate.Filter;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.consulting.entitity.EmployeeOnProject;
import ru.consulting.repositories.EmployeeOnProjectRepo;

import javax.persistence.EntityManager;

@NoArgsConstructor
@Service
public class EmployeeOnProjectService {

    private EmployeeOnProjectRepo employeeOnProjectRepo;
    private EntityManager entityManager;

    @Autowired
    public EmployeeOnProjectService(EmployeeOnProjectRepo employeeOnProjectRepo, EntityManager entityManager) {
        this.employeeOnProjectRepo = employeeOnProjectRepo;
        this.entityManager = entityManager;
    }

    public void add(EmployeeOnProject employeeOnProject) {
        employeeOnProjectRepo.save(employeeOnProject);
    }

    public void removeById(Long id) {
        employeeOnProjectRepo.deleteById(id);
    }

    public Iterable<EmployeeOnProject> findAll(Boolean isDeleted) {
        if (isDeleted == null) {
            isDeleted = false;
        }
        final Session session = entityManager.unwrap(Session.class);
        final Filter filter = session.enableFilter("deletedEmployeeOnProjectFilter");
        filter.setParameter("isDeleted", isDeleted);
        Iterable<EmployeeOnProject> projectRepoAll = employeeOnProjectRepo.findAll();
        session.disableFilter("deletedEmployeeOnProjectFilter");
        return projectRepoAll;
    }

}
