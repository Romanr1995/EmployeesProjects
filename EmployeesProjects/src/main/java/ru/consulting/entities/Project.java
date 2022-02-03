package ru.consulting.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    @Column(name = "id")
    private Long id;

    @Column(name = "title", unique = true, nullable = false)
    private String title;

    @Column(name = "start", nullable = false)
    @Temporal(TemporalType.DATE)
    private LocalDate start;

    @Column(name = "plannedEnding")
    @Temporal(TemporalType.DATE)
    private LocalDate plannedEnding;

    @Column(name = "ending")
    @Temporal(TemporalType.DATE)
    private LocalDate ending;

    @ManyToOne
    @JoinColumn(name = "projectManager_id")
    private Employee projectManager;

    @ManyToOne
    @JoinColumn(name = "client_id")
    private Client client;

    @OneToMany(mappedBy = "project")
    private List<EmployeeOnProject> employeeOnProjects;
}
