package ru.consulting.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Null;

@ToString
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class DepartmentDto {

    @Null
    private Long id;

    //    @NotEmpty
    @NotBlank(message = "Title необходимо обязательно")
    //добавил,чтобы проверять,что строка не заполнена пробелами,например
    private String title;
}
