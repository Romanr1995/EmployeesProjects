package ru.consulting.dto;

import lombok.*;
import ru.consulting.validated.OnCreate;
import ru.consulting.validated.OnUpdate;

import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@ToString
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeDto {

    @NotNull(message = "id должен быть задан", groups = OnUpdate.class)
    @Null(message = "для создания id не требуется", groups = OnCreate.class)
    private Long id;

    @NotBlank(message = "Необходимо указать имя", groups = OnCreate.class)
    private String name;

    @NotBlank(message = "Необходимо указать фамилию", groups = OnCreate.class)
    private String surname;

    private String patronymic = "отчества нет";

    //    @Positive(message = "Зарплата должна быть больше 0")
    @Null(groups = {OnCreate.class, OnUpdate.class}, message = "Зарплата задается в другом методе")
    private BigDecimal salary;

    @Past(message = "Дата приёма на работу не должна быть позже текущей")
    @NotNull(message = "Необходимо обязательно задать дату устройства на работу.",
            groups = OnCreate.class)
    private LocalDate dateOfEmployment;

    @Email(regexp = ".+[@].+[\\.].+", message = "Неверный формат email")
    private String email;

    @Pattern(regexp = "89[0-9]{9}", message = "Телефонный номер должен начинаться с 89, затем - 9 цифр")
    private String phone;

}
