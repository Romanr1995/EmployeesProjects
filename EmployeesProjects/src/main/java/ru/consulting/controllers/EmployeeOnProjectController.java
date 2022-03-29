package ru.consulting.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.consulting.dto.EmployeeOnProjectDto;
import ru.consulting.service.EmployeeOnProjectService;

import java.util.List;

@RestController
@RequestMapping("${emp.project.api}")
public class EmployeeOnProjectController {

    private EmployeeOnProjectService employeeOnProjectService;

    @Autowired
    public EmployeeOnProjectController(EmployeeOnProjectService employeeOnProjectService) {
        this.employeeOnProjectService = employeeOnProjectService;
    }

    @PreAuthorize("hasAuthority('project_write')")
    @PostMapping
    public void addNewOrUpdate(@RequestBody EmployeeOnProjectDto employeeOnProjectDto) {
        employeeOnProjectService.addOrUpdate(employeeOnProjectDto);
    }

    @PreAuthorize("hasAuthority('project_write')")
    @DeleteMapping("{id}")
    public void delete(@PathVariable Long id) {
        employeeOnProjectService.removeById(id);
    }

    @PreAuthorize("hasAnyAuthority('employee:partial_write','project_write')")
    @GetMapping
    public List<EmployeeOnProjectDto> showAll(@RequestParam(required = false) Boolean isDeleted) {
        return employeeOnProjectService.findAll(isDeleted);
    }

}
