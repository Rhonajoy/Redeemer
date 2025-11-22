package com.rewards.Rewards.repository;

import com.rewards.Rewards.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.UUID;
@Repository
public interface TransactionRepository extends JpaRepository<Transaction, UUID> {

    boolean existsByTransactionReference(String transactionReference);

    Transaction findByTransactionReference(String transactionReference);
}
