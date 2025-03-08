package com.creditapp.CreditManagementApp.controller;

import com.creditapp.CreditManagementApp.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/send-reminders")
public class EmailReminderController {

    @Autowired
    EmailService emailService;

    @PostMapping
    public void sendReminder(){
        emailService.sendMonthlyReminders();
    }

    @PostMapping("/send")
    public String sendRemindersManually() {
        emailService.sendMonthlyReminders();
        return "Reminder emails sent successfully!";
    }
}
