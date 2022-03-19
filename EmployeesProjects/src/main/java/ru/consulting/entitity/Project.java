package ru.consulting.entitity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "projects")
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

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

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.REFRESH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.DETACH})
    @JoinColumn(name = "client_id")
    private Client client;

    @OneToMany(mappedBy = "project")
    private List<EmployeeOnProject> employeeOnProjects;
}
