package ru.consulting.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import ru.consulting.dto.ProjectRoleDto;
import ru.consulting.entitity.ProjectRole;
import ru.consulting.service.ProjectRoleService;

@RestController
@RequestMapping("${project.role.api}")
public class ProjectRoleController {
    private ProjectRoleService projectRoleService;

    @Autowired
    public ProjectRoleController(ProjectRoleService projectRoleService) {
        this.projectRoleService = projectRoleService;
    }


    @RequestMapping(method = RequestMethod.POST)
    public void add(@RequestBody ProjectRole projectRole) {
        projectRoleService.addNewProjectRole(projectRole);
    }

    @DeleteMapping(value = "{title}")
    public void deleteByTitle(@PathVariable String title) {
        projectRoleService.deleteByTitle(title);
    }

    @GetMapping
    public Iterable<ProjectRoleDto> showAll(@RequestParam(required = false) Boolean isDeleted) {
        return projectRoleService.findAll(isDeleted);
    }
}
