package ru.consulting.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.consulting.dto.EmployeeDto;
import ru.consulting.service.EmployeeService;
import ru.consulting.validated.OnCreate;
import ru.consulting.validated.OnUpdate;

import javax.validation.Valid;
import javax.validation.constraints.*;
import java.util.List;
import java.util.Map;

@Validated
@RestController
@RequestMapping("/employee")
public class EmployeeController {

    private EmployeeService employeeService;

    @Autowired
    public void setEmployeeService(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping
    public List<@Valid EmployeeDto> showAllEmployeeDto() {
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

    @Validated(OnCreate.class)
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Void> saveNew(@RequestBody @Valid EmployeeDto employeeDto) {
        employeeService.save(employeeDto);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable("id") @NotNull Long id) {
        employeeService.delete(id);
    }

    @Validated(OnUpdate.class)
    @PutMapping("update")
    public void update(@RequestBody @Valid EmployeeDto employeeDto) {
        employeeService.update(employeeDto);
    }

    @DeleteMapping("/removes")
    public void deletes(@RequestBody List<Long> id) {
        employeeService.removeList(id);
    }

    @DeleteMapping("/removes/map")
    public void deletesByNameAndEmail(@RequestBody @NotEmpty Map<@NotBlank String, @Email String> namesAndEmails) {
        employeeService.removesMap(namesAndEmails);
    }

    @PutMapping("/update/department/{title}")
    public void updateDepartment(@PathVariable @NotBlank String title, @RequestParam @NotBlank String name,
                                 @RequestParam @NotBlank String surname) {
        employeeService.updateDepartment(title, name, surname);
    }

    @PostMapping("/insert/position/{title}")
    public void insertPosition(@PathVariable @NotBlank String title, @RequestParam @NotBlank @Pattern(regexp = "89[0-9]{9}") String phone,
                               @RequestParam(required = false) String email) {
        employeeService.addPosition(title, phone, email);
    }

}
