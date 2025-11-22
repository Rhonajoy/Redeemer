package com.rewards.Rewards.controller;

import com.rewards.Rewards.config.ApiConfig;
import com.rewards.Rewards.dto.RedeemPointsRequestDto;
import com.rewards.Rewards.dto.TransactionDto;
import com.rewards.Rewards.dto.TransactionRequestDto;
import com.rewards.Rewards.service.TransactionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(TransactionController.PATH)
public class TransactionController {
    private final TransactionService transactionService;
    public static final String PATH = ApiConfig.BASE_API_PATH + "transactions";
    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping("/credit")
    public ResponseEntity<TransactionDto> creditAccount(
            @RequestHeader(value = "Idempotency-Key", required = false) String idempotencyKey,
            @RequestBody TransactionRequestDto request) {

        if (idempotencyKey != null && !idempotencyKey.isBlank()) {
            request.setTransactionReference(idempotencyKey);
        }
        TransactionDto transaction = transactionService.createTransaction(request);
        return ResponseEntity.ok(transaction);
    }

    @PostMapping("/redeem")
    public ResponseEntity<TransactionDto> redeemPoints(@RequestBody RedeemPointsRequestDto redeemPointsRequestDto) {
        return ResponseEntity.ok(transactionService.redeemPoints(redeemPointsRequestDto));
    }
}
