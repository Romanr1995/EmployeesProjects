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

    @Null(message = "При добавлении Department id не задается")
    private Long id;

    //    @NotEmpty
    @NotBlank(message = "Title должно быть не пустым")
    //добавил,чтобы проверять,что строка не заполнена пробелами,например
    private String title;

    private String depHeadSurname;

    private String higherDepTitle;
}
