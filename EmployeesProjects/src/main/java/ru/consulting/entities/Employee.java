package ru.consulting.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "employees")
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "surname", nullable = false)
    private String surname;

    @Column(name = "patronymic")
    private String patronymic;

    @Column(name = "salary", nullable = false)
    private BigDecimal salary;

    @Temporal(TemporalType.DATE)
    @Column(name = "dateOfEmployment")
    private LocalDate dateOfEmployment;

    @Column(name = "email", unique = true, nullable = false)
    private String email;

    @Column(name = "phone", unique = true, length = 11, nullable = false)
    private String phone;

    @ManyToOne
    @JoinColumn(name = "department_id")
    private Department department;

    @ManyToOne
    @JoinColumn(name = "position_id")
    private Position position;

    @OneToMany(mappedBy = "projectManager")
    private List<Project> projects;

    @OneToMany(mappedBy = "employee")
    private List<EmployeeOnProject> employeeOnProjects;

}
