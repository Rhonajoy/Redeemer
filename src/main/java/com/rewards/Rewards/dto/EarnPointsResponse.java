package com.rewards.Rewards.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EarnPointsResponse {
    private String transactionReference;
    private Long transactionId;
    private Long transactionAmount;
    private Long pointsEarned;
    private Long newPointsBalance;
    private LocalDateTime timestamp;
}
