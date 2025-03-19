package com.creditapp.CreditManagementApp.service;

import com.creditapp.CreditManagementApp.entity.Customer;
import com.creditapp.CreditManagementApp.entity.Transaction;
import com.creditapp.CreditManagementApp.repository.CustomerRepo;
import com.creditapp.CreditManagementApp.security.User;
import com.creditapp.CreditManagementApp.security.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CustomerServiceImpl implements CustomerService{

    @Autowired
    CustomerRepo customerRepo;

    @Autowired
    UserRepository userRepository;

    @Override
    public Customer createCustomer(Customer customer, String shopOwner){
        try {
            System.out.println("Saving Customer: " + customer.getName());

            if (customer.getTransactions() != null) {
                for (Transaction transaction : customer.getTransactions()) {
                    transaction.setCustomer(customer);
                    System.out.println("Assigning transaction: " + transaction.getItemName());
                }
            }

            User shopOwnerDetails = userRepository.findByUserName(shopOwner)
                    .orElseThrow(() -> new UsernameNotFoundException("Shop Owner not found"));
            customer.setShopOwner(shopOwnerDetails);
            customer.setCreated_at(LocalDateTime.now());

            return customerRepo.save(customer);
        } catch (DataIntegrityViolationException e) {
            throw new RuntimeException("Database constraint violated: " + e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException("Error while saving customer: " + e.getMessage());
        }
    }


    @Override
    public List<Customer> getAllCustomers(String shopOwner) {

        User shopOwnerDetails = userRepository.findByUserName(shopOwner)
                .orElseThrow(() -> new UsernameNotFoundException("Shop Owner not found"));

        return customerRepo.findByShopOwner(shopOwnerDetails);
    }
}
