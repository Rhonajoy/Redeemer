package com.rewards.Rewards.service;

import com.rewards.Rewards.entity.PointsAccount;
import com.rewards.Rewards.entity.User;
import com.rewards.Rewards.entity.Wallet;
import com.rewards.Rewards.events.PointsEvent;
import com.rewards.Rewards.exceptions.BadRequestException;
import com.rewards.Rewards.exceptions.ResourceNotFoundException;
import com.rewards.Rewards.repository.PointsAccountRepository;
import com.rewards.Rewards.repository.WalletRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PointsService {
    private final  PointsAccountRepository pointsAccountRepository;
    private  final  WalletRepository walletRepository;
    private final KafkaTemplate<String, PointsEvent> pointsKafkaTemplate;

    @Cacheable(value = "pointsBalance", key = "#p0")
    public int getBalance(Long userId) {
        return pointsAccountRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Points account missing"))
                .getPointsBalance();
    }

    @Transactional
    @CacheEvict(value = "pointsBalance", key = "#p0.id")
    public int awardPoints(User user, Long amount, String referenceId) {
        int points = calculatePoints(amount);
        PointsAccount account = user.getPointsAccount();
        Wallet wallet = user.getWallet();
        wallet.setWalletBalance(wallet.getWalletBalance() + amount);
        account.setPointsBalance(account.getPointsBalance() + points);
        pointsAccountRepository.save(account);
        walletRepository.save(wallet);
        pointsKafkaTemplate.send("points-events",
                new PointsEvent(user.getId(), points, referenceId));

        return points;
    }

    @Transactional
    @CacheEvict(value = "pointsBalance", key = "#p0.id")
    public int redeemPoints(User user, int points, String referenceId) {
        PointsAccount account = user.getPointsAccount();
        Wallet wallet = user.getWallet();
        if (points > account.getPointsBalance()) {
            throw new BadRequestException("Insufficient points balance");
        }
        account.setPointsBalance(account.getPointsBalance() - points);
        int moneyGained = points;
        wallet.setWalletBalance(wallet.getWalletBalance() + moneyGained);
        wallet.setTotalRedeemedMoney(wallet.getTotalRedeemedMoney() + moneyGained);
        walletRepository.save(wallet);
        pointsAccountRepository.save(account);
        pointsKafkaTemplate.send("points-events",
                new PointsEvent(user.getId(), -points, referenceId));

        return points;
    }

    private int calculatePoints(Long amount) {
        return (int) (amount / 100);
    }

}
