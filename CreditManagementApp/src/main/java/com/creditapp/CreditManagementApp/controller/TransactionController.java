package com.creditapp.CreditManagementApp.controller;

import com.creditapp.CreditManagementApp.DTO.TransactionDTO;
import com.creditapp.CreditManagementApp.entity.Transaction;
import com.creditapp.CreditManagementApp.repository.TransactionRepo;
import com.creditapp.CreditManagementApp.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/transactions")
public class TransactionController {

    @Autowired
    TransactionService transactionService;

    @PostMapping()
    public ResponseEntity<String> createTransaction(TransactionDTO transaction){
        String transaction1 = transactionService.createTransaction(transaction);

        return new ResponseEntity<>(transaction1, HttpStatus.CREATED);
    }

    @GetMapping("/{customerId}")
    public ResponseEntity<List<Transaction>> pendingTransactions(@PathVariable(name = "customerId") Integer customerId){
        List<Transaction> transactions = transactionService.pendingTransactions(customerId);

        return new ResponseEntity<>(transactions, HttpStatus.OK);
    }

    @GetMapping("/{transactionId}")
    public ResponseEntity<String> updateTransactionAsPaid(Integer transactionId) {
        String message = transactionService.updateTransactionAsPaid(transactionId);

        return new ResponseEntity<>(message, HttpStatus.OK);
    }
}
