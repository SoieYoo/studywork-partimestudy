package com.partimestudy.repository;

import com.partimestudy.entity.UserChallenge;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserChallengeRepository extends JpaRepository<UserChallenge, Long> {
    boolean existsByUserIdAndChallengeId(Long userId, Long challengeId);
}
