package org.j2os.project.entity;

import lombok.*;

import javax.persistence.*;

@Entity(name = "person")
@Table(name = "person")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Person {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(columnDefinition = "NUMBER")
    private long id;

    @Column(columnDefinition = "VARCHAR2(20)")
    private String name;

    @Column(columnDefinition = "VARCHAR2(20)")
//    @Transient
    private String family;

    @Column(columnDefinition = "VARCHAR2(150)")
    private String city;

    @Version
    private int recordVersion;

    public Person(String name, String family, String city){
        this.name = name;
        this.family = family;
        this.city = city;
    }
}
