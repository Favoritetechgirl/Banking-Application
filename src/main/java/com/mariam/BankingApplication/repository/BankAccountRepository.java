package com.mariam.BankingApplication.repository;

import com.mariam.BankingApplication.model.AccountUser;
import com.mariam.BankingApplication.model.BankAccount;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BankAccountRepository extends JpaRepository<BankAccount, Integer> {
    BankAccount findByAccountUser(AccountUser user);

    BankAccount findByAccountNumber(String accountNumber);
}
