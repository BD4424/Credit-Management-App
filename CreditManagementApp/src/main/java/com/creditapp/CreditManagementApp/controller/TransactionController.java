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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/transactions")
public class TransactionController {

    @Autowired
    TransactionService transactionService;

    @PostMapping()
    public ResponseEntity<Map<String, String>> createTransaction(@RequestBody TransactionDTO transaction){
        String transaction1 = transactionService.createTransaction(transaction);

        Map<String, String> response = new HashMap<>();
        response.put("message", transaction1);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/pendingTransactions/{customerId}")
    public ResponseEntity<List<Transaction>> pendingTransactions(@PathVariable(name = "customerId") Integer customerId){
        List<Transaction> transactions = transactionService.pendingTransactions(customerId);

        return new ResponseEntity<>(transactions, HttpStatus.OK);
    }

    @PutMapping("/updateTransaction/{transactionId}")
    public ResponseEntity<Map<String, String>> updateTransactionAsPaid(@PathVariable(name = "transactionId") Integer transactionId) {
        String message = transactionService.updateTransactionAsPaid(transactionId);

        Map<String, String> response = new HashMap<>();
        response.put("message", message);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/allTransactions/{customerId}")
    public ResponseEntity<List<Transaction>> allTransactions(@PathVariable(name = "customerId") Integer customerId){
        List<Transaction> transactions = transactionService.allTransactions(customerId);

        return new ResponseEntity<>(transactions, HttpStatus.OK);
    }
}
