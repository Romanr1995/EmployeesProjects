package ru.consulting.service;

import org.hibernate.Filter;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import ru.consulting.dto.EmployeeOnProjectDto;
import ru.consulting.entitity.Employee;
import ru.consulting.entitity.EmployeeOnProject;
import ru.consulting.entitity.Project;
import ru.consulting.entitity.ProjectRole;
import ru.consulting.entitity.security.Role;
import ru.consulting.repositories.EmployeeOnProjectRepo;
import ru.consulting.repositories.EmployeeRepo;
import ru.consulting.repositories.ProjectRepo;
import ru.consulting.repositories.ProjectRoleRepo;

import javax.persistence.EntityManager;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static ru.consulting.service.EmployeeService.isEmployeeUnderManager;

@Validated
@Service
public class EmployeeOnProjectService {

    private EmployeeOnProjectRepo employeeOnProjectRepo;
    private EntityManager entityManager;
    private EmployeeRepo employeeRepo;
    private ProjectRepo projectRepo;
    private ProjectRoleRepo projectRoleRepo;

    @Autowired
    public EmployeeOnProjectService(EmployeeOnProjectRepo employeeOnProjectRepo, EntityManager entityManager,
                                    EmployeeRepo employeeRepo, ProjectRepo projectRepo,
                                    ProjectRoleRepo projectRoleRepo) {
        this.employeeOnProjectRepo = employeeOnProjectRepo;
        this.entityManager = entityManager;
        this.employeeRepo = employeeRepo;
        this.projectRepo = projectRepo;
        this.projectRoleRepo = projectRoleRepo;
    }

    @Validated(EmployeeOnProjectRepo.class)
    public void addOrUpdate(@Valid EmployeeOnProjectDto employeeOnProjectDto, Principal principal) {
        String emailPrincipal = principal.getName();
        Employee empPrincipal = employeeRepo.findByEmail(emailPrincipal).orElseThrow();

        Employee repoByPhone = employeeRepo.findByPhone(employeeOnProjectDto.getEmpPhone());
        EmployeeOnProject empOnProjectByPhoneAndStartDate = employeeOnProjectRepo.getByEmployeePhone(repoByPhone.getPhone(),
                employeeOnProjectDto.getStartWorks());
        if (empOnProjectByPhoneAndStartDate == null) {
            empOnProjectByPhoneAndStartDate = new EmployeeOnProject().setEmployee(repoByPhone).setStartWorks(employeeOnProjectDto.getStartWorks());
        }

        if (employeeOnProjectDto.getEndWorks() != null) {
            empOnProjectByPhoneAndStartDate.setEndWorks(employeeOnProjectDto.getEndWorks());
        }

        if (employeeOnProjectDto.getProjectTitle() != null) {
            Project project = projectRepo.findByTitleIgnoreCase(employeeOnProjectDto.getProjectTitle()).orElseThrow();
            if (canDo(repoByPhone, empPrincipal, project)) {
                empOnProjectByPhoneAndStartDate.setProject(project);
            }
        }
        employeeOnProjectRepo.save(empOnProjectByPhoneAndStartDate);
    }

    public void updateProjectRole(@NotBlank String roleTitle, Long id, Principal principal) {
        String emailPrincipal = principal.getName();
        Employee empPrincipal = employeeRepo.findByEmail(emailPrincipal).orElseThrow();

        ProjectRole projectRole = projectRoleRepo.findByTitleIgnoreCase(roleTitle).get();
        EmployeeOnProject employeeOnProject = employeeOnProjectRepo.findById(id).get();

        if (employeeOnProject.getProject().getProjectManager().equals(empPrincipal)) {
            employeeOnProject.setProjectRole(projectRole);
        } else {
            throw new RuntimeException("Изменить роль сотрудника на проекте может только руководитель проекта");
        }

    }

    public void removeById(Long id) {
        employeeOnProjectRepo.deleteById(id);
    }

    public List<EmployeeOnProjectDto> findAll(Boolean isDeleted) {
        if (isDeleted == null) {
            isDeleted = false;
        }
        final Session session = entityManager.unwrap(Session.class);
        final Filter filter = session.enableFilter("deletedEmployeeOnProjectFilter");
        filter.setParameter("isDeleted", isDeleted);
        Iterable<EmployeeOnProject> projectRepoAll = employeeOnProjectRepo.findAll();
        session.disableFilter("deletedEmployeeOnProjectFilter");
        return convertIteratorToDto(projectRepoAll);
    }

    public List<EmployeeOnProjectDto> convertIteratorToDto(Iterable<EmployeeOnProject> employeeOnProjects) {
        final Iterator<EmployeeOnProject> iterator = employeeOnProjects.iterator();
        List<EmployeeOnProjectDto> projectDtos = new ArrayList<>();
        final String word = "Пока не задано";
        while (iterator.hasNext()) {
            EmployeeOnProject employee = iterator.next();
            EmployeeOnProjectDto employeeOnProjectDto = new EmployeeOnProjectDto().setStartWorks(employee.getStartWorks())
                    .setEmpPhone(employee.getEmployee().getPhone()).setEndWorks(employee.getEndWorks());
            employeeOnProjectDto.setProjectTitle(employee.getProject() == null ? word : employee.getProject().getTitle());
            employeeOnProjectDto.setProjectRoleTitle(employee.getProjectRole() == null ? word : employee.getProjectRole().getTitle());

            projectDtos.add(employeeOnProjectDto);
        }

        return projectDtos;
    }

    private boolean canDo(Employee employee, Employee empPrincipal, Project project) {

        if (containsProject(project, empPrincipal)) {
            return true;
        } else if (empPrincipal.getRole().contains(Role.MAINUSER)) {
            if (isEmployeeUnderManager(empPrincipal, employee)) {
                return true;
            } else {
                throw new RuntimeException("У вас нет прав для добавления этого сотрудника");
            }
        } else {
            throw new RuntimeException("Невозможно добавить сотрудника");
        }
    }

    private static boolean containsProject(Project project, Employee projectManager) {
        return projectManager.getProjects().contains(project);
    }
}
