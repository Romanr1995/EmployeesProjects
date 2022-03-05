package ru.consulting.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PositionDto {

    private Long id;

    @Size(min = 2, max = 25)
    @NotBlank
    private String title;
    @NotEmpty
    @Size(min = 2)
    private String[] jobFunction;
}
