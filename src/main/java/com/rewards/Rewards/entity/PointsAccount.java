package com.rewards.Rewards.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "points_account",
        uniqueConstraints = @UniqueConstraint(columnNames = "relatedTxId"))
public class PointsAccount {
    @Id
    @GeneratedValue
    private Long id;
    @Column(nullable=false)
    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;
    @Enumerated(EnumType.STRING)
    private TransactionType type;
    private int amountPoints;
    @Column(nullable = false)
    private String relatedTxId;
    private LocalDateTime createdAt;

    private int pointsBalance;
}
