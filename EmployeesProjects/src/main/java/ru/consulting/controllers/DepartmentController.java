package ru.consulting.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.consulting.dto.DepartmentDto;
import ru.consulting.service.DepartmentService;
import ru.consulting.validated.MapValidConstraint;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.List;
import java.util.Map;

@Validated
@RestController
@RequestMapping("department")
public class DepartmentController {

    private DepartmentService departmentService;

    @Autowired
    public void setDepartmentService(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    @RequestMapping(method = RequestMethod.POST)
    public void saveNew(@RequestBody @Valid DepartmentDto departmentDto) {
        departmentService.save(departmentDto);
    }

    @GetMapping
    public List<DepartmentDto> showAllDepartment() {
        return departmentService.getAll();
    }

    @DeleteMapping("{id}")
    public void deleteById(@PathVariable @NotNull Long id) {
        departmentService.removeById(id);
    }

    @PostMapping("/save/list")
    public void saveNewList(@RequestBody List<@Valid DepartmentDto> departmentDtos) {
        departmentService.saveAll(departmentDtos);
    }

    @PutMapping("update/departmenthead/{id}")
    public void updateDepartmentHead(@PathVariable Long id,
                                     @RequestParam @Pattern(regexp = "89[0-9]{9}", message = "Телефонный номер должен начинаться с 89, затем - 9 цифр")
                                             String phone,
                                     @RequestParam(required = false) @Email String email) {
        departmentService.updateDepartmentHead(id, phone, email);
    }

    @PostMapping("savehigherdepartment")
    public void setHigherDepartment(@RequestBody @MapValidConstraint Map<String, DepartmentDto> departmentDtoMap) {
        departmentService.setHigherDepartment(departmentDtoMap);
    }
}
