package com.creditapp.CreditManagementApp.controller;

import com.creditapp.CreditManagementApp.entity.Customer;
import com.creditapp.CreditManagementApp.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/customers")
public class CustomerController {

    @Autowired
    CustomerService customerService;

    @PostMapping()
    public ResponseEntity<Map<String, String>> createCustomer(@RequestBody Customer customer){

        Map<String, String> response = new HashMap<>();
        try {
            Customer savedCustomer = customerService.createCustomer(customer);
            response.put("message", "Customer created successfully!");
            response.put("customerId", savedCustomer.getId().toString()); // Return created ID
            return new ResponseEntity<>(response, HttpStatus.CREATED); // 201 Created
        } catch (IllegalArgumentException e) {
            response.put("error", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST); // 400 Bad Request
        } catch (RuntimeException e) {
            response.put("error", "Internal Server Error: " + e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR); // 500
        }
    }

    @GetMapping()
    public ResponseEntity<List<Customer>> getCustomers(){
        List<Customer> customers = customerService.getAllCustomers();

        return new ResponseEntity<>(customers, HttpStatus.OK);
    }
}
