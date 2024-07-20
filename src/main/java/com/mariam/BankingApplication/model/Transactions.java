package com.mariam.BankingApplication.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.util.Date;

import static org.springframework.data.jpa.domain.AbstractPersistable_.id;

@Data
@Entity(name = "transactions")
public class Transactions {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    @NotBlank
    @Length(min = 10)
    private String accountNumber;

    private Date transactionDate;

    private double amount;

    private String transactionId;

    @Enumerated(EnumType.STRING)
    private TransactionType transactionType;
}
