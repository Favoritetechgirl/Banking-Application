package com.mariam.BankingApplication.service;

import com.mariam.BankingApplication.model.AccountUser;
import com.mariam.BankingApplication.model.BankAccount;
import com.mariam.BankingApplication.repository.BankAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;

@Service
public class BankAccountService {

    @Autowired
    private BankAccountRepository bankAccountRepository;

    public ResponseEntity<List<BankAccount>> getAllBankAccounts(){
        return new ResponseEntity<>(bankAccountRepository.findAll(), HttpStatus.OK);
    }

    public ResponseEntity<BankAccount> getById(int id){
        return new ResponseEntity<>(bankAccountRepository.findById(id).get(), HttpStatus.OK);
    }

    public ResponseEntity<BankAccount> getByUser(AccountUser user){
        return new ResponseEntity<>(bankAccountRepository.findByAccountUser(user), HttpStatus.OK);
    }

    public ResponseEntity<BankAccount> updateAccount(BankAccount bankAccount){
        return new ResponseEntity<>(bankAccountRepository.save(bankAccount), HttpStatus.OK);
    }

    public ResponseEntity<BankAccount> getByAccountNumber(String accountNumber){
        return new ResponseEntity<>(bankAccountRepository.findByAccountNumber(accountNumber), HttpStatus.OK);
    }

    public ResponseEntity<BankAccount> createBankAccount(AccountUser accountUser, double openingAmount ){
        StringBuilder accountNumber = new StringBuilder();
        int count = 0;
        while(count < 10){
            int randomInt = new Random().nextInt(10);
            accountNumber.append(randomInt);
            count++;
        }
        BankAccount account = new BankAccount(accountUser, openingAmount, accountNumber.toString());
        return new ResponseEntity<>(bankAccountRepository.save(account), HttpStatus.CREATED);
    }

    public ResponseEntity<BankAccount> createBankAccount(AccountUser accountUser ){
        StringBuilder accountNumber = new StringBuilder();
        int count = 0;
        while(count < accountNumber.length()){
            int randomInt = new Random().nextInt(10);
            accountNumber.append(randomInt);
        }
        BankAccount account = new BankAccount(accountUser, 0.00, accountNumber.toString());
        return new ResponseEntity<>(bankAccountRepository.save(account), HttpStatus.CREATED);
    }

}

