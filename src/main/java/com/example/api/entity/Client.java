package com.example.api.entity;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
/**
 * Class responsible for the structure of the client entity, mapped to the table "client".
 *  * Annotations:
 *  * @AllArgsConstructor: Generates a constructor with 1 parameter for each field in your class. Fields are initialized in the order they are declared.
 *  * @NoArgsConstructor: Generates a no-argument constructor.
 *  * @Getter: Generates getters.
 *  * @Setter: Generates setters.
 *  * @EqualsAndHashCode: Generates hashCode and equals implementations from the id field.
 *  * @Entity: Specifies that the class is an entity and is mapped to a database table.
 *  * @Table: Specifies the name of the database table to be used for mapping.
 *  */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of = "id")
@Entity
@Table(name = "client")
public class Client implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //auto increment
    private Long id;
    @Column(nullable = false)
    private String name;
    @Column(unique = true, nullable = false)
    private Integer accountNumber;
    @Column(nullable = false, columnDefinition = "double default 0")
    private Double balance;
}
