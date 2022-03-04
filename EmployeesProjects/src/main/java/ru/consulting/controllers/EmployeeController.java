package ru.consulting.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.consulting.dto.EmployeeDto;
import ru.consulting.service.EmployeeService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/employee")
public class EmployeeController {

    private EmployeeService employeeService;

    @Autowired
    public void setEmployeeService(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping
    public List<EmployeeDto> showAllEmployeeDto() {
        return employeeService.getAllEmployeeDto();
    }

    @GetMapping("{id}")
    public EmployeeDto showEmployeeDtoById(@PathVariable("id") Long id) {
        return employeeService.getEmployeeDtoById(id);
    }

    @GetMapping("/count")
    public long showCountEmployees() {
        return employeeService.getCountEmployees();
    }

    @RequestMapping(method = RequestMethod.POST)
    public void saveNew(@RequestBody EmployeeDto employeeDto) {
        employeeService.save(employeeDto);
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable("id") Long id) {
        employeeService.delete(id);
    }

    @PutMapping("update")
    public void update(@RequestBody EmployeeDto employeeDto) {
        employeeService.update(employeeDto);
    }

    @DeleteMapping("/removes")
    public void deletes(@RequestBody List<Long> id) {
        employeeService.removeList(id);
    }

    @DeleteMapping("/removes/map")
    public void deletesByNameAndEmail(@RequestBody Map<String, String> namesAndEmails) {
        employeeService.removesMap(namesAndEmails);
    }

    @PutMapping("/update/department/{title}")
    public void updateDepartment(@PathVariable String title, @RequestParam String name,
                                 @RequestParam String surname) {
        employeeService.updateDepartment(title, name, surname);
    }

}
