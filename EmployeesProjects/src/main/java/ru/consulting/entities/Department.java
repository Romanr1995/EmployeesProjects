package ru.consulting.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "departments")
public class Department {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "title", unique = true, nullable = false)
    private String title;

    @OneToOne
    @JoinColumn(name = "departmentHead_id")
    private Employee departmentHead;

    @OneToOne
    @JoinColumn(name = "higherDepartment_id")
    private Department higherDepartment;

    @OneToMany(mappedBy = "department")
    private List<Employee> employeesOfDepartment;
}
