package com.rewards.Rewards.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(
        name = "transactions",
        uniqueConstraints = @UniqueConstraint(columnNames = {"transaction_reference"})
)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "transaction_reference", nullable = false, unique = true, updatable = false)
    private String transactionReference;
    private Long amount;
    private int pointsEarned;
    private int pointsRedeemed;
    @Enumerated(EnumType.STRING)
    private TransactionType type;
    private LocalDateTime createdAt;
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        if (this.transactionReference == null) {
            this.transactionReference = UUID.randomUUID().toString();
        }
    }
}
