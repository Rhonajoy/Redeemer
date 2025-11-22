package com.rewards.Rewards.response;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ErrorResponse(Integer status, String message, String stackTrace) {
    public ErrorResponse(Integer status, String message) {
        this(status, message, null);
    }
}