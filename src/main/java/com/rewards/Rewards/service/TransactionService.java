package com.rewards.Rewards.service;

import com.rewards.Rewards.converter.DtoConverter;
import com.rewards.Rewards.dto.RedeemPointsRequestDto;
import com.rewards.Rewards.dto.TransactionDto;
import com.rewards.Rewards.dto.TransactionRequestDto;
import com.rewards.Rewards.entity.*;
import com.rewards.Rewards.repository.TransactionRepository;
import com.rewards.Rewards.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class TransactionService {

    private final UserRepository userRepository;
    private final TransactionRepository transactionRepository;

    public TransactionService(UserRepository userRepository, TransactionRepository transactionRepository) {
        this.userRepository = userRepository;
        this.transactionRepository = transactionRepository;
    }

    @Transactional
    public TransactionDto createTransaction(TransactionRequestDto request) {
        String reference = request.getTransactionReference();

        // 1️⃣ Idempotency check
        if (reference != null && transactionRepository.existsByTransactionReference(reference)) {
            Transaction existing = transactionRepository.findByTransactionReference(reference);
            return mapToDto(existing);
        }

        // 2️⃣ Fetch user
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // 3️⃣ Create transaction
        Transaction transaction = new Transaction();
        transaction.setAmount(request.getAmount());
        transaction.setUser(user);

        // ✅ Safe enum conversion
        try {
            TransactionType type = request.getType();
            if (type == null) {
                throw new RuntimeException("Transaction type is required");
            }
            transaction.setType(type);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid transaction type: " + request.getType());
        }

        // Use client-supplied reference if provided
        if (reference != null) {
            transaction.setTransactionReference(reference);
        }
        // else @PrePersist generates a UUID

        Transaction saved = transactionRepository.save(transaction);

        return mapToDto(saved);
    }

    private TransactionDto mapToDto(Transaction transaction) {
        TransactionDto dto = new TransactionDto();
        dto.setId(transaction.getId());
        dto.setTransactionReference(transaction.getTransactionReference());
        dto.setAmount(transaction.getAmount());
        dto.setPointsEarned(transaction.getPointsEarned());
        dto.setPointsRedeemed(transaction.getPointsRedeemed());
        dto.setTransactionType(transaction.getType());
        dto.setTimestamp(transaction.getCreatedAt());
        return dto;
    }



    @Transactional
    public TransactionDto redeemPoints(RedeemPointsRequestDto dto) {
        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));
        Wallet wallet = user.getWallet();
        PointsAccount pointsAccount = user.getPointsAccount();

        if(dto.getPointsToRedeem() > wallet.getWalletBalance()) {
            throw new RuntimeException("Insufficient wallet points to redeem");
        }


        wallet.setWalletBalance(wallet.getWalletBalance() - dto.getPointsToRedeem());
        pointsAccount.setPointsBalance((int) (pointsAccount.getPointsBalance() - dto.getPointsToRedeem()));
        Transaction transaction = new Transaction();
        transaction.setUser(user);
        transaction.setAmount(0L);
        transaction.setPointsEarned(0);
        transaction.setPointsRedeemed(dto.getPointsToRedeem());
//        transaction.setTransactionReference(dto.getTransactionReference());
        transaction.setType(TransactionType.REDEEM);
        transactionRepository.save(transaction);
        return DtoConverter.toTransactionDto(transaction);
    }

}
