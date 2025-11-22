package com.rewards.Rewards.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WalletDto {
    private Long Id;
    private Long walletBalance;
    private Long totalCredits;
    private Long totalRedeemedMoney;
}
