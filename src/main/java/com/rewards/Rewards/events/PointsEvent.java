package com.rewards.Rewards.events;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PointsEvent {
    private Long userId;
    private int pointsChange;
    private String transactionReference;
}
