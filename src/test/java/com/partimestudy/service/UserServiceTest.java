package com.partimestudy.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import com.partimestudy.entity.User;
import com.partimestudy.exception.ApiException;
import com.partimestudy.repository.UserRepository;
import com.partimestudy.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;
class UserServiceTest {
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("사용자가 존재할 경우, 올바른 사용자 정보를 반환해야 한다")
    void getMyInfo_UserExists_ShouldReturnUserInfo() {
        // 사용자 객체 생성 및 설정
        User user = new User();
        user.setId(1L);
        user.setUsername("testUser");

        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));

        User result = userService.getMyInfo("testUser");

        assertEquals("testUser", result.getUsername());
        assertEquals(1L, result.getId());
    }

    @Test
    @DisplayName("사용자가 존재하지 않을 경우, 예외가 발생해야 한다")
    void getMyInfo_UserNotFound_ShouldThrowException() {
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());

        ApiException exception = assertThrows(ApiException.class, () ->
                userService.getMyInfo("unknownUser"));

        assertEquals("USER_NOT_FOUND", exception.getErrorCode().toString());
    }
}