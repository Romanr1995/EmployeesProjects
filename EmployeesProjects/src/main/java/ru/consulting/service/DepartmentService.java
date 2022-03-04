package ru.consulting.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.consulting.dto.DepartmentDto;
import ru.consulting.entitity.Department;
import ru.consulting.entitity.Employee;
import ru.consulting.repositories.DepartmentRepo;
import ru.consulting.repositories.EmployeeRepo;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class DepartmentService {

    private DepartmentRepo departmentRepo;
    private EmployeeRepo employeeRepo;

    @Autowired
    public DepartmentService(DepartmentRepo departmentRepo, EmployeeRepo employeeRepo) {
        this.departmentRepo = departmentRepo;
        this.employeeRepo = employeeRepo;
    }

    public void save(DepartmentDto departmentDto) {
        departmentRepo.save(convertDepartmentDtoToDepartment(departmentDto));
    }

    public List<DepartmentDto> getAll() {
        List<DepartmentDto> departmentDtos = new ArrayList<>();
//        Iterator<Department> departmentIterator = departmentRepo.findAll().iterator();
//        while (departmentIterator.hasNext()) {
//            departmentDtos.add(convertDepartmentToDepartmentDto(departmentIterator.next()));
//        }
        return departmentRepo.getAllOrderById().stream().map(this::convertDepartmentToDepartmentDto)
                .collect(Collectors.toList());
//        return departmentDtos;
    }

    public void removeById(Long id) {
        departmentRepo.deleteById(id);
    }

    public void saveAll(List<DepartmentDto> departmentDtos) {
        List<Department> departmentList = departmentDtos.stream().map(this::convertDepartmentDtoToDepartment).collect(Collectors.toList());
        departmentRepo.saveAll(departmentList);
    }

    public void updateDepartmentHead(Long depId, String phone, String email) {
        Department department = departmentRepo.findById(depId).orElseThrow(() ->
                new RuntimeException("Department с id: " + depId + " не найден"));
        Employee depHead;
        if (Objects.nonNull(email)) {
            depHead = employeeRepo.findByPhoneOrEmailIgnoreCase(phone, email).orElseThrow(() ->
                    new RuntimeException("Employee с phone: " + phone + " и email: " + email + " не найден"));
        } else {
            depHead = employeeRepo.findByPhone(phone);
        }
        department.setDepartmentHead(depHead);
        departmentRepo.save(department);
    }

    public Department convertDepartmentDtoToDepartment(DepartmentDto departmentDto) {
        return new Department().setTitle(departmentDto.getTitle());
    }

    public DepartmentDto convertDepartmentToDepartmentDto(Department department) {
        return new DepartmentDto().setId(department.getId()).setTitle(department.getTitle());
    }
}
