package com.rewards.Rewards.repository;

import com.rewards.Rewards.entity.PointsBalance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PointsBalanceRepository extends JpaRepository<PointsBalance,Integer> {

}
