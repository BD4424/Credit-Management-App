package com.creditapp.CreditManagementApp.service;

import com.creditapp.CreditManagementApp.entity.Customer;

import java.util.List;

public interface CustomerService {

    String createCustomer(Customer customer);
    List<Customer> getAllCustomers();
}
