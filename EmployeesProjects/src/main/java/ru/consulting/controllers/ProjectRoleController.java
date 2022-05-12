package ru.consulting.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.consulting.dto.ProjectRoleDto;
import ru.consulting.entitity.ProjectRole;
import ru.consulting.service.ProjectRoleService;

@RestController
@RequestMapping("role/project")
public class ProjectRoleController {
    private ProjectRoleService projectRoleService;

    @Autowired
    public ProjectRoleController(ProjectRoleService projectRoleService) {
        this.projectRoleService = projectRoleService;
    }


    @PreAuthorize("hasAuthority('employee:write')")
    @RequestMapping(method = RequestMethod.POST)
    public void add(@RequestBody ProjectRole projectRole) {
        projectRoleService.addNewProjectRole(projectRole);
    }

    @PreAuthorize("hasAuthority('employee:write')")
    @DeleteMapping(value = "{title}")
    public void deleteByTitle(@PathVariable String title) {
        projectRoleService.deleteByTitle(title);
    }

    @PreAuthorize("hasAnyAuthority('employee:partial_write','project_write')")
    @GetMapping
    public Iterable<ProjectRoleDto> showAll(@RequestParam(required = false) Boolean isDeleted) {
        return projectRoleService.findAll(isDeleted);
    }
}
