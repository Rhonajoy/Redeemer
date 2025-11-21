package com.rewards.Rewards.repository;

import com.rewards.Rewards.entity.PointsBalance;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

public interface PointsBalanceRepository extends JpaRepository<PointsBalance,Integer> {

}
