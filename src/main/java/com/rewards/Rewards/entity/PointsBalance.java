package com.rewards.Rewards.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "points_balance")
public class PointsBalance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long availablePoints;

    @OneToOne
    @JoinColumn(name = "points_account_id")
    private PointsAccount pointsAccount;
}
