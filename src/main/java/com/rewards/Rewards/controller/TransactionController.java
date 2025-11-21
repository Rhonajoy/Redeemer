package com.rewards.Rewards.controller;

import com.rewards.Rewards.dto.RedeemPointsRequestDto;
import com.rewards.Rewards.dto.TransactionDto;
import com.rewards.Rewards.dto.TransactionRequestDto;
import com.rewards.Rewards.service.TransactionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {
    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping("/credit")
    public ResponseEntity<TransactionDto> creditAccount(@RequestBody TransactionRequestDto dto) {
        return ResponseEntity.ok(transactionService.createTransaction(dto));
    }

    @PostMapping("/redeem")
    public ResponseEntity<TransactionDto> redeemPoints(@RequestBody RedeemPointsRequestDto dto) {
        return ResponseEntity.ok(transactionService.redeemPoints(dto));
    }
}
