package com.creditapp.CreditManagementApp.records;

import java.math.BigDecimal;

public record DashboardSummary(BigDecimal totalOutstanding,
                               BigDecimal avgPaymentDelayDays,
                               long highRiskCustomerCount,
                               BigDecimal monthOverMonthGrowth) {}
