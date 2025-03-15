package com.creditapp.CreditManagementApp.service;

import com.creditapp.CreditManagementApp.DTO.TransactionDTO;
import com.creditapp.CreditManagementApp.entity.Customer;
import com.creditapp.CreditManagementApp.entity.Transaction;
import com.creditapp.CreditManagementApp.entity.TransactionStatus;
import com.creditapp.CreditManagementApp.repository.CustomerRepo;
import com.creditapp.CreditManagementApp.repository.TransactionRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TransactionServiceImpl implements TransactionService{

    @Autowired
    TransactionRepo transactionRepo;

    @Autowired
    CustomerRepo customerRepo;

    @Override
    public String createTransaction(TransactionDTO transaction){
        try {
            Transaction transaction1 = new Transaction();
            Optional<Customer> customer = customerRepo.findById(transaction.getCustomerId());
            if (customer.isEmpty()) throw new RuntimeException("Failed to fetch customer details!");
            transaction1.setAmount(transaction.getAmount());
            transaction1.setCustomer(customer.get());
            transaction1.setDate(LocalDateTime.now());
            transaction1.setQuantity(transaction.getQuantity());
            transaction1.setItemName(transaction.getItemName());
            transaction1.setStatus(transaction.getStatus());

            transactionRepo.save(transaction1);
            return "Transaction added!";
        }catch (Exception ex){
            return "Transaction failed!";
        }
    }

    @Override
    public List<Transaction> pendingTransactions(Integer customerId) {
        try {
            return transactionRepo.fetchAllPendingTransactionForCustomer(customerId);
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return null;
    }

    @Override
    public String updateTransactionAsPaid(Integer transactionId) {

        try {
            Optional<Transaction> transaction = transactionRepo.findById(transactionId);
            if (transaction.isPresent()){
                Transaction transaction1 = transaction.get();
                transaction1.setStatus(TransactionStatus.PAID);
                transactionRepo.save(transaction1);
                return "Transaction updated as paid!";
            } else return "No such transaction found";
        }catch (Exception ex){
            ex.printStackTrace();
        }

        return null;
    }

    @Override
    public List<Transaction> allTransactionsofCustomer(Integer customerId) {
        try {
            return transactionRepo.findByCustomerId(customerId);
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return null;
    }

    @Override
    public List<TransactionDTO> allTransactions() {
        List<Transaction> transactionList = transactionRepo.findAll();
        List<TransactionDTO> transactionDTOS = new ArrayList<>();
        for (Transaction transaction: transactionList){
            TransactionDTO transaction1 = new TransactionDTO();
            transaction1.setTransactionId(transaction.getTransactionId());
            transaction1.setAmount(transaction.getAmount());
            transaction1.setDate(transaction.getDate());
            transaction1.setCustomerId(transaction.getCustomer().getId());
            transaction1.setItemName(transaction.getItemName());
            transaction1.setQuantity(transaction.getQuantity());
            transaction1.setStatus(transaction.getStatus());
            transaction1.setCustomerName(transaction.getCustomer().getName());

            transactionDTOS.add(transaction1);
        }
        return transactionDTOS;
    }


}
