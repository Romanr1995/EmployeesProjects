package ru.consulting.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ProjectRoleDto {

    private String title;

    private Map<String, String> empSurnameAndProjectTitle;

    public ProjectRoleDto(String title) {
        this.title = title;
    }
}
