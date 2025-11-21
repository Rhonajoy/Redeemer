package com.rewards.Rewards.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RedeemPointsResponse {
    private String transactionReference;
    private Long transactionId;
    private Long pointsRedeemed;
    private Long amountCredited;
    private Long newPointsBalance;
    private Long newWalletBalance;
    private LocalDateTime timestamp;
}
