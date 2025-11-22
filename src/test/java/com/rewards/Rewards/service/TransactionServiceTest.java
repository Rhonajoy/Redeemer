package com.rewards.Rewards.service;

import com.rewards.Rewards.dto.RedeemPointsRequestDto;
import com.rewards.Rewards.dto.TransactionDto;
import com.rewards.Rewards.dto.TransactionRequestDto;
import com.rewards.Rewards.entity.Transaction;
import com.rewards.Rewards.entity.TransactionType;
import com.rewards.Rewards.entity.User;
import com.rewards.Rewards.repository.TransactionRepository;
import com.rewards.Rewards.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class TransactionServiceTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private PointsService pointsService;

    @InjectMocks
    private TransactionService transactionService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testCreateTransaction_success() {
        TransactionRequestDto dto = new TransactionRequestDto();
        dto.setUserId(1L);
        dto.setAmount(500L);
        dto.setType(TransactionType.CREDIT);
        dto.setTransactionReference("ref1");

        User user = new User();
        user.setId(1L);

        Transaction savedTx = new Transaction();
        savedTx.setId(10L);
        savedTx.setAmount(500L);
        savedTx.setUser(user);
        savedTx.setType(TransactionType.CREDIT);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(transactionRepository.save(any())).thenReturn(savedTx);
        when(pointsService.awardPoints(user, 500L, "ref1")).thenReturn(5);

        TransactionDto result = transactionService.createTransaction(dto);

        assertEquals(5, result.getPointsEarned());
        verify(pointsService).awardPoints(user, 500L, "ref1");
    }

    @Test
    void testCreateTransaction_duplicateReference() {
        TransactionRequestDto dto = new TransactionRequestDto();
        dto.setTransactionReference("ref1");

        Transaction existing = new Transaction();
        existing.setId(99L);

        when(transactionRepository.existsByTransactionReference("ref1")).thenReturn(true);
        when(transactionRepository.findByTransactionReference("ref1")).thenReturn(existing);

        TransactionDto result = transactionService.createTransaction(dto);

        assertEquals(99L, result.getId());
        verify(pointsService, never()).awardPoints(any(), anyLong(), any());
    }

    @Test
    void testRedeemPoints_success() {
        RedeemPointsRequestDto dto = new RedeemPointsRequestDto();
        dto.setUserId(1L);
        dto.setPointsToRedeem(10);
        dto.setTransactionReference("refA");

        User user = new User();
        user.setId(1L);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(pointsService.redeemPoints(user, 10, "refA")).thenReturn(10);
        when(transactionRepository.save(any())).thenAnswer(i -> i.getArguments()[0]);

        TransactionDto result = transactionService.redeemPoints(dto);

        assertEquals(10, result.getPointsRedeemed());
        verify(pointsService).redeemPoints(user, 10, "refA");
    }
}
