package com.rewards.Rewards.service;

import com.rewards.Rewards.converter.DtoConverter;
import com.rewards.Rewards.dto.PointsAccountDto;
import com.rewards.Rewards.dto.UserDto;
import com.rewards.Rewards.dto.WalletDto;
import com.rewards.Rewards.entity.PointsAccount;
import com.rewards.Rewards.entity.User;
import com.rewards.Rewards.entity.Wallet;
import com.rewards.Rewards.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // Create user + wallet + points account automatically
    @Transactional
    public UserDto createUser(String email, String name) {
        User user = new User();
        user.setEmail(email);
        user.setName(name);
        user.setCreatedAt(LocalDateTime.now());

        // Wallet
        Wallet wallet = new Wallet();
        wallet.setUser(user);
        wallet.setWalletBalance(0L);

        // Points account
        PointsAccount pointsAccount = new PointsAccount();
        pointsAccount.setUser(user);
        pointsAccount.setPointsBalance(0);

        user.setWallet(wallet);
        user.setPointsAccount(pointsAccount);

        User savedUser = userRepository.save(user);
        return DtoConverter.toUserDto(savedUser);
    }

    // Get user with wallet info
    public UserDto getUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return DtoConverter.toUserDto(user);
    }

    // Get only wallet balance
    public WalletDto getWalletBalance(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return DtoConverter.toWalletDto(user.getWallet());
    }

    // Get points balance
    public PointsAccountDto getPointsBalance(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return DtoConverter.toPointsAccountDto(user.getPointsAccount());
    }
}
