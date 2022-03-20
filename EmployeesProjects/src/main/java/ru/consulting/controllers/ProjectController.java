package ru.consulting.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.consulting.dto.ProjectDto;
import ru.consulting.entitity.Project;
import ru.consulting.service.ProjectService;

@RestController
@RequestMapping("project")
public class ProjectController {

    private ProjectService projectService;

    @Autowired
    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @PostMapping
    public void save(@RequestBody Project project) {
        projectService.saveNew(project);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping
    public void deleteById(@RequestParam Long id) {
        projectService.remove(id);
    }

    @GetMapping("{id}")
    public ProjectDto showById(@PathVariable Long id) {
        return projectService.getById(id);
    }

    @GetMapping("show/{title}")
    public ResponseEntity<?> showByTitle(@PathVariable String title) {
        try {
            return ResponseEntity.ok(projectService.getByTitle(title));
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("addclient/{email}")
    public void addProject(@PathVariable String email, @RequestParam String title) {
        projectService.addClient(email, title);
    }
}
