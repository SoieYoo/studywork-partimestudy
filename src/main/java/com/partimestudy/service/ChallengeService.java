package com.partimestudy.service;

import static com.partimestudy.type.ErrorCode.CHALLENGE_NOT_ACTIVE;
import static com.partimestudy.type.ErrorCode.CHALLENGE_NOT_FOUND;
import static com.partimestudy.type.ErrorCode.INVALID_DEPOSIT;
import static com.partimestudy.type.ErrorCode.USER_ALREADY_ENROLLED;
import static com.partimestudy.type.ErrorCode.USER_NOT_FOUND;

import com.partimestudy.dto.ChallengeApplicationDTO;
import com.partimestudy.dto.ChallengeScheduleDTO;
import com.partimestudy.entity.Challenge;
import com.partimestudy.entity.ChallengeSchedule;
import com.partimestudy.entity.User;
import com.partimestudy.entity.UserChallenge;
import com.partimestudy.exception.ApiException;
import com.partimestudy.repository.ChallengeRepository;
import com.partimestudy.repository.UserChallengeRepository;
import com.partimestudy.repository.UserRepository;
import com.partimestudy.type.ChallengeStatus;
import java.util.ArrayList;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ChallengeService {
    private final UserRepository userRepository;
    private final ChallengeRepository challengeRepository;
    private final UserChallengeRepository userChallengeRepository;

    @Transactional
    public ChallengeApplicationDTO applyForChallenge(String username, ChallengeApplicationDTO applicationDTO) {
        // 유저 체크
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ApiException(USER_NOT_FOUND));

        // 동일한 챌린시 신청시 예외
        if (userChallengeRepository.existsByUserIdAndChallengeId(user.getId(), applicationDTO.getChallengeId())) {
            throw new ApiException(USER_ALREADY_ENROLLED);
        }

        // 챌린지 신청
        Challenge challenge = challengeRepository.findById(applicationDTO.getChallengeId())
                .orElseThrow(() -> new ApiException(CHALLENGE_NOT_FOUND));

        // 챌린지 상태 체크
        if (challenge.getStatus() != ChallengeStatus.ACTIVE) {
            throw new ApiException(CHALLENGE_NOT_ACTIVE);
        }

        // 보증금 validation
        if (applicationDTO.getDeposit() < challenge.getMinDeposit() || applicationDTO.getDeposit() > challenge.getMaxDeposit()) {
            throw new ApiException(INVALID_DEPOSIT);
        }

        // UserChallenge 생성
        UserChallenge userChallenge = UserChallenge.builder()
                .user(user)
                .challenge(challenge)
                .deposit(applicationDTO.getDeposit())
                .challengeSchedules(new ArrayList<>())
                .build();

        // 스케줄 추가
        for (ChallengeScheduleDTO scheduleDTO : applicationDTO.getChallengeSchedules()) {
            ChallengeSchedule schedule = ChallengeSchedule.builder()
                    .challenge(challenge)
                    .applyDate(scheduleDTO.getApplyDate())
                    .hours(scheduleDTO.getHours())
                    .userChallenge(userChallenge)
                    .build();
            userChallenge.getChallengeSchedules().add(schedule);
        }

        // 저장
        userChallengeRepository.save(userChallenge);

        applicationDTO.setChallengeName(challenge.getChallengeName());
        applicationDTO.setDeposit(userChallenge.getDeposit());
        applicationDTO.setPaymentAmount(userChallenge.getDeposit());

        return applicationDTO;
    }
}
