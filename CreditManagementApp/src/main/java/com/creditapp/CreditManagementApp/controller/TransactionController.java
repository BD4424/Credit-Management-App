package com.creditapp.CreditManagementApp.controller;

import com.creditapp.CreditManagementApp.DTO.TransactionDTO;
import com.creditapp.CreditManagementApp.entity.Transaction;
import com.creditapp.CreditManagementApp.repository.TransactionRepo;
import com.creditapp.CreditManagementApp.security.JwtUtil;
import com.creditapp.CreditManagementApp.service.TransactionService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/transactions")
public class TransactionController {

    @Autowired
    TransactionService transactionService;

    @Autowired
    JwtUtil jwtUtil;

    @PostMapping()
    @PreAuthorize("hasAnyAuthority('ROLE_CUSTOMER', 'ROLE_SHOP_OWNER')")
    public ResponseEntity<Map<String, String>> createTransaction(@RequestBody List<TransactionDTO> transactions
            , HttpServletRequest request){

        String shopOwner = jwtUtil.extractUserNameFromRequest(request);

        String transaction1 = transactionService.createTransaction(transactions,shopOwner);

        Map<String, String> response = new HashMap<>();
        response.put("message", transaction1);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/pendingTransactions/{customerId}")
    @PreAuthorize("hasAnyAuthority('ROLE_CUSTOMER', 'ROLE_SHOP_OWNER')")
    public ResponseEntity<List<Transaction>> pendingTransactions(@PathVariable(name = "customerId") Integer customerId){
        List<Transaction> transactions = transactionService.pendingTransactions(customerId);

        return new ResponseEntity<>(transactions, HttpStatus.OK);
    }

    @PutMapping("/updateTransaction/{transactionId}")
    @PreAuthorize("hasAuthority('ROLE_SHOP_OWNER')")
    public ResponseEntity<Map<String, String>> updateTransactionAsPaid(@PathVariable(name = "transactionId") Integer transactionId) {
        String message = transactionService.updateTransactionAsPaid(transactionId);

        Map<String, String> response = new HashMap<>();
        response.put("message", message);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/allTransactions/{customerId}")
    @PreAuthorize("hasAnyAuthority('ROLE_CUSTOMER', 'ROLE_SHOP_OWNER')")
    public ResponseEntity<List<Transaction>> allTransactionsOfCustomer(@PathVariable(name = "customerId") Integer customerId
            , HttpServletRequest request){
        String shopOwner = jwtUtil.extractUserNameFromRequest(request);

        List<Transaction> transactions = transactionService.allTransactionsOfCustomerOfAShopOwner(customerId, shopOwner);

        return new ResponseEntity<>(transactions, HttpStatus.OK);
    }

    @GetMapping("/allTransactions")
    @PreAuthorize("hasAuthority('ROLE_SHOP_OWNER')")
    public ResponseEntity<List<TransactionDTO>> allTransactions(HttpServletRequest request){

        String shopOwner = jwtUtil.extractUserNameFromRequest(request);

        List<TransactionDTO> transactions = transactionService.allTransactions(shopOwner);

        return new ResponseEntity<>(transactions, HttpStatus.OK);
    }

    @GetMapping("/getFilteredTransactions")
    public ResponseEntity<List<TransactionDTO>> getFilteredTransactions(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate,
            @RequestParam(required = false) String status, HttpServletRequest request){

        String shopOwner = jwtUtil.extractUserNameFromRequest(request);

        List<TransactionDTO> transactions = transactionService.getFilteredTransactions(fromDate, toDate, status,shopOwner);

        System.out.println("Inside getFilteredTransactions");


        return ResponseEntity.ok(transactions);
    }
}
