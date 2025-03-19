package com.creditapp.CreditManagementApp.repository;

import com.creditapp.CreditManagementApp.entity.Customer;
import com.creditapp.CreditManagementApp.security.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Transactional
public interface CustomerRepo extends JpaRepository<Customer, Integer> {
    List<Customer> findByShopOwner(User shopOwner);
}
