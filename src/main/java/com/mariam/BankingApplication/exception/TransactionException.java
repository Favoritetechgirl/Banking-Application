package com.mariam.BankingApplication.exception;

public class TransactionException extends RuntimeException {
    private String message;
    public TransactionException(String message){
        super(message);
    }
}
