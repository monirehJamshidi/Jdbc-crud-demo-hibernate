package org.j2os.project.entity;

import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;

@Entity(name = "person")
@Table(name = "person")
@SQLDelete(sql = "UPDATE person SET is_deleted = 1 WHERE id = ? AND recordVersion = ?")
@Where(clause = "is_deleted = 0")
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

    @Column(name = "is_deleted", nullable = false, columnDefinition = "NUMBER(1) DEFAULT 0")
    private boolean deleted = false;

    public Person(String name, String family, String city){
        this.name = name;
        this.family = family;
        this.city = city;
    }
}
