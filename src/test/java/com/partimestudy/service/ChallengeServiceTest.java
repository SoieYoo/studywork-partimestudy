package com.partimestudy.service;

import static org.junit.jupiter.api.Assertions.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import com.partimestudy.dto.ChallengeApplicationDTO;
import com.partimestudy.dto.ChallengeScheduleDTO;
import com.partimestudy.entity.Challenge;
import com.partimestudy.entity.User;
import com.partimestudy.entity.UserChallenge;
import com.partimestudy.exception.ApiException;
import com.partimestudy.repository.ChallengeRepository;
import com.partimestudy.repository.UserChallengeRepository;
import com.partimestudy.repository.UserRepository;
import com.partimestudy.service.ChallengeService;
import com.partimestudy.type.ChallengeStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Optional;

class ChallengeServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private ChallengeRepository challengeRepository;

    @Mock
    private UserChallengeRepository userChallengeRepository;

    @InjectMocks
    private ChallengeService challengeService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("사용자를 찾을 수 없는 경우 예외가 발생해야 한다")
    void applyForChallenge_UserNotFound_ShouldThrowException() {
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());

        ApiException exception = assertThrows(ApiException.class, () ->
                challengeService.applyForChallenge("username", new ChallengeApplicationDTO()));

        assertEquals("USER_NOT_FOUND", exception.getErrorCode().toString());
    }

    @Test
    @DisplayName("사용자가 이미 챌린지에 등록된 경우 예외가 발생해야 한다")
    void applyForChallenge_UserAlreadyEnrolled_ShouldThrowException() {
        User user = new User();
        user.setId(1L);

        Challenge challenge = new Challenge();
        challenge.setId(1L);
        challenge.setChallengeName("Test Challenge");
        challenge.setStatus(ChallengeStatus.ACTIVE);
        challenge.setMinDeposit(10000);
        challenge.setMaxDeposit(50000);

        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));
        when(userChallengeRepository.existsByUserIdAndChallengeId(user.getId(), challenge.getId())).thenReturn(true);
        when(challengeRepository.findById(challenge.getId())).thenReturn(Optional.of(challenge));

        ChallengeApplicationDTO applicationDTO = new ChallengeApplicationDTO();
        applicationDTO.setChallengeId(challenge.getId());
        applicationDTO.setDeposit(15000);

        ApiException exception = assertThrows(ApiException.class, () ->
                challengeService.applyForChallenge("username", applicationDTO));

        assertEquals("USER_ALREADY_ENROLLED", exception.getErrorCode().toString());
    }

    @Test
    @DisplayName("챌린지를 찾을 수 없는 경우 예외가 발생해야 한다")
    void applyForChallenge_ChallengeNotFound_ShouldThrowException() {
        User user = new User();
        user.setId(1L);

        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));
        when(userChallengeRepository.existsByUserIdAndChallengeId(anyLong(), anyLong())).thenReturn(false);
        when(challengeRepository.findById(anyLong())).thenReturn(Optional.empty());

        ApiException exception = assertThrows(ApiException.class, () ->
                challengeService.applyForChallenge("username", new ChallengeApplicationDTO()));

        assertEquals("CHALLENGE_NOT_FOUND", exception.getErrorCode().toString());
    }

    @Test
    @DisplayName("챌린지가 활성 상태가 아닌 경우 예외가 발생해야 한다")
    void applyForChallenge_ChallengeNotActive_ShouldThrowException() {
        // 사용자와 챌린지 객체 생성
        User user = new User();
        user.setId(1L);

        Challenge challenge = new Challenge();
        challenge.setId(1L);
        challenge.setStatus(ChallengeStatus.COMPLETED); // 챌린지 상태를 COMPLETED로 설정

        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));
        when(userChallengeRepository.existsByUserIdAndChallengeId(user.getId(), challenge.getId())).thenReturn(false);
        when(challengeRepository.findById(challenge.getId())).thenReturn(Optional.of(challenge));

        ChallengeApplicationDTO applicationDTO = new ChallengeApplicationDTO();
        applicationDTO.setChallengeId(challenge.getId());

        ApiException exception = assertThrows(ApiException.class, () ->
                challengeService.applyForChallenge("username", applicationDTO));

        // 예외 메시지 검증
        assertEquals("CHALLENGE_NOT_ACTIVE", exception.getErrorCode().toString());
    }

    @Test
    @DisplayName("보증금이 유효하지 않은 경우 예외가 발생해야 한다")
    void applyForChallenge_InvalidDeposit_ShouldThrowException() {
        // 사용자와 챌린지 객체 생성
        User user = new User();
        user.setId(1L);

        Challenge challenge = new Challenge();
        challenge.setId(1L);
        challenge.setMinDeposit(10000); // 최소 보증금 설정
        challenge.setMaxDeposit(20000); // 최대 보증금 설정
        challenge.setStatus(ChallengeStatus.ACTIVE); // 챌린지 상태를 ACTIVE로 설정

        ChallengeApplicationDTO applicationDTO = new ChallengeApplicationDTO();
        applicationDTO.setChallengeId(challenge.getId());
        applicationDTO.setDeposit(5000); // 유효하지 않은 보증금 금액 설정

        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));
        when(userChallengeRepository.existsByUserIdAndChallengeId(user.getId(), challenge.getId())).thenReturn(false);
        when(challengeRepository.findById(challenge.getId())).thenReturn(Optional.of(challenge));

        ApiException exception = assertThrows(ApiException.class, () ->
                challengeService.applyForChallenge("username", applicationDTO));

        assertEquals("INVALID_DEPOSIT", exception.getErrorCode().toString());
    }

    @Test
    @DisplayName("성공적인 챌린지 신청 후 신청 정보를 반환해야 한다")
    void applyForChallenge_SuccessfulApplication_ShouldReturnApplicationDetails() {
        User user = new User();
        user.setId(1L);
        Challenge challenge = new Challenge();
        challenge.setId(1L);
        challenge.setChallengeName("Test Challenge");
        challenge.setMinDeposit(10000);
        challenge.setMaxDeposit(20000);
        challenge.setStatus(ChallengeStatus.ACTIVE);

        ChallengeApplicationDTO applicationDTO = new ChallengeApplicationDTO();
        applicationDTO.setChallengeId(1L);
        applicationDTO.setDeposit(15000);
        applicationDTO.setChallengeSchedules(new ArrayList<>());

        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));
        when(userChallengeRepository.existsByUserIdAndChallengeId(anyLong(), anyLong())).thenReturn(false);
        when(challengeRepository.findById(anyLong())).thenReturn(Optional.of(challenge));
        when(userChallengeRepository.save(any(UserChallenge.class))).thenAnswer(i -> i.getArguments()[0]);

        ChallengeApplicationDTO result = challengeService.applyForChallenge("username", applicationDTO);

        assertEquals("Test Challenge", result.getChallengeName());
        assertEquals(15000, result.getDeposit());
        assertEquals(15000, result.getPaymentAmount());
    }
}