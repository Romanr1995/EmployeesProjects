package ru.consulting.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.consulting.dto.DepartmentDto;
import ru.consulting.entitity.Department;
import ru.consulting.entitity.Employee;
import ru.consulting.repositories.DepartmentRepo;
import ru.consulting.repositories.EmployeeRepo;

import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
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
        return departmentRepo.getAllOrderById().stream().map(this::convertDepartmentToDepartmentDto)
                .collect(Collectors.toList());
    }

    public void removeById(Long id) {
        if (departmentRepo.existsById(id)){
            departmentRepo.deleteById(id);
        } else {
            throw new RuntimeException("Пользователь с id: " + id + " не найден.");
        }
    }

    @Transactional(noRollbackFor = ConstraintViolationException.class)
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

    public void setHigherDepartment(Map<String, DepartmentDto> departmentDtoMap) {
        Department higherDepartment;
        Department department = null;

        DepartmentDto employeeDtoHigherDepartment = departmentDtoMap.get("higherDepartment");
        Optional<Department> departmentOptional = departmentRepo.findByIdOrTitleIgnoreCase(employeeDtoHigherDepartment.getId(),
                employeeDtoHigherDepartment.getTitle());
        if (departmentOptional.isPresent()) {
            higherDepartment = departmentOptional.get();
        } else {
            if (employeeDtoHigherDepartment.getTitle() == null) {
                throw new RuntimeException("Ошибка.Такой Department еще не существует." +
                        "Для создания нового Department должно обязательно быть задано title.");
            } else {
                higherDepartment = departmentRepo.save(convertDepartmentDtoToDepartment(employeeDtoHigherDepartment));
            }
        }
        DepartmentDto employeeDepartment = departmentDtoMap.get("department");
        department = departmentRepo.findByIdOrTitleIgnoreCase(employeeDepartment.getId(),
                employeeDepartment.getTitle()).orElseThrow(() -> new RuntimeException("Department не существует"));

        department.setHigherDepartment(higherDepartment);
        departmentRepo.save(department);
    }

    public Department convertDepartmentDtoToDepartment(DepartmentDto departmentDto) {
        return new Department().setTitle(departmentDto.getTitle());
    }

    public DepartmentDto convertDepartmentToDepartmentDto(Department department) {
        return new DepartmentDto().setId(department.getId()).setTitle(department.getTitle());
    }
}
