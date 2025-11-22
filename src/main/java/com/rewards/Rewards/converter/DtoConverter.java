package com.rewards.Rewards.converter;

import com.rewards.Rewards.dto.PointsAccountDto;
import com.rewards.Rewards.dto.TransactionDto;
import com.rewards.Rewards.dto.UserDto;
import com.rewards.Rewards.dto.WalletDto;
import com.rewards.Rewards.entity.PointsAccount;
import com.rewards.Rewards.entity.Transaction;
import com.rewards.Rewards.entity.User;
import com.rewards.Rewards.entity.Wallet;

public class DtoConverter {
    public static UserDto toUserDto(User user) {
        if (user == null) return null;
        UserDto dto = new UserDto();
        dto.setId(user.getId());
        dto.setEmail(user.getEmail());
        dto.setName(user.getName());
        dto.setWallet(toWalletDto(user.getWallet()));
        dto.setPointsAccount(toPointsAccountDto(user.getPointsAccount()));
        return dto;
    }

    public static WalletDto toWalletDto(Wallet wallet) {
        if (wallet == null) return null;
        WalletDto dto = new WalletDto();
        dto.setId(wallet.getId());
        dto.setWalletBalance(wallet.getWalletBalance());
        return dto;
    }

    public static PointsAccountDto toPointsAccountDto(PointsAccount account) {
        if (account == null) return null;
        PointsAccountDto dto = new PointsAccountDto();
        dto.setId(account.getId());
        dto.setPointsBalance(account.getPointsBalance());
        return dto;
    }
    public static TransactionDto toTransactionDto(Transaction transaction) {
        if (transaction == null) return null;

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

    public static WalletDto toWalletDto(Wallet wallet, PointsAccount pointsAccount) {
        if (wallet == null) return null;
        WalletDto dto = new WalletDto();
        dto.setId(wallet.getId());
        dto.setWalletBalance(wallet.getWalletBalance());
        Long totalCredits = 0L;
        if (pointsAccount != null) {
            totalCredits = (long) pointsAccount.getPointsBalance();
        }
        dto.setTotalCredits(totalCredits);
        dto.setTotalRedeemedMoney(0L);
        return dto;
    }
}

