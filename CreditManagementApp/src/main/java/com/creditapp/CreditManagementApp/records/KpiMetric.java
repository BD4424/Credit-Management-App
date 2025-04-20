package com.creditapp.CreditManagementApp.records;

public record KpiMetric(
    String title,
    String value,
    String trend, // "up", "down", "neutral"
    String delta  // e.g., "+12%"
) {}