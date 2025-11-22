package com.rewards.Rewards.controller;

import com.rewards.Rewards.config.ApiConfig;
import com.rewards.Rewards.dto.CreateUserRequest;
import com.rewards.Rewards.dto.PointsAccountDto;
import com.rewards.Rewards.dto.UserDto;
import com.rewards.Rewards.dto.WalletDto;
import com.rewards.Rewards.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping(UserController.PATH)
public class UserController {
    private final UserService userService;
    public static final String PATH = ApiConfig.BASE_API_PATH + "users";
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/create")
    public ResponseEntity<UserDto> createUser(@RequestBody CreateUserRequest createUserRequest  ) {
        UserDto dto = userService.createUser(createUserRequest.getEmail(), createUserRequest.getName());
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/retrieve/{id}")
    public ResponseEntity<UserDto> getUser(@PathVariable ("id")Long id) {
        UserDto dto = userService.getUser(id);
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/wallet/{id}")
    public ResponseEntity<WalletDto> getWalletBalance(@PathVariable ("id") Long id) {
        WalletDto dto = userService.getWalletBalance(id);
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/points/{id}")
    public ResponseEntity<PointsAccountDto> getPointsBalance(@PathVariable ("id")Long id) {
        PointsAccountDto dto = userService.getPointsBalance(id);
        return ResponseEntity.ok(dto);
    }
}
