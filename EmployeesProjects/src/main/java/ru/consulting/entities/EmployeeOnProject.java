package ru.consulting.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "employees_on_projects",
        uniqueConstraints = @UniqueConstraint(columnNames = {"employee_id", "project_id", "role_id"}))
public class EmployeeOnProject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "startWorks")
    @Temporal(TemporalType.DATE)
    private LocalDate startWorks;

    @Column(name = "endWorks")
    @Temporal(TemporalType.DATE)
    private LocalDate endWorks;

    @ManyToOne
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    @ManyToOne
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    @ManyToOne()
    @JoinColumn(name = "role_id", nullable = false)
    private ProjectRole projectRole;

}
