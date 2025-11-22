package com.rewards.Rewards.service;

import com.rewards.Rewards.entity.PointsAccount;
import com.rewards.Rewards.entity.User;
import com.rewards.Rewards.entity.Wallet;
import com.rewards.Rewards.events.PointsEvent;
import com.rewards.Rewards.exceptions.BadRequestException;
import com.rewards.Rewards.exceptions.ResourceNotFoundException;
import com.rewards.Rewards.repository.PointsAccountRepository;
import com.rewards.Rewards.repository.WalletRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class PointServiceTest {
    @Mock
    private PointsAccountRepository pointsAccountRepository;
    @Mock
    private WalletRepository walletRepository;
    @Mock
    private KafkaTemplate<String, String> kafkaTemplate;
    @InjectMocks
    private PointsService pointsService;
    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }
    @Test
    void testGetBalance_success() {
        PointsAccount account = new PointsAccount();
        account.setPointsBalance(50);
        when(pointsAccountRepository.findById(1L)).thenReturn(Optional.of(account));
        int balance = pointsService.getBalance(1L);
        assertEquals(50, balance);
    }

    @Test
    void testGetBalance_notFound() {
        when(pointsAccountRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> pointsService.getBalance(1L));
    }

    @Test
    void testAwardPoints_success() {
        User user = new User();
        user.setId(1L);

        Wallet wallet = new Wallet();
        wallet.setWalletBalance(0L);

        PointsAccount account = new PointsAccount();
        account.setPointsBalance(0);

        user.setWallet(wallet);
        user.setPointsAccount(account);

        int result = pointsService.awardPoints(user, 500L, "ref123");

        assertEquals(5, result);
        assertEquals(500L, wallet.getWalletBalance());
        assertEquals(5, account.getPointsBalance());

        verify(pointsAccountRepository).save(account);
        verify(walletRepository).save(wallet);
        verify(kafkaTemplate).send(eq("points-events"), String.valueOf(ArgumentMatchers.any(PointsEvent.class)));
    }

    @Test
    void testRedeemPoints_insufficient() {
        User user = new User();
        PointsAccount account = new PointsAccount();
        account.setPointsBalance(3);

        Wallet wallet = new Wallet();
        wallet.setWalletBalance(0L);

        user.setWallet(wallet);
        user.setPointsAccount(account);

        assertThrows(BadRequestException.class,
                () -> pointsService.redeemPoints(user, 5, "ref"));
    }

    @Test
    void testRedeemPoints_success() {
        User user = new User();
        user.setId(1L);

        PointsAccount account = new PointsAccount();
        account.setPointsBalance(10);

        Wallet wallet = new Wallet();
        wallet.setWalletBalance(0L);

        user.setWallet(wallet);
        user.setPointsAccount(account);

        int redeemed = pointsService.redeemPoints(user, 5, "ref123");

        assertEquals(5, redeemed);
        assertEquals(5, account.getPointsBalance());
        assertEquals(5L, wallet.getWalletBalance());
        assertEquals(5L, wallet.getTotalRedeemedMoney());

        verify(walletRepository).save(wallet);
        verify(pointsAccountRepository).save(account);
        verify(kafkaTemplate).send(eq("points-events"), String.valueOf(ArgumentMatchers.any(PointsEvent.class)));
    }
}
