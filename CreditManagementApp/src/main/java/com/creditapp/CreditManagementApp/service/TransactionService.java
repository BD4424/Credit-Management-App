package com.creditapp.CreditManagementApp.service;

import com.creditapp.CreditManagementApp.DTO.TransactionDTO;
import com.creditapp.CreditManagementApp.entity.Transaction;

import java.util.List;

public interface TransactionService {

    String createTransaction(TransactionDTO transaction);
    List<Transaction> pendingTransactions(Integer customerId);
    String updateTransactionAsPaid(Integer transactionId);
}
