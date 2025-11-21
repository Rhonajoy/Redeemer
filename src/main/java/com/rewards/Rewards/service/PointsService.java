package com.rewards.Rewards.service;

import com.rewards.Rewards.converter.DtoConverter;
import com.rewards.Rewards.dto.TransactionDto;
import com.rewards.Rewards.entity.PointsAccount;
import com.rewards.Rewards.entity.Transaction;
import com.rewards.Rewards.events.PointsEvent;
import com.rewards.Rewards.converter.TransactionMapper;
import com.rewards.Rewards.repository.PointsAccountRepository;
import com.rewards.Rewards.repository.TransactionRepository;
import jakarta.transaction.Transactional;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class PointsService {
    private final TransactionRepository transactionRepository;
    private final PointsAccountRepository pointsAccountRepository;
    private final KafkaTemplate<String, PointsEvent> kafkaTemplate;

    public PointsService(TransactionRepository transactionRepository,
                         PointsAccountRepository pointsAccountRepository,
                         KafkaTemplate<String, PointsEvent> kafkaTemplate) {
        this.transactionRepository = transactionRepository;
        this.pointsAccountRepository = pointsAccountRepository;
        this.kafkaTemplate = kafkaTemplate;
    }

    // =========================
    // Get points balance (cached)
    // =========================
    @Cacheable(value = "pointsBalance", key = "#userId")
    public int getCurrentPointsBalance(Long userId) {
        PointsAccount account = pointsAccountRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Points account not found"));
        return account.getPointsBalance();
    }

    // =========================
    // Earn points
    // =========================
    @Transactional
    @CacheEvict(value = "pointsBalance", key = "#userId")
    public TransactionDto earnPoints(Long userId, Long amount, String referenceId) {
        return processTransaction(userId, amount, 0, referenceId, true);
    }

    // =========================
    // Redeem points
    // =========================
    @Transactional
    @CacheEvict(value = "pointsBalance", key = "#userId")
    public TransactionDto redeemPoints(Long userId, int pointsToRedeem, String referenceId) {
        if (pointsToRedeem <= 0) {
            throw new IllegalArgumentException("Points to redeem must be positive");
        }
        return processTransaction(userId, 0L, pointsToRedeem, referenceId, false);
    }

    // =========================
    // Common transaction logic
    // =========================
    private TransactionDto processTransaction(Long userId, Long amount, int pointsToRedeem,
                                              String referenceId, boolean isEarning) {
        // Idempotency check
        if (transactionRepository.existsByTransactionReference(referenceId)) {
            throw new RuntimeException("Transaction already processed: " + referenceId);
        }

        PointsAccount account = pointsAccountRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Points account not found"));

        int currentBalance = account.getPointsBalance();
        int pointsEarned = isEarning ? calculatePoints(amount) : 0;

        if (!isEarning && pointsToRedeem > currentBalance) {
            throw new RuntimeException("Insufficient points to redeem");
        }

        int newBalance = currentBalance + pointsEarned - pointsToRedeem;
        account.setPointsBalance(newBalance);
        pointsAccountRepository.save(account);

        // Save transaction
        Transaction transaction = new Transaction();
        transaction.setUser(account.getUser());
        transaction.setAmount(amount);
        transaction.setPointsEarned(pointsEarned);
        transaction.setPointsRedeemed(pointsToRedeem);
        transaction.setCreatedAt(LocalDateTime.now());
        transaction.setTransactionReference(referenceId);
        transactionRepository.save(transaction);

        // Publish Kafka event
        int pointsChange = isEarning ? pointsEarned : -pointsToRedeem;
        kafkaTemplate.send("points-events", new PointsEvent(userId, pointsChange, referenceId));

        return DtoConverter.toTransactionDto(transaction);
    }

    private int calculatePoints(Long amount) {
        return (int) (amount / 100); // 1 point per 100 units spent
    }

}
