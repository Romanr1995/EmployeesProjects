package ru.consulting.entitity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.List;

@OnDelete(action = OnDeleteAction.NO_ACTION)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "departments")
public class Department {

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE,
            generator = "dep_generator")
    @TableGenerator(name = "dep_generator", allocationSize = 3, initialValue = 20, table = "dep_table",
            pkColumnName = "dep_id", valueColumnName = "dep_count")
    private Long id;

    @Column(unique = true, nullable = false, length = 30)
    private String title;

    @OneToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.REFRESH, CascadeType.MERGE})
    @JoinColumn(name = "departmentHead_id")
    private Employee departmentHead;

    @OneToOne()
    @JoinColumn(name = "higherDepartment_id")
    private Department higherDepartment;

    @OneToMany(mappedBy = "department", fetch = FetchType.LAZY)
    private List<Employee> employeesOfDepartment;

}
