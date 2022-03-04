package ru.consulting.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.consulting.dto.DepartmentDto;
import ru.consulting.service.DepartmentService;

import java.util.List;

@RestController
@RequestMapping("department")
public class DepartmentController {

    private DepartmentService departmentService;

    @Autowired
    public void setDepartmentService(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    @RequestMapping(method = RequestMethod.POST, name = "save")
    public void saveNew(@RequestBody DepartmentDto departmentDto) {
        departmentService.save(departmentDto);
    }

    @GetMapping
    public List<DepartmentDto> showAllDepartment() {
        return departmentService.getAll();
    }

    @DeleteMapping("{id}")
    public void deleteById(@PathVariable Long id) {
        departmentService.removeById(id);
    }

    @PostMapping("/save/list")
    public void saveNewList(@RequestBody List<DepartmentDto> departmentDtos) {
        departmentService.saveAll(departmentDtos);
    }

    @PutMapping("update/departmenthead/{id}")
    public void updateDepartmentHead(@PathVariable Long id, @RequestParam String phone,
                                     @RequestParam(required = false) String email) {
        departmentService.updateDepartmentHead(id, phone, email);
    }
}
