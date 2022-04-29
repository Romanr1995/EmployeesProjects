package ru.consulting.entitity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.*;
import javax.validation.constraints.AssertFalse;
import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
//@SQLDelete(sql =
//        "UPDATE projects " +
//                "SET deleted = true " +
//                "WHERE id = ?")
@Loader(namedQuery = "findProjectsById")
@NamedQuery(name = "findProjectsById", query =
        "SELECT p " +
                "FROM Projects p " +
                "WHERE " +
                "  p.id = ?1 AND " +
                "  p.deleted = false")
@Where(clause = "deleted = false")
@Entity(name = "Projects")
@Table(name = "projects")
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @AssertFalse
    private Boolean deleted = false;

    @Column(unique = true, nullable = false)
    private String title;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @Column(nullable = false)
    private LocalDate start;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @Column(name = "plannedEnding", nullable = false)
    private LocalDate plannedEnding;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @Column(name = "ending")
    private LocalDate ending;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "projectManager_id", foreignKey = @ForeignKey(name = "MyKey",
            foreignKeyDefinition = "FOREIGN KEY (project_manager_id) REFERENCES employees (id) On Delete SET Null"))
    private Employee projectManager;

    @ManyToOne(fetch = FetchType.LAZY)
    @NotFound(action = NotFoundAction.IGNORE)
    @JoinColumn(name = "client_id", foreignKey = @ForeignKey(name = "ClientKey",
            foreignKeyDefinition = "FOREIGN KEY (client_id) REFERENCES clients (id) On Delete SET Null"))
    private Client client;

    @OneToMany(mappedBy = "project")
    private List<EmployeeOnProject> employeeOnProjects;

    public Project(String title) {
        this.title = title;
    }

    public Project(String title, LocalDate start, LocalDate plannedEnding,
                   LocalDate ending, Employee projectManager, Client client) {
        this.title = title;
        this.start = start;
        this.plannedEnding = plannedEnding;
        this.ending = ending;
        this.projectManager = projectManager;
        this.client = client;
    }
}
