package com.creditapp.CreditManagementApp.service;

import com.creditapp.CreditManagementApp.entity.Customer;
import com.creditapp.CreditManagementApp.repository.TransactionRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class Scheduler {

    @Autowired
    TransactionRepo transactionRepo;
    public void sendMonthlyReminders() {
        List<Customer> customers = transactionRepo.findCustomersWithPendingTransactions();

//        customers.stream().forEach();
    }
}
