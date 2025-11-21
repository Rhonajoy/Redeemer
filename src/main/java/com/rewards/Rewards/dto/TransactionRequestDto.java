package com.rewards.Rewards.dto;

import com.rewards.Rewards.entity.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionRequestDto {
    private Long amount;
    private String transactionReference;
    private TransactionType type;
    private Long userId;
}
