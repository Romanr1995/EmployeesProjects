package ru.consulting.repositories.specifications;

import org.springframework.data.jpa.domain.Specification;
import ru.consulting.entitity.Department;
import ru.consulting.entitity.Employee;

import javax.persistence.criteria.Join;
import java.math.BigDecimal;
import java.time.LocalDate;

public class EmployeeSpecifications {

    public static Specification<Employee> minSalary(BigDecimal salary) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.greaterThanOrEqualTo(root.get("salary"), salary);
    }

    public static Specification<Employee> maxSalary(BigDecimal salary) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.lessThanOrEqualTo(root.get("salary"), salary);
    }

    public static Specification<Employee> dateAfter(LocalDate localDate) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.greaterThanOrEqualTo(root.get("dateOfEmployment"), localDate);
    }

    public static Specification<Employee> dateBefore(LocalDate localDate) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.lessThanOrEqualTo(root.get("dateOfEmployment"), localDate);
    }

    public static Specification<Employee> departmentTitle(String title) {
        return (root, query, criteriaBuilder) -> {
            Join<Employee, Department> employeeDepartmentJoin = root.join("department");
            return criteriaBuilder.equal(employeeDepartmentJoin.get("title"), title);
        };
    }

    public static Specification<Employee> departmentHeadSurname(String surname) {
        return (root, query, criteriaBuilder) -> {
            Join<Employee, Department> employeeDepartmentJoin = root.join("department");
            Join<Department, Employee> departmentHead = employeeDepartmentJoin.join("departmentHead");
            return criteriaBuilder.equal(departmentHead.get("surname"), surname);
        };
    }
}
