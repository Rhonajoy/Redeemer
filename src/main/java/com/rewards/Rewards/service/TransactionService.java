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
    public TransactionDto createTransaction(TransactionRequestDto dto) {
        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        int pointsEarned = (int) (dto.getAmount() / 100);
        Wallet wallet = user.getWallet();
        wallet.setWalletBalance(wallet.getWalletBalance() + pointsEarned);
        PointsAccount pointsAccount = user.getPointsAccount();
        pointsAccount.setPointsBalance(pointsAccount.getPointsBalance() + pointsEarned);
        Transaction transaction = new Transaction();
        transaction.setUser(user);
        transaction.setAmount(dto.getAmount());
        transaction.setPointsEarned(pointsEarned);
        transaction.setPointsRedeemed(0);
        transaction.setTransactionReference(dto.getReferenceId());
        transaction.setType(TransactionType.CREDIT); // transaction type
        transactionRepository.save(transaction);
        return DtoConverter.toTransactionDto(transaction);
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
        transaction.setTransactionReference(dto.getTransactionReference());
        transaction.setType(TransactionType.REDEEM);
        transactionRepository.save(transaction);
        return DtoConverter.toTransactionDto(transaction);
    }

}
