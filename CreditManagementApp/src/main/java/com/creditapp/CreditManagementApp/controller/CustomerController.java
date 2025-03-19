package com.creditapp.CreditManagementApp.controller;

import com.creditapp.CreditManagementApp.entity.Customer;
import com.creditapp.CreditManagementApp.security.JwtUtil;
import com.creditapp.CreditManagementApp.service.CustomerService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/customers")
public class CustomerController {

    @Autowired
    CustomerService customerService;

    @Autowired
    JwtUtil jwtUtil;

    @PostMapping()
    @PreAuthorize("hasAuthority('ROLE_SHOP_OWNER')")
    public ResponseEntity<Map<String, String>> createCustomer(@RequestBody Customer customer, HttpServletRequest request){
        String shopOwner = jwtUtil.extractUserNameFromRequest(request);

        Map<String, String> response = new HashMap<>();
        try {
            Customer savedCustomer = customerService.createCustomer(customer, shopOwner);
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
    @PreAuthorize("hasAuthority('ROLE_SHOP_OWNER')")
    public ResponseEntity<List<Customer>> getCustomers(HttpServletRequest request){
        String shopOwner = jwtUtil.extractUserNameFromRequest(request);

        System.out.println("Getting customers of shop owner:"+shopOwner);
        List<Customer> customers = customerService.getAllCustomers(shopOwner);

        return new ResponseEntity<>(customers, HttpStatus.OK);
    }
}
