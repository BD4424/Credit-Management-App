package com.creditapp.CreditManagementApp.service;

import com.creditapp.CreditManagementApp.DTO.KpiMetrics;
import com.creditapp.CreditManagementApp.repository.TransactionRepo;
import com.creditapp.CreditManagementApp.security.User;
import com.creditapp.CreditManagementApp.security.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class StasticsService {
    @Autowired
    UserRepository userRepo;

    @Autowired
    TransactionRepo transactionRepo;

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

    public String formatAmount(Double amount) {
        if (amount == null || amount == 0) return "₹0";

//        if (amount < 1000) return "₹0.5k";
        if (amount < 100000) return "₹" + String.format("%.1f", amount / 1000) + "k";
        if (amount < 10000000) return "₹" + String.format("%.1f", amount / 100000) + "L";

        return "₹" + String.format("%.1f", amount / 10000000) + "Cr";
    }
}
