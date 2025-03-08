package com.creditapp.CreditManagementApp.controller;

import com.creditapp.CreditManagementApp.entity.Customer;
import com.creditapp.CreditManagementApp.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/customers")
public class CustomerController {

    @Autowired
    CustomerService customerService;

    @PostMapping()
    public ResponseEntity<String> createCustomer(@RequestBody Customer customer){
        String customer1 = customerService.createCustomer(customer);

        return new ResponseEntity<>(customer1, HttpStatus.CREATED);
    }

    @GetMapping()
    public ResponseEntity<List<Customer>> getCustomers(){
        List<Customer> customers = customerService.getAllCustomers();

        return new ResponseEntity<>(customers, HttpStatus.OK);
    }
}
