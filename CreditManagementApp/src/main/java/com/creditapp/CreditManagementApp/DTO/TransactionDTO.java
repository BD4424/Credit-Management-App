package com.creditapp.CreditManagementApp.DTO;

import com.creditapp.CreditManagementApp.entity.TransactionStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TransactionDTO {

    private Integer customerId;

    private String itemName;

    private String quantity;

    private Double amount;

    private TransactionStatus status;

}
