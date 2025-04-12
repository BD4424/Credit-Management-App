package com.creditapp.CreditManagementApp.service;

import com.creditapp.CreditManagementApp.DTO.KpiMetrics;
import com.creditapp.CreditManagementApp.DTO.TransactionDTO;
import com.creditapp.CreditManagementApp.entity.Transaction;

import java.time.LocalDate;
import java.util.List;

public interface TransactionService {

    String createTransaction(List<TransactionDTO> transactions, String shopOwner);

    List<Transaction> pendingTransactions(Integer customerId);

    String updateTransactionAsPaid(Integer transactionId);

    List<Transaction> allTransactionsofCustomer(Integer customerId);

    List<TransactionDTO> allTransactions(String userName);

    List<Transaction> allTransactionsOfCustomerOfAShopOwner(Integer customerId, String shopOwner);

    List<TransactionDTO> getFilteredTransactions(LocalDate fromDate, LocalDate toDate, String status, String shopOwner);

    byte[] generateTransactionsPdf(List<Integer> transactionIds);

    List<KpiMetrics> getKpiMetrics(String shopOwner);

}
