package com.creditapp.CreditManagementApp.service;

import com.creditapp.CreditManagementApp.entity.Customer;
import com.creditapp.CreditManagementApp.entity.Transaction;
import com.creditapp.CreditManagementApp.repository.CustomerRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerServiceImpl implements CustomerService{

    @Autowired
    CustomerRepo customerRepo;

//    @Override
//    public String createCustomer(Customer customer){
//        try{
//            customerRepo.save(customer);
//            return "customer created successfully!";
//        }catch (Exception ex){
//            return "Customer creation failed!";
//        }
//    }

    @Override
    public Customer createCustomer(Customer customer){
        try {
            System.out.println("Saving Customer: " + customer.getName());

            if (customer.getTransactions() != null) {
                for (Transaction transaction : customer.getTransactions()) {
                    transaction.setCustomer(customer);
                    System.out.println("Assigning transaction: " + transaction.getItemName());
                }
            }

            return customerRepo.save(customer);
        } catch (DataIntegrityViolationException e) {
            throw new RuntimeException("Database constraint violated: " + e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException("Error while saving customer: " + e.getMessage());
        }
    }


    @Override
    public List<Customer> getAllCustomers() {
        return customerRepo.findAll();
    }
}
