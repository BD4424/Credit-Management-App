package com.creditapp.CreditManagementApp.DTO;

import lombok.Data;

@Data
public class AuthRequest {

    private String email; //Same username
    private String password;
}
