package com.creditapp.CreditManagementApp.DTO;

import com.creditapp.CreditManagementApp.entity.TransactionStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
public class TransactionDTO {

    private Integer transactionId;

    private Integer customerId;

    private String itemName;

    private String quantity;

    private Double amount;

    private TransactionStatus status;

    private LocalDateTime date;

    private String customerName;

}
