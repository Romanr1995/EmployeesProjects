package ru.consulting.dto;

import ru.consulting.entitity.Employee;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface EmployeeProjection {

    Long getId();

    String getName();

    String getSurname();

    String getPatronymic();

    BigDecimal getSalary();

    LocalDate getDateOfEmployment();

    String getEmail();

    String getPhone();

}
