package com.rewards.Rewards.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EarnPointsRequestDto {
    private Long userId;
    private Long transactionAmount;
    private String transactionReference;
}
