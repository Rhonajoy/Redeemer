package com.rewards.Rewards.controller;

import com.rewards.Rewards.dto.CreateUserRequest;
import com.rewards.Rewards.dto.PointsAccountDto;
import com.rewards.Rewards.dto.UserDto;
import com.rewards.Rewards.dto.WalletDto;
import com.rewards.Rewards.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/v1/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/create")
    public ResponseEntity<UserDto> createUser(@RequestBody CreateUserRequest createUserRequest  ) {
        UserDto dto = userService.createUser(createUserRequest.getEmail(), createUserRequest.getName());
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUser(@PathVariable Long id) {
        UserDto dto = userService.getUser(id);
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/{id}/wallet")
    public ResponseEntity<WalletDto> getWalletBalance(@PathVariable Long id) {
        WalletDto dto = userService.getWalletBalance(id);
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/{id}/points")
    public ResponseEntity<PointsAccountDto> getPointsBalance(@PathVariable Long id) {
        PointsAccountDto dto = userService.getPointsBalance(id);
        return ResponseEntity.ok(dto);
    }
}
