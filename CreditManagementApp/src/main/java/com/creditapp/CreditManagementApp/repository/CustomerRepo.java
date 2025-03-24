package com.creditapp.CreditManagementApp.repository;

import com.creditapp.CreditManagementApp.entity.Customer;
import com.creditapp.CreditManagementApp.security.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public interface CustomerRepo extends JpaRepository<Customer, Integer> {

    Optional<Customer> findByPhoneNoOrEmail(String phoneNo, String email);
}
