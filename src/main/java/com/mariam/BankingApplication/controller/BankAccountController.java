package com.mariam.BankingApplication.controller;

import com.mariam.BankingApplication.exception.TransactionException;
import com.mariam.BankingApplication.model.AccountUser;
import com.mariam.BankingApplication.model.BankAccount;
import com.mariam.BankingApplication.model.OperationRequest;
import com.mariam.BankingApplication.model.TransferRequest;
import com.mariam.BankingApplication.service.BankAccountService;
import com.mariam.BankingApplication.service.BankingOperationService;
import jakarta.mail.MessagingException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/accounts")
public class BankAccountController {

    @Autowired
    private BankingOperationService operationService;

    @PostMapping("/deposit")
    public ResponseEntity<BankAccount> depositFund(@RequestBody OperationRequest request) throws MessagingException {
        return new ResponseEntity<>(operationService.depositFund(request.getAccountNumber(), request.getAmount()).getBody(), HttpStatus.OK);
    }

    @PostMapping("/withdraw")
    public ResponseEntity<BankAccount> withdrawFund(@RequestBody OperationRequest request) throws MessagingException {
        return new ResponseEntity<>(operationService.withdrawFund(request.getAccountNumber(), request.getAmount()).getBody(), HttpStatus.OK);
    }

    @PostMapping("/transfer")
    @Transactional
    public ResponseEntity<String> transferFunds(@RequestBody TransferRequest request ){
        try{
            operationService.withdrawFund(request.getAccountFrom(), request.getAmount());
            operationService.depositFund(request.getAccountTo(), request.getAmount());
            return new ResponseEntity<>("Transaction Successful", HttpStatus.OK);
        } catch (TransactionException | MessagingException transactionException){
            System.out.println(transactionException.getMessage());
        }
        return new ResponseEntity<>("Transaction failed", HttpStatus.NOT_ACCEPTABLE);
    }
}

