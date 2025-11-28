package org.j2os.project.entity;

import lombok.*;

import javax.persistence.*;

@Entity(name = "car")
@Table(name = "car")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@SequenceGenerator(name = "carSeq", sequenceName = "CAR_SEQ")
public class Car {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "carSeq")
    private long id;

    @Column(columnDefinition = "VARCHAR2(20)")
    private String model;
}
