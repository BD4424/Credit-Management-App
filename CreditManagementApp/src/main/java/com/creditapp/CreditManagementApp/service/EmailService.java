package com.creditapp.CreditManagementApp.service;

import com.creditapp.CreditManagementApp.entity.Customer;
import com.creditapp.CreditManagementApp.entity.Transaction;
import com.creditapp.CreditManagementApp.entity.TransactionStatus;
import com.creditapp.CreditManagementApp.repository.TransactionRepo;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmailService {

    @Autowired
    JavaMailSender mailSender;

    @Autowired
    TransactionRepo transactionRepo;

    public void sendReminderMail(Customer customer){
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setFrom("bdprabhutosh@gmail.com");
            helper.setTo(customer.getEmail());
            helper.setSubject("Credit Payment Reminder");

            String emailContent = generateEmailContent(customer);

            helper.setText(emailContent, true);

            mailSender.send(message);
            System.out.println("Reminder email sent to: "+customer.getEmail());
        } catch (MessagingException e) {
            e.printStackTrace();
            System.out.println("Failed to send email to: " + customer.getEmail());
        }
    }

    private String generateEmailContent(Customer customer) {

        StringBuilder content = new StringBuilder();
        content.append("<h2>Dear ").append(customer.getName()).append(",</h2>");
        content.append("<p>You have pending dues for the following items:</p>");
        content.append("<ul>");

        for (Transaction transaction : customer.getTransactions()) {
            if (TransactionStatus.PENDING.equals(transaction.getStatus())) {
                content.append("<li>")
                        .append(transaction.getItemName())
                        .append(" (")
                        .append(transaction.getQuantity())
                        .append(") - ₹")
                        .append(transaction.getAmount())
                        .append("</li>");
            }
        }

        content.append("</ul>");
        content.append("<p>Please clear your payment by this month.</p>");
        content.append("<p>Thank you!</p>");

        return content.toString();
    }

    @Scheduled(cron = "0 0 10 5 * ?")
    public void sendMonthlyReminders(){
        List<Customer> customers = transactionRepo.findCustomersWithPendingTransactions();

        for (Customer customer : customers) {
            System.out.println("Processing customer: " + customer.getName());
            System.out.println("Transactions: " + customer.getTransactions().size());

            for (Transaction transaction : customer.getTransactions()) {
                System.out.println("Item: " + transaction.getItemName() + ", Status: " + transaction.getStatus());
            }

            sendReminderMail(customer);
        }
    }
}
