package ru.consulting.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.consulting.validated.OnCreate;
import ru.consulting.validated.OnUpdate;

import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeDto {

    @NotNull(message = "id должен быть задан", groups = OnUpdate.class)
    private Long id;

    @NotBlank(message = "Необходимо указать имя", groups = OnCreate.class)
    private String name;

    @NotBlank(message = "Необходимо указать фамилию", groups = OnCreate.class)
    private String surname;
    private String patronymic;

    @Positive(message = "Зарплата должна быть больше 0")
    private BigDecimal salary;

    @Past(message = "Дата приёма на работу не должна быть позже текущей")
    private LocalDate dateOfEmployment;

    @Email(message = "Email должен быть корректным адресом электронной почты")
    private String email;

    @Pattern(regexp = "89[0-9]{9}", message = "Телефонный номер должен начинаться с 89, затем - 9 цифр")
    private String phone;

}
