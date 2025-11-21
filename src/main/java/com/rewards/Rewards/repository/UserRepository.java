package com.rewards.Rewards.repository;

import com.rewards.Rewards.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
