package com.mariam.BankingApplication.repository;

import com.mariam.BankingApplication.model.AccountUser;
import com.mariam.BankingApplication.model.KnowYourCustomer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface KYCRepository extends JpaRepository<KnowYourCustomer, Integer> {
    KnowYourCustomer getByAccountUser(AccountUser accountUser);
}
