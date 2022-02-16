package ru.consulting.entitity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;

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
    private Long id;

    @Column(nullable = false, length = 50)
    private String name;

    @Column(nullable = false, length = 50)
    private String surname;

    @Value("${default.patronymic}")
    @Column(columnDefinition = "varchar(50) DEFAULT 'отсутствует'")
    private String patronymic;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal salary;

    @Column(nullable = false)
    private LocalDate dateOfEmployment;

    @Column(columnDefinition = "varchar(30) NOT NULL UNIQUE")
    private String email;

    @Column(unique = true,nullable = false,columnDefinition = "varchar(11)")
    private String phone;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id")
    private Department department;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "position_id")
    private Position position;

    @OneToMany(mappedBy = "projectManager", cascade = {CascadeType.REFRESH, CascadeType.MERGE, CascadeType.PERSIST})
    private List<Project> projects;

    @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL)
    private List<EmployeeOnProject> employeeOnProjects;

}
