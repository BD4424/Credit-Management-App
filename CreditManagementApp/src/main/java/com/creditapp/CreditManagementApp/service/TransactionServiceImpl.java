package com.creditapp.CreditManagementApp.service;

import com.creditapp.CreditManagementApp.DTO.KpiMetrics;
import com.creditapp.CreditManagementApp.DTO.TransactionDTO;
import com.creditapp.CreditManagementApp.entity.Customer;
import com.creditapp.CreditManagementApp.entity.Transaction;
import com.creditapp.CreditManagementApp.entity.TransactionStatus;
import com.creditapp.CreditManagementApp.repository.CustomerRepo;
import com.creditapp.CreditManagementApp.repository.TransactionRepo;
import com.creditapp.CreditManagementApp.security.User;
import com.creditapp.CreditManagementApp.security.UserRepository;
import com.itextpdf.text.DocumentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TransactionServiceImpl implements TransactionService {

    @Autowired
    TransactionRepo transactionRepo;

    @Autowired
    CustomerRepo customerRepo;

    @Autowired
    CustomerService customerService;

    @Autowired
    UserRepository userRepo;

    @Override
    public String createTransaction(List<TransactionDTO> transactions, String shopOwner) {
        try {

            Optional<User> shopOwnerOptional = userRepo.findByUserName(shopOwner);
            if (shopOwnerOptional.isEmpty()) throw new RuntimeException("User Not Found");

            List<Transaction> allTransactions = new ArrayList<>();

            for (TransactionDTO transaction : transactions) {
                Transaction transaction1 = new Transaction();
                Optional<Customer> customer = customerRepo.findById(transaction.getCustomerId());
                if (customer.isEmpty()) throw new RuntimeException("Failed to fetch customer details!");
                transaction1.setAmount(transaction.getAmount());
                transaction1.setCustomer(customer.get());
                transaction1.setDate(LocalDateTime.now());
                transaction1.setQuantity(transaction.getQuantity());
                transaction1.setItemName(transaction.getItemName());
                transaction1.setStatus(transaction.getStatus());
                transaction1.setShopOwner(shopOwnerOptional.get());

                allTransactions.add(transaction1);
            }


            transactionRepo.saveAll(allTransactions);
            return "Transaction added!";
        } catch (Exception ex) {
            return "Transaction failed!";
        }
    }

    @Override
    public List<Transaction> pendingTransactions(Integer customerId) {
        try {
            return transactionRepo.fetchAllPendingTransactionForCustomer(customerId);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    @Override
    public String updateTransactionAsPaid(Integer transactionId) {

        try {
            Optional<Transaction> transaction = transactionRepo.findById(transactionId);
            if (transaction.isPresent()) {
                Transaction transaction1 = transaction.get();
                transaction1.setStatus(TransactionStatus.PAID);
                transaction1.setPaidDate(LocalDateTime.now());
                transactionRepo.save(transaction1);
                return "Transaction updated as paid!";
            } else return "No such transaction found";
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return null;
    }

    @Override
    public List<Transaction> allTransactionsofCustomer(Integer customerId) {
        try {
            return transactionRepo.findByCustomerId(customerId);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Transaction> allTransactionsOfCustomerOfAShopOwner(Integer customerId, String shopOwner) {
        Optional<User> shopOwnerOptional = userRepo.findByUserName(shopOwner);
        Optional<Customer> customer = customerRepo.findById(customerId);

        List<Transaction> byCustomerAndShopOwner = new ArrayList<>();

        if (customer.isPresent() && shopOwnerOptional.isPresent()) {
            byCustomerAndShopOwner = transactionRepo.findByCustomerAndShopOwner(customer.get(), shopOwnerOptional.get());
        }

        return byCustomerAndShopOwner;
    }

    @Override
    public List<TransactionDTO> getFilteredTransactions(LocalDate fromDate, LocalDate toDate, String status, String shopOwner) {

        Optional<User> shopOwnerOptional = userRepo.findByUserName(shopOwner);
        if (shopOwnerOptional.isEmpty()) throw new RuntimeException("Shop Owner not available or check DB!");

        System.out.println(fromDate+":::::"+toDate);
        Specification<Transaction> spec = Specification.where(null);

        if (fromDate != null) {
            spec = spec.and((root, query, cb) -> cb.greaterThanOrEqualTo(root.get("date"), fromDate));
        }
        if (toDate != null) {
            spec = spec.and((root, query, cb) -> cb.lessThan(root.get("date"), toDate.plusDays(1).atStartOfDay()));
        }
        if (status != null && !status.equals("ALL")) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("status"), status));
        }

        List<Transaction> transactionList = transactionRepo.findAll(spec);

        List<TransactionDTO> transactionDTOS = new ArrayList<>();
        for (Transaction transaction: transactionList) {
            if (transaction.getShopOwner().equals(shopOwnerOptional.get())){
                TransactionDTO transactionDTO = new TransactionDTO();;
                transactionDTO.setCustomerName(transaction.getCustomer().getName());
                transactionDTO.setTransactionId(transaction.getTransactionId());
                transactionDTO.setAmount(transaction.getAmount());
                transactionDTO.setQuantity(transaction.getQuantity());
                transactionDTO.setItemName(transaction.getItemName());
                transactionDTO.setStatus(transaction.getStatus());
                transactionDTO.setDate(transaction.getDate());

                transactionDTOS.add(transactionDTO);
            }
        }

        return transactionDTOS;

    }

    @Override
    public byte[] generateTransactionsPdf(List<Integer> transactionIds) {

        List<Transaction> allTransactions = transactionRepo.findAllById(transactionIds);
        byte[] pdfBytes;
        try {
            pdfBytes = PdfExportService.generateTransactionsPdf(allTransactions);
        } catch (DocumentException e) {
            e.printStackTrace();
            throw new RuntimeException("Document could not be created properly.");
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Pdf generation failed");
        }
        return pdfBytes;
    }

    @Override
    public List<KpiMetrics> getKpiMetrics(String shopOwner) {

        User user = userRepo.findByUserName(shopOwner).orElseThrow(()-> new RuntimeException("Shop Owner not present in DB!"));

        try {
            double currentOutstanding = transactionRepo.getTotalPendingAmount(user);
            double pastOutstanding = transactionRepo.getTotalPendingAmount30DaysAgo(LocalDateTime.now().minusDays(30),user).orElse(0.0);

            String trend = currentOutstanding > pastOutstanding ? "up" : "down";
            String delta = formatAmount(currentOutstanding - pastOutstanding);

            KpiMetrics outstandingMetric = new KpiMetrics("Total Outstanding", formatAmount(currentOutstanding), trend, delta);

            Double avgDelay = transactionRepo.getAveragePaymentDelay(user);
            Double prevAvgDelay = 2.8;

            String delayTrend = (avgDelay < prevAvgDelay) ? "down" : "up";
            String delayDelta = String.format("%.1f", Math.abs(avgDelay - prevAvgDelay)) + " days";

            KpiMetrics delayMetric = new KpiMetrics("Avg. Payment Delay", String.format("%.1f days", avgDelay), delayTrend, delayDelta);

            List<KpiMetrics> highRiskAccounts = transactionRepo.getHighRiskAccounts(user.getCustomers().size());

            return List.of(outstandingMetric,delayMetric, highRiskAccounts.get(0));
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException("Couldn't fetch details");
        }

    }

    @Override
    public List<TransactionDTO> allTransactions(String shopOwner) {
        List<Customer> allCustomers = customerService.getAllCustomers(shopOwner);
        List<TransactionDTO> transactionDTOS = new ArrayList<>();
        User shopOwner1 = userRepo.findByUserName(shopOwner)
                .orElseThrow(() -> new UsernameNotFoundException("Shop Owner not found"));

        allCustomers.forEach(customer -> {
            customer.getTransactions().forEach(transaction -> {
                if (transaction.getShopOwner().equals(shopOwner1)){
                    TransactionDTO transaction1 = new TransactionDTO();
                    transaction1.setTransactionId(transaction.getTransactionId());
                    transaction1.setAmount(transaction.getAmount());
                    transaction1.setDate(transaction.getDate());
                    transaction1.setCustomerId(transaction.getCustomer().getId());
                    transaction1.setItemName(transaction.getItemName());
                    transaction1.setQuantity(transaction.getQuantity());
                    transaction1.setStatus(transaction.getStatus());
                    transaction1.setCustomerName(transaction.getCustomer().getName());

                    transactionDTOS.add(transaction1);
                }

            });
        });

        return transactionDTOS;
    }

    public String formatAmount(Double amount) {
        if (amount == null || amount == 0) return "₹0";

//        if (amount < 1000) return "₹0.5k";
        if (amount < 100000) return "₹" + String.format("%.1f", amount / 1000) + "k";
        if (amount < 10000000) return "₹" + String.format("%.1f", amount / 100000) + "L";

        return "₹" + String.format("%.1f", amount / 10000000) + "Cr";
    }
}
