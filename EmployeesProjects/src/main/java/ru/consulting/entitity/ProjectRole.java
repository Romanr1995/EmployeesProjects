package ru.consulting.entitity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "projectsRoles")
public class ProjectRole {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String title;

    @OneToMany(mappedBy = "projectRole", orphanRemoval = true, fetch = FetchType.LAZY,
            cascade = CascadeType.ALL)
    private List<EmployeeOnProject> employeeOnProjects;
}
