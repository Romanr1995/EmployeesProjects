package ru.consulting.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.consulting.validated.EmpOnProjectAddOrUpdate;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import javax.validation.constraints.Past;
import java.time.LocalDate;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeOnProjectDto {

    @NotNull(message = "Для создания или обновления необходимо обязательно указать дату начала работы на проекте")
    private LocalDate startWorks;

    @Past(message = "Дата окончания работы на проекте не должна быть позже сегодняшней")
    private LocalDate endWorks;

    @NotNull(message = "Для создания или обновления обязательно необходимо внести телефон сотрудника")
    private String empPhone;

    @Null(groups = EmpOnProjectAddOrUpdate.class, message = "Для создания или обновления проектной роли " +
            "необходимо воспользоваться другим методом")
    private String projectTitle;

    private String projectRoleTitle;
}
