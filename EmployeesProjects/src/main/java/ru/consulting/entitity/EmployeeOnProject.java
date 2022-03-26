package ru.consulting.entitity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.*;
import org.springframework.beans.factory.annotation.Value;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.*;
import javax.validation.constraints.AssertFalse;
import javax.validation.constraints.Past;
import java.time.LocalDate;

@Data
@SQLDelete(sql = "UPDATE employees_on_projects SET deleted = true WHERE id=?")
@FilterDef(name = "deletedEmployeeOnProjectFilter", parameters = @ParamDef(name = "isDeleted", type = "boolean"))
@Filter(name = "deletedEmployeeOnProjectFilter", condition = "deleted = :isDeleted")
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "employees_on_projects",
        uniqueConstraints = @UniqueConstraint(columnNames = {"employee_id", "project_id", "role_id"}))
public class EmployeeOnProject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @AssertFalse
    @Value("false")
    private boolean deleted;

    @Column(nullable = false)
    private LocalDate startWorks;

    @Past(message = "Дата окончания работы на проекте не должна быть позже сегодняшней")
    private LocalDate endWorks;

    @ManyToOne(fetch = FetchType.LAZY)
    @NotFound(action = NotFoundAction.IGNORE)
    @JoinColumn(name = "employee_id")
    private Employee employee;

    @ManyToOne(fetch = FetchType.LAZY)
    @NotFound(action = NotFoundAction.IGNORE)
    @JoinColumn(name = "project_id")
    private Project project;

    @ManyToOne(cascade = CascadeType.ALL)
    @NotFound(action = NotFoundAction.IGNORE)
    @JoinColumn(name = "role_id")
    private ProjectRole projectRole;

}
