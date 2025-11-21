package com.rewards.Rewards.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RedeemPointsRequestDto {
    private Long userId;
    private int pointsToRedeem;
    private String transactionReference;
}
