package com.mariam.BankingApplication.service;

import com.mariam.BankingApplication.exception.TransactionException;
import com.mariam.BankingApplication.model.AccountUser;
import com.mariam.BankingApplication.model.BankAccount;
import com.mariam.BankingApplication.model.TransactionType;
import com.mariam.BankingApplication.model.Transactions;
import jakarta.mail.MessagingException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class BankingOperationService {

    @Autowired
    private BankAccountService bankAccountService;

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private MessageService messageService;

    @Autowired
    AccountUserService accountUserService;

    @Autowired
    JWTService jwtService;

    public ResponseEntity<BankAccount> depositFund(String accountNumber, double amount) throws MessagingException {
        if( amount < 1 ){
            throw new TransactionException("You can not deposit negative amount");
        }
        BankAccount account = bankAccountService.getByAccountNumber(accountNumber).getBody();
        assert account != null;

        account.setAccountBalance(amount + account.getAccountBalance());

        Transactions transaction = new Transactions();
        transaction.setAccountNumber(accountNumber);
        transaction.setAmount(amount);
        transaction.setTransactionType(TransactionType.Deposit);
        transactionService.postTransaction(transaction);
        AccountUser user = accountUserService.getUserById(account.getAccountUser().getId()).getBody();
        assert user != null;
        messageService.depositNotification(user.getFirstName(), user.getUsername(), amount);
        return new ResponseEntity<>(bankAccountService.updateAccount(account).getBody(), HttpStatus.OK);
    }

    public ResponseEntity<BankAccount> withdrawFund(String accountNumber, double amount) throws MessagingException {
        if( amount < 1 ){
            throw new TransactionException("You can not deposit negative amount");
        }
        BankAccount account = bankAccountService.getByAccountNumber(accountNumber).getBody();
        assert account != null;

        if( account.getAccountBalance() < amount ){
            throw new TransactionException("Insufficient Balance");
        }
        account.setAccountBalance(account.getAccountBalance() - amount);

        Transactions transaction = new Transactions();
        transaction.setAccountNumber(accountNumber);
        transaction.setAmount(amount);
        transaction.setTransactionType(TransactionType.Withdrawal);
        transactionService.postTransaction(transaction);
        AccountUser user = accountUserService.getUserById(account.getAccountUser().getId()).getBody();
        assert user != null;
        messageService.withdrawalNotification(user.getFirstName(), user.getUsername(), amount);
        return new ResponseEntity<>(bankAccountService.updateAccount(account).getBody(), HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<String> transferFunds(String accountFrom, String accountTo, double amount ) throws MessagingException {
        try{
            withdrawFund(accountFrom, amount);
            depositFund(accountTo, amount);
            return new ResponseEntity<>("Transaction Successful", HttpStatus.OK);
        } catch (TransactionException transactionException){
            System.out.println(transactionException.getMessage());
        }
        return new ResponseEntity<>("Transaction failed", HttpStatus.NOT_ACCEPTABLE);
    }
}
