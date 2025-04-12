package com.creditapp.CreditManagementApp.repository;

import com.creditapp.CreditManagementApp.DTO.KpiMetrics;
import com.creditapp.CreditManagementApp.entity.Customer;
import com.creditapp.CreditManagementApp.entity.Transaction;
import com.creditapp.CreditManagementApp.security.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

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

    @Query("SELECT SUM(t.amount) FROM Transaction t WHERE t.status = 'PENDING' and t.shopOwner = :shopOwner")
    Double getTotalPendingAmount(User shopOwner);

    @Query("SELECT SUM(t.amount) FROM Transaction t WHERE t.status = 'PENDING' AND t.date <= :thirtyDaysAgo and t.shopOwner = :shopOwner")
    Optional<Double> getTotalPendingAmount30DaysAgo(LocalDateTime thirtyDaysAgo, User shopOwner);

    @Query("""
    SELECT new com.creditapp.CreditManagementApp.DTO.KpiMetrics(
        'High Risk Accounts',
        CONCAT(ROUND( (COUNT(DISTINCT t.customer) * 100.0) / :totalCustomers, 1), '%'),
        NULL,
        NULL
    )
    FROM Transaction t
    WHERE t.status = 'PENDING' 
      AND t.amount > 5000 
      AND TIMESTAMPDIFF(DAY, t.date, CURRENT_DATE) > 30
""")
    List<KpiMetrics> getHighRiskAccounts(@Param("totalCustomers") Integer totalCustomers);

    @Query("""
                SELECT AVG(DATEDIFF(t.paidDate, t.date))
                FROM Transaction t
                WHERE t.status = 'PAID' AND t.paidDate IS NOT NULL and t.shopOwner = :shopOwner
            """)
    Double getAveragePaymentDelay(User shopOwner);
}
