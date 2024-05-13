package com.example.api.entity;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of = "id")
@Entity
@Table(name = "transactions")
public class Transaction implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    //@ManyToOne
    //@JoinColumn(name = "to_account_number")

    private Long toAccountNumber;
    //@ManyToOne
    //@JoinColumn(name = "from_account_number")
    private Long fromAccountNumber;
    @Column(name = "transaction_value")
    private Double value;
    @Column(name = "transaction_date")
    private LocalDateTime date;
    private String status;
}
