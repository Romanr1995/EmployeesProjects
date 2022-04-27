package ru.consulting.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.consulting.dto.DepartmentDto;
import ru.consulting.entitity.Department;
import ru.consulting.entitity.Employee;
import ru.consulting.exception_handling.NoSuchEntityException;
import ru.consulting.repositories.DepartmentRepo;
import ru.consulting.repositories.EmployeeRepo;

import java.util.*;
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
        if (departmentRepo.existsById(id)) {
            departmentRepo.deleteById(id);
        } else {
            throw new NoSuchEntityException(id, Department.class);
        }
    }

//    @Transactional(noRollbackFor = ConstraintViolationException.class)
//    public void saveAll(List<DepartmentDto> departmentDtos) {
//        List<Department> departmentList = departmentDtos.stream().map(this::convertDepartmentDtoToDepartment).collect(Collectors.toList());
//        departmentRepo.saveAll(departmentList);
//    }

    public void updateDepartmentHead(Long depId, String phone, String email) {
        Department department = departmentRepo.findById(depId).orElseThrow(() ->
                new NoSuchEntityException(depId, Department.class));
        Employee depHead;
        if (Objects.nonNull(email)) {
            depHead = employeeRepo.findByPhoneOrEmailIgnoreCase(phone, email).orElseThrow(() ->
                    new NoSuchEntityException("Employee с phone: " + phone + " и email: " + email + " не найден"));
        } else {
            depHead = employeeRepo.findByPhone(phone);
            if (depHead == null) {
                throw new NoSuchEntityException("Employee с phone: " + phone + " не найден");
            }
        }
        department.setDepartmentHead(depHead);
        departmentRepo.save(department);
    }

    public void setHigherDepartment(Map<String, DepartmentDto> departmentDtoMap) {
        Department higherDepartment;
        Department department;

        DepartmentDto employeeDtoHigherDepartment = departmentDtoMap.get("higherDepartment");
        DepartmentDto employeeDtoDepartment = departmentDtoMap.get("department");
        if (employeeDtoHigherDepartment == null || employeeDtoDepartment == null) {
            throw new RuntimeException("Ключи для Departments заданы не верно!Необходимо использовать 2 " +
                    " ключа: department для Department, которому задается вышестоящий Department, " +
                    "и higherDepartment для выщестоящего Department");
        }
        Optional<Department> departmentOptional = departmentRepo.findByIdOrTitleIgnoreCase(employeeDtoHigherDepartment.getId(),
                employeeDtoHigherDepartment.getTitle());
        if (departmentOptional.isPresent()) {
            higherDepartment = departmentOptional.get();
        } else {
            if (employeeDtoHigherDepartment.getTitle() == null) {
                throw new NoSuchEntityException("Ошибка.Такой Department еще не существует." +
                        "Для создания нового Department должно обязательно быть задано title.");
            } else {
                higherDepartment = departmentRepo.save(convertDepartmentDtoToDepartment(employeeDtoHigherDepartment));
            }
        }

        department = departmentRepo.findByIdOrTitleIgnoreCase(employeeDtoDepartment.getId(),
                employeeDtoDepartment.getTitle()).orElseThrow(() -> new NoSuchEntityException("Department не существует"));

        department.setHigherDepartment(higherDepartment);
        departmentRepo.save(department);
    }

    public static List<Department> recursionDepartment(Department department) {

        if (department.getSubDepartments().size() == 0) {
            return List.of();
        } else {
            final List<Department> subDepartments = department.getSubDepartments();
            List<Department> result = new ArrayList<>(subDepartments);
            for (Department subDepartment : subDepartments) {
                final List<Department> departments = recursionDepartment(subDepartment);
                result.addAll(departments);
            }
            return result;
        }
    }

    public Department convertDepartmentDtoToDepartment(DepartmentDto departmentDto) {
        return new Department().setTitle(departmentDto.getTitle());
    }

    public DepartmentDto convertDepartmentToDepartmentDto(Department department) {
        final Employee departmentHead = department.getDepartmentHead();
        final Department higherDepartment = department.getHigherDepartment();
        String depHeadSurname = (departmentHead == null) ? "не задан" : departmentHead.getSurname();
        String higherDepTitle = (higherDepartment == null) ? "не задан" : higherDepartment.getTitle();
        return new DepartmentDto().setId(department.getId()).setTitle(department.getTitle())
                .setDepHeadSurname(depHeadSurname)
                .setHigherDepTitle(higherDepTitle);
    }
}
