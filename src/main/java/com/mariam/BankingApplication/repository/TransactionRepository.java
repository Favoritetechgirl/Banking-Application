package com.mariam.BankingApplication.repository;

import com.mariam.BankingApplication.model.Transactions;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends JpaRepository<Transactions, Long> {
    Transactions findByTransactionId(String transactionId);
}
