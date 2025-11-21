package com.rewards.Rewards.dto;

import lombok.Data;

@Data
public class CreateUserRequest {
    private String email;
    private String name;
}
