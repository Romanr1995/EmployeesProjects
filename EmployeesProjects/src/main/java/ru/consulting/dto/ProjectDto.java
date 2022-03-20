package ru.consulting.dto;

import lombok.*;

import java.time.LocalDate;

@ToString(exclude = "id")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ProjectDto {

    private Long id;

    private String title;

    private LocalDate start;

    private LocalDate plannedEnding;

    private LocalDate ending;

    private String projectManagerSurname;

    private String clientTitle;
}
