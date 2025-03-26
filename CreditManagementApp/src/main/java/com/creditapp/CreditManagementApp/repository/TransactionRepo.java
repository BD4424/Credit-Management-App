package com.creditapp.CreditManagementApp.repository;

import com.creditapp.CreditManagementApp.entity.Customer;
import com.creditapp.CreditManagementApp.entity.Transaction;
import com.creditapp.CreditManagementApp.security.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Transactional
public interface TransactionRepo extends JpaRepository<Transaction, Integer>, JpaSpecificationExecutor<Transaction> {

    @Query(value = "select * from transaction where customer_id = ?1 and status = 'PENDING'", nativeQuery = true)
    List<Transaction> fetchAllPendingTransactionForCustomer(Integer customerId);

    @Query(value = "select t.customer from Transaction t where t.status = 'PENDING' GROUP BY t.customer")
    List<Customer> findCustomersWithPendingTransactions();

    @Query
    List<Transaction> findByCustomerId(Integer customerId);

    List<Transaction> findByCustomerAndShopOwner(Customer customer, User shopOwner);
}
