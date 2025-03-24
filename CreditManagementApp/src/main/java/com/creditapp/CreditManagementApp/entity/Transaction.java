package com.creditapp.CreditManagementApp.entity;

import com.creditapp.CreditManagementApp.security.User;
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

    private LocalDateTime date;

    @Enumerated(EnumType.STRING)
    private TransactionStatus status;

    @ManyToOne
    @JoinColumn(name = "shop_owner_id")
    @JsonIgnore
    private User shopOwner;

}
