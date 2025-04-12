package com.creditapp.CreditManagementApp.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class KpiMetrics {
    String title;
    String value;
    String trend; // "up", "down", or null
    String delta;  // e.g., "+12%"
}