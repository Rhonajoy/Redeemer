package com.rewards.Rewards.dto;

import com.rewards.Rewards.entity.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionDto {
    private Long id;
    private String transactionReference;
    private Long amount;
    private TransactionType transactionType;
    private int pointsEarned;
    private int pointsRedeemed;
    private LocalDateTime timestamp;
}
