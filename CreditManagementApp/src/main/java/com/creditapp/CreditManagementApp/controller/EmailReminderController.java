package com.creditapp.CreditManagementApp.controller;

import com.creditapp.CreditManagementApp.entity.Customer;
import com.creditapp.CreditManagementApp.repository.CustomerRepo;
import com.creditapp.CreditManagementApp.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/send-reminders")
public class EmailReminderController {

    @Autowired
    EmailService emailService;

    @Autowired
    CustomerRepo customerRepo;

    @PostMapping
    public void sendReminder(){
        emailService.sendMonthlyReminders();
    }

    @PostMapping("/sendMails")
    @PreAuthorize("hasAuthority('ROLE_SHOP_OWNER')")
    public ResponseEntity<Map<String, String>> sendRemindersManually() {
        Map<String, String> response = new HashMap<>();
        try {
            emailService.sendMonthlyReminders();
            response.put("message", "Reminder emails sent successfully!");

        }catch (Exception ex){
            ex.printStackTrace();
            response.put("message", "Mail sending failed!");
        }
        return ResponseEntity.ok(response);


    }

    @PostMapping("/sendIndividualReminder/{customerId}")
    @PreAuthorize("hasAuthority('ROLE_SHOP_OWNER')")
    public ResponseEntity<Map<String, String>> sendRemindersManually(@PathVariable(name = "customerId") Integer customerId) {
        Optional<Customer> customer = customerRepo.findById(customerId);
        Map<String, String> response = new HashMap<>();
        try {
            customer.ifPresent(value -> emailService.sendReminderMail(value));
            response.put("message", "Reminder email sent successfully to customer: "+customer.get().getEmail());
        } catch (Exception ex){
            ex.printStackTrace();
            response.put("message", "Mail sending failed!");
        }

        return ResponseEntity.ok(response);
    }
}
