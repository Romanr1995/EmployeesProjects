package ru.consulting.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.consulting.dto.EmployeeDto;
import ru.consulting.service.EmployeeService;
import ru.consulting.validated.OnCreate;
import ru.consulting.validated.OnUpdate;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.math.BigDecimal;
import java.security.Principal;
import java.time.LocalDate;
import java.util.List;

@Validated
@RestController
@RequestMapping("/employee")
public class EmployeeController {

    private EmployeeService employeeService;
//    @Autowired
//    private PasswordEncoder passwordEncoder;

    @Autowired
    public void setEmployeeService(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping
    public List<EmployeeDto> showAllEmployeeDtoWithFiltering(@RequestParam(required = false) Integer page,
                                                             @RequestParam(required = false) BigDecimal minSalary,
                                                             @RequestParam(required = false) BigDecimal maxSalary,
                                                             @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate after,
                                                             @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate before,
                                                             @RequestParam(required = false) String sort,
                                                             @RequestParam(required = false) Boolean direction,
                                                             @RequestParam(required = false) String department,
                                                             @RequestParam(required = false) String surname) {
        return employeeService.getAllEmployeeDtoWithFiltering(page, minSalary, maxSalary, after,
                before, sort, direction, department, surname);
    }

    @GetMapping("{id}")
    public ResponseEntity<EmployeeDto> showEmployeeDtoById(@PathVariable("id") Long id) {
        try {
            return new ResponseEntity<>(employeeService.getEmployeeDtoById(id), HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.MULTI_STATUS).build();
        }

    }

    @GetMapping("/count")
    public long showCountEmployees() {
        return employeeService.getCountEmployees();
    }

    @PreAuthorize("hasAuthority('employee:partial_write')")
    @Validated(OnCreate.class)
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Void> saveNew(@RequestBody @Valid EmployeeDto employeeDto) {
        employeeService.save(employeeDto);
        return ResponseEntity.status(201).build();
    }

    @PreAuthorize("hasAuthority('main_user_write')")
    @PostMapping("/salary/{id}")
    public void setOrUpdateSalary(@PathVariable Long id,@RequestParam BigDecimal salary, Principal principal) {
        employeeService.setSalary(id, salary, principal);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteById(@PathVariable("id") @NotNull Long id, Principal principal) {
        return employeeService.delete(id, principal);
    }

    @PreAuthorize("hasAuthority('employee:partial_write')")
    @Validated(OnUpdate.class)
    @PutMapping("/update")
    public void update(@RequestBody @Valid EmployeeDto employeeDto) {
        employeeService.update(employeeDto);
    }

//    @DeleteMapping("/removes")
//    public void deletes(@RequestBody List<Long> id) {
//        employeeService.removeList(id);
//    }

//    @DeleteMapping("/removes/map")
//    public void deletesByNameAndEmail(@RequestBody @NotEmpty Map<@NotBlank String, @Email String> namesAndEmails) {
//        employeeService.removesMap(namesAndEmails);
//    }

    @PutMapping("/update/department/{title}")
    public void updateDepartment(@PathVariable @NotBlank String title, @RequestParam @NotBlank String name,
                                 @RequestParam @NotBlank String surname, Principal principal) {
        employeeService.updateDepartment(title, name, surname, principal);
    }

    @PreAuthorize("hasAuthority('employee:partial_write')")
    @PostMapping("/insert/position/{title}")
    public void insertPosition(@PathVariable @NotBlank String title, @RequestParam @NotBlank @Pattern(regexp = "89[0-9]{9}") String phone,
                               @RequestParam(required = false) String email, Principal principal) {
        employeeService.addPosition(title, phone, email, principal);
    }

//    @PutMapping("/password")
//    public void editPassword(@RequestBody String password, Principal principal) {
//        employeeService.editPassword(passwordEncoder.encode(password), principal);
//    }

}
