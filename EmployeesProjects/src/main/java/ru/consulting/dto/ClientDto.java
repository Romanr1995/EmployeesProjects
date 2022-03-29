package ru.consulting.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ClientDto {

    private String title;

    private String email;

    private String phone;

    private List<String> projectTitle;
}
