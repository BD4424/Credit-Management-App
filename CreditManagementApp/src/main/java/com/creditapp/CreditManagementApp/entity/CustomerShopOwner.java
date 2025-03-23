package com.creditapp.CreditManagementApp.entity;

import com.creditapp.CreditManagementApp.security.User;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class CustomerShopOwner {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Customer customer;

    @ManyToOne
    private User shopOwner;
}
