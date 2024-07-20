package com.mariam.BankingApplication.repository;

import com.mariam.BankingApplication.model.AccountUser;
import com.mariam.BankingApplication.model.ResetCode;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ResetCodeRepository extends JpaRepository<ResetCode, Integer> {

    ResetCode findByAccountUser(AccountUser accountUser);
}
