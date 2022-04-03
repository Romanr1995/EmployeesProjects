package ru.consulting.entitity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import ru.consulting.entitity.security.Role;
import ru.consulting.entitity.security.Status;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
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

    @Column(columnDefinition = "varchar(50)")
    private String patronymic = "Отчества нет";

    @Column(precision = 10, scale = 2)
    private BigDecimal salary;

    @Column(nullable = false)
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate dateOfEmployment;

    @Column(columnDefinition = "varchar(30) NOT NULL UNIQUE")
    private String email;

    @Column(unique = true, nullable = false, columnDefinition = "varchar(11)")
    private String phone;

    @Column(name = "password")
    private String password = "$2a$10$i/mWpPNpx1YDCWtdj6yefubKLr3ZUZej7eZdmQ.PmsC6mHSVPFE9q";

    @Enumerated(value = EnumType.STRING)
    @Column(name = "status")
    private Status status = Status.ACTIVE;

    @ElementCollection(targetClass = Role.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "employees_roles", joinColumns = @JoinColumn(name = "employee_id"),
            uniqueConstraints = @UniqueConstraint(columnNames = "role"))
    @Enumerated(EnumType.STRING)
    private Set<Role> role = Set.of(Role.USER);

    public Set<SimpleGrantedAuthority> getAllAuthorities() {
        Set<SimpleGrantedAuthority> simpleGrantedAuthorities = new HashSet<>();
        role.forEach(rol -> simpleGrantedAuthorities.addAll(rol.getAuthorities()));
        return simpleGrantedAuthorities;
    }

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "department_id")
    private Department department;

    @OneToOne(mappedBy = "departmentHead")
    private Department underManagement;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "position_id")
    private Position position;

    @OneToMany(mappedBy = "projectManager", cascade = {CascadeType.REFRESH, CascadeType.MERGE, CascadeType.PERSIST})
    private List<Project> projects;

    @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL)
    private List<EmployeeOnProject> employeeOnProjects;

    public Employee(String name, String surname, String patronymic, BigDecimal salary, LocalDate dateOfEmployment,
                    String email, String phone) {
        this.name = name;
        this.surname = surname;
        this.patronymic = patronymic;
        this.salary = salary;
        this.dateOfEmployment = dateOfEmployment;
        this.email = email;
        this.phone = phone;
    }
}
