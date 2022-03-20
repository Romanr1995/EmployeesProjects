package ru.consulting.entitity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Loader;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import javax.validation.constraints.Pattern;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "Clients")
@SQLDelete(sql =
        "UPDATE clients " +
                "SET deleted = true " +
                "WHERE id = ?")
@Loader(namedQuery = "findClientsById")
@NamedQuery(name = "findClientsById", query =
        "SELECT c " +
                "FROM Clients c " +
                "WHERE " +
                "  c.id = ?1 AND " +
                "  c.deleted = false")
@Where(clause = "deleted = false")
@Table(name = "clients")
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Boolean deleted = false;

    @Column(unique = true, nullable = false)
    private String title;

    @Column(unique = true, nullable = false, length = 30)
    private String email;

    @Column(unique = true, nullable = false)
    @Pattern(regexp = "89[0-9]{9}", message = "Номер телефона должен состоять из 11 цифр и начинаться с 89")
    private String phone;


    @OneToMany(mappedBy = "client", fetch = FetchType.LAZY)
    private List<Project> projects;

}
