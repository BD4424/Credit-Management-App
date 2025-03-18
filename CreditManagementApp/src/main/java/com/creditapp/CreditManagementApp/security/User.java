package com.creditapp.CreditManagementApp.security;

import jakarta.persistence.*;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

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

}
