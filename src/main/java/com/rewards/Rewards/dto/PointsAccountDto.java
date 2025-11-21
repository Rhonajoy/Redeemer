package com.rewards.Rewards.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PointsAccountDto {
    private Long id;
    private int pointsBalance;
}
