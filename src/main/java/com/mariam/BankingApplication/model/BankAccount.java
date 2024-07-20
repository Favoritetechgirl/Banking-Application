package com.mariam.BankingApplication.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;

@Entity (name = "account")
public class BankAccount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @ManyToOne
    @JoinColumn(name = "account_user_id")
    private AccountUser accountUser;

    private double accountBalance;
    @Pattern(regexp = "[0-9]{10}")
    private String accountNumber;

    public BankAccount() {}

    public AccountUser getAccountUser() {
        return accountUser;
    }

    public void setAccountUser(AccountUser accountUser) {
        this.accountUser = accountUser;
    }

    public double getAccountBalance() {
        return accountBalance;
    }

    public void setAccountBalance(double accountBalance) {
        this.accountBalance = accountBalance;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public BankAccount(AccountUser accountUser, double accountBalance, String accountNumber) {
        this.accountUser = accountUser;
        this.accountBalance = accountBalance;
        this.accountNumber = accountNumber;
    }

    @Override
    public String toString() {
        return "BankAccount{" +
                "id=" + id +
                ", accountUser=" + accountUser +
                ", accountBalance=" + accountBalance +
                ", accountNumber='" + accountNumber + '\'' +
                '}';
    }
}
