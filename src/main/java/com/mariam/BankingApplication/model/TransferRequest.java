package com.mariam.BankingApplication.model;

import lombok.Data;

@Data
public class TransferRequest {
    private  String accountFrom;

    private String accountTo;
    
    private double amount;
}
