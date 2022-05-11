package ru.consulting.entitity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.ParamDef;
import org.hibernate.annotations.SQLDelete;

import javax.persistence.*;
import javax.validation.constraints.AssertFalse;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SQLDelete(sql = "UPDATE projects_roles SET deleted = true WHERE id=?")
@FilterDef(name = "deleteProjectRole", parameters = @ParamDef(name = "isDeleted", type = "boolean"))
@Filter(name = "deleteProjectRole", condition = "deleted = :isDeleted")
@Entity
@Table(name = "projects_roles")
public class ProjectRole {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @AssertFalse
    private boolean deleted = Boolean.FALSE;

    @Column(unique = true, nullable = false)
    private String title;

    @OneToMany(mappedBy = "projectRole",
            cascade = CascadeType.ALL)
    private List<EmployeeOnProject> employeeOnProjects;

    public ProjectRole(String title) {
        this.title = title;
    }
}
