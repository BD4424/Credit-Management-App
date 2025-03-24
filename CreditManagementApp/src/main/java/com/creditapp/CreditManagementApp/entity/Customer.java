package com.creditapp.CreditManagementApp.entity;

import com.creditapp.CreditManagementApp.security.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "customer_details")
@Data
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    @Column(unique = true, length=10)
    private String phoneNo;

    @Column(unique = true)
    private String email;

    private LocalDateTime created_at;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL)
    private List<Transaction> transactions;

    @ManyToMany
    @JoinTable(name = "customer_shopowners",
            joinColumns = @JoinColumn(name = "customer_id"),
            inverseJoinColumns = @JoinColumn(name = "shopowner)id")
    )
    @JsonIgnore
    private List<User> shopOwners = new ArrayList<>();

}
