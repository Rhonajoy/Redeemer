package com.rewards.Rewards.service;

import com.rewards.Rewards.converter.DtoConverter;
import com.rewards.Rewards.dto.RedeemPointsRequestDto;
import com.rewards.Rewards.dto.TransactionDto;
import com.rewards.Rewards.dto.TransactionRequestDto;
import com.rewards.Rewards.entity.*;
import com.rewards.Rewards.exceptions.BadRequestException;
import com.rewards.Rewards.exceptions.ResourceNotFoundException;
import com.rewards.Rewards.repository.TransactionRepository;
import com.rewards.Rewards.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final UserRepository userRepository;
    private final TransactionRepository transactionRepository;
    private final PointsService pointsService;



    @Transactional
    public TransactionDto createTransaction(TransactionRequestDto request) {
        String reference = request.getTransactionReference();
        if (reference != null && transactionRepository.existsByTransactionReference(reference)) {
            Transaction existing = transactionRepository.findByTransactionReference(reference);
            return DtoConverter.toTransactionDto(existing);
        }

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        Transaction transaction = new Transaction();
        transaction.setAmount(request.getAmount());
        transaction.setUser(user);

        try {
            TransactionType type = request.getType();
            if (type == null) {
                throw new BadRequestException("Transaction type is required");
            }
            transaction.setType(type);
        } catch (IllegalArgumentException e) {
            throw new BadRequestException("Invalid transaction type: " + request.getType());
        }
        if (reference != null) {
            transaction.setTransactionReference(reference);
        }
        Transaction saved = transactionRepository.save(transaction);
        int pointsEarned = pointsService.awardPoints(user, request.getAmount(), reference);
        saved.setPointsEarned(pointsEarned);
        transactionRepository.save(saved);
        return DtoConverter.toTransactionDto(saved);
    }

    @Transactional
    public TransactionDto redeemPoints(RedeemPointsRequestDto request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        String reference = request.getTransactionReference();
        if (reference != null && transactionRepository.existsByTransactionReference(reference)) {
            return DtoConverter.toTransactionDto(transactionRepository.findByTransactionReference(reference));

        }
        int redeemed = pointsService.redeemPoints(user, request.getPointsToRedeem(), reference);
        Transaction tx = new Transaction();
        tx.setUser(user);
        tx.setType(TransactionType.REDEEM);
        tx.setAmount(0L);
        tx.setPointsEarned(0);
        tx.setPointsRedeemed(redeemed);
        tx.setTransactionReference(reference);
        transactionRepository.save(tx);
        return DtoConverter.toTransactionDto(tx);
    }

}
