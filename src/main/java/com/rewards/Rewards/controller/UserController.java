package com.rewards.Rewards.controller;

import com.rewards.Rewards.dto.PointsAccountDto;
import com.rewards.Rewards.dto.UserDto;
import com.rewards.Rewards.dto.WalletDto;
import com.rewards.Rewards.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // Create user (automatically creates wallet & points account)
    @PostMapping("/create")
    public ResponseEntity<UserDto> createUser(@RequestParam String email,
                                              @RequestParam String name) {
        UserDto dto = userService.createUser(email, name);
        return ResponseEntity.ok(dto);
    }

    // Get user details (includes wallet & points)
    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUser(@PathVariable Long id) {
        UserDto dto = userService.getUser(id);
        return ResponseEntity.ok(dto);
    }

    // Get wallet balance only
    @GetMapping("/{id}/wallet")
    public ResponseEntity<WalletDto> getWalletBalance(@PathVariable Long id) {
        WalletDto dto = userService.getWalletBalance(id);
        return ResponseEntity.ok(dto);
    }

    // Get points balance only
    @GetMapping("/{id}/points")
    public ResponseEntity<PointsAccountDto> getPointsBalance(@PathVariable Long id) {
        PointsAccountDto dto = userService.getPointsBalance(id);
        return ResponseEntity.ok(dto);
    }
}
