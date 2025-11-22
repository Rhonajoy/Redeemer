package com.rewards.Rewards.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "wallets")
public class Wallet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private Long walletBalance=0L;
    @Column(nullable = false)
    private Long totalRedeemedMoney=0L;
    @OneToOne
    @JoinColumn(name="user_id", nullable=false, unique=true)
    private User user;
}
