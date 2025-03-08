package com.creditapp.CreditManagementApp.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "transaction")
@Data
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer transactionId;

    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    @JsonIgnore
    private Customer customer;

    private String itemName;

    private String quantity;

    private Double amount;

    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime date;

    @Enumerated(EnumType.STRING)
    private TransactionStatus status;

}
