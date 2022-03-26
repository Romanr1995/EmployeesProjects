package ru.consulting.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import ru.consulting.entitity.EmployeeOnProject;
import ru.consulting.service.EmployeeOnProjectService;

@RestController
@RequestMapping("${emp.project.api}")
public class EmployeeOnProjectController {

    private EmployeeOnProjectService employeeOnProjectService;

    @Autowired
    public EmployeeOnProjectController(EmployeeOnProjectService employeeOnProjectService) {
        this.employeeOnProjectService = employeeOnProjectService;
    }


    @PostMapping
    public void addNew(@RequestBody EmployeeOnProject employee) {
        employeeOnProjectService.add(employee);
    }

    @DeleteMapping("{id}")
    public void delete(@PathVariable Long id) {
        employeeOnProjectService.removeById(id);
    }


    @GetMapping
    public Iterable<EmployeeOnProject> showAll(@RequestParam(required = false) Boolean isDeleted) {
        return employeeOnProjectService.findAll(isDeleted);
    }

}
