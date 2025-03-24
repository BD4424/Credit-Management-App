package com.creditapp.CreditManagementApp.service;

import com.creditapp.CreditManagementApp.entity.Customer;
import com.creditapp.CreditManagementApp.repository.CustomerRepo;
import com.creditapp.CreditManagementApp.security.User;
import com.creditapp.CreditManagementApp.security.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

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

            User shopOwnerDetails = userRepository.findByUserName(shopOwner)
                    .orElseThrow(() -> new UsernameNotFoundException("Shop Owner not found"));
            // Check if customer already exists (by phone or email)
            Optional<Customer> existingCustomer = customerRepo.findByPhoneNoOrEmail(customer.getPhoneNo(), customer.getEmail());

            Customer savedCustomer;
            if (existingCustomer.isPresent()){
                savedCustomer = existingCustomer.get();

                if (!savedCustomer.getShopOwners().contains(shopOwnerDetails)){
                    savedCustomer.getShopOwners().add(shopOwnerDetails);
                    customerRepo.save(savedCustomer);
                }
                if (!shopOwnerDetails.getCustomers().contains(savedCustomer)){
                    shopOwnerDetails.getCustomers().add(savedCustomer);
                    userRepository.save(shopOwnerDetails);
                }
            }else {
                // Create new Customer
                customer.setCreated_at(LocalDateTime.now());
                customer.getShopOwners().add(shopOwnerDetails);

                savedCustomer = customerRepo.save(customer);
            }

            return savedCustomer;
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

        return shopOwnerDetails.getCustomers();
    }
}
