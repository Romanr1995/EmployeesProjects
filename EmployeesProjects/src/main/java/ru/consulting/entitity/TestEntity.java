package ru.consulting.entitity;

import lombok.*;

import javax.persistence.*;

@Setter
@Getter
@Entity
@EqualsAndHashCode
@ToString
@Table(name = "test", schema = "public", indexes = @Index(name = "title", columnList = "string,number", unique = true))
@NoArgsConstructor
public class TestEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "seq_generator")
    @SequenceGenerator(
            name = "seq_generator",
            initialValue = 2,
            allocationSize = 3,
            schema = "public"
    )
    private Long id;


    @Column(insertable = false, unique = false)
    private String string;


    @Column(insertable = false)
    private Integer number;


    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, optional = false,
            targetEntity = Position.class)
    @JoinColumns({@JoinColumn(name = "position_id", referencedColumnName = "id"),
            @JoinColumn(name = "position_title", referencedColumnName = "title")})
    private Position position;

}
