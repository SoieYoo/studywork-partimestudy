package com.partimestudy.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import com.partimestudy.dto.JoinDTO;
import com.partimestudy.entity.User;
import com.partimestudy.exception.ApiException;
import com.partimestudy.repository.UserRepository;
import com.partimestudy.service.JoinService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;
class JoinServiceTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @InjectMocks
    private JoinService joinService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("이미 존재하는 사용자 이름이 있을 경우 예외가 발생해야 한다")
    void joinProcess_AlreadyExistUsername_ShouldThrowException() {
        JoinDTO joinDTO = new JoinDTO();
        joinDTO.setUsername("existingUser");
        joinDTO.setPassword("password");
        joinDTO.setNickname("nickname");
        joinDTO.setStudyGoal("studyGoal");

        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(new User()));

        ApiException exception = assertThrows(ApiException.class, () ->
                joinService.joinProcess(joinDTO));

        assertEquals("ALREADY_EXIST_USERNAME", exception.getErrorCode().toString());
    }

    @Test
    @DisplayName("새로운 사용자가 정상적으로 등록되어야 한다")
    void joinProcess_NewUser_ShouldSaveUser() {
        JoinDTO joinDTO = new JoinDTO();
        joinDTO.setUsername("newUser");
        joinDTO.setPassword("password");
        joinDTO.setNickname("nickname");
        joinDTO.setStudyGoal("studyGoal");

        when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());
        when(bCryptPasswordEncoder.encode(anyString())).thenReturn("encodedPassword");

        joinService.joinProcess(joinDTO);

        verify(userRepository, times(1)).save(any(User.class));
    }
}