package com.creditapp.CreditManagementApp.service;

import com.creditapp.CreditManagementApp.entity.Customer;

import java.util.List;

public interface CustomerService {

    Customer createCustomer(Customer customer, String shopOwner);
    List<Customer> getAllCustomers(String shopOwner);
}
