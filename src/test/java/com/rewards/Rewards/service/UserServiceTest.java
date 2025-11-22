package com.rewards.Rewards.service;

import com.rewards.Rewards.dto.UserDto;
import com.rewards.Rewards.entity.User;
import com.rewards.Rewards.exceptions.ResourceNotFoundException;
import com.rewards.Rewards.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testCreateUser_success() {
        User saved = new User();
        saved.setId(10L);
        saved.setEmail("test@example.com");
        saved.setName("John");

        when(userRepository.save(any())).thenReturn(saved);

        UserDto result = userService.createUser("test@example.com", "John");

        assertEquals(10L, result.getId());
    }

    @Test
    void testGetUser_success() {
        User user = new User();
        user.setId(1L);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        UserDto dto = userService.getUser(1L);

        assertEquals(1L, dto.getId());
    }

    @Test
    void testGetUser_notFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> userService.getUser(1L));
    }
}
