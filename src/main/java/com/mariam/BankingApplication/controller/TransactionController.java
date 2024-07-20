package com.mariam.BankingApplication.controller;


import lombok.RequiredArgsConstructor;
import com.mariam.BankingApplication.model.Transactions;
import com.mariam.BankingApplication.service.TransactionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    @GetMapping("/allTransactions")
    private ResponseEntity<List<Transactions>> getAllTransactions() {
        return transactionService.getAllTransactions();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Transactions> getTransactionById(@PathVariable long id) {
        return transactionService.getTransactionById(id);
    }

    @GetMapping("/get")
    public ResponseEntity<Transactions> getByTransactionId(@RequestParam String transactionId) {
        return transactionService.getByTransactionId(transactionId);
    }

    @PostMapping("")
    public ResponseEntity<Transactions> postTransaction(Transactions transaction) {
        return transactionService.postTransaction(transaction);
    }

}
