package ru.consulting.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import javax.validation.constraints.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeDto {

    private Long id;

    @NotBlank(message = "Необходимо указать имя")
    private String name;

    @NotBlank(message = "Необходимо указать имя")
    private String surname;
    private String patronymic;

    @Positive(message = "Зарплата должна быть больше 0")
    private BigDecimal salary;

    @Past(message = "Дата приёма на работу не должна быть раньше текущей")
    private LocalDate dateOfEmployment;

    @Email(message = "Email должен быть корректным адресом электронной почты")
    private String email;

    @Pattern(regexp = "89[0-9]{9}", message = "Телефонный номер должен начинаться с 89, затем - 9 цифр")
    private String phone;

}
