package com.creditapp.CreditManagementApp.security;

import com.creditapp.CreditManagementApp.entity.Customer;
import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer userId;
    @Column(unique = true)
    private String userName;
    private String password;

    @Enumerated(EnumType.STRING)
    private RoleType role;

    @ManyToMany(mappedBy = "shopOwners")
    List<Customer> customers = new ArrayList<>();

}
