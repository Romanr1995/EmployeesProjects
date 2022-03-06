package ru.consulting.repositories.specifications;

import org.springframework.data.jpa.domain.Specification;
import ru.consulting.entitity.Employee;

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
}
