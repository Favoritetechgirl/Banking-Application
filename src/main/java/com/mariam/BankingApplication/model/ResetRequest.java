package com.mariam.BankingApplication.model;

import lombok.Data;

@Data
public class ResetRequest {

    private String username;

    private String resetCode;

    private String password;

    private String confirmPassword;
}
