package com.creditapp.CreditManagementApp.controller;

import com.creditapp.CreditManagementApp.DTO.KpiMetrics;
import com.creditapp.CreditManagementApp.security.JwtUtil;
import com.creditapp.CreditManagementApp.service.StasticsService;
import com.creditapp.CreditManagementApp.service.TransactionService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/analytics")
public class AnalyticsController {

    @Autowired
    TransactionService transactionService;

    @Autowired
    JwtUtil jwtUtil;

    @Autowired
    StasticsService stasticsService;

    @GetMapping("/kpis")
    public List<KpiMetrics> getKpis(HttpServletRequest req) {

        String shopOwner = jwtUtil.extractUserNameFromRequest(req);
        return stasticsService.getKpiMetrics(shopOwner);
    }


}
