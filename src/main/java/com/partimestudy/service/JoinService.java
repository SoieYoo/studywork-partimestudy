package com.partimestudy.service;

import static com.partimestudy.type.ErrorCode.ALREADY_EXIST_USERNAME;

import com.partimestudy.dto.JoinDTO;
import com.partimestudy.entity.User;
import com.partimestudy.exception.ApiException;
import com.partimestudy.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JoinService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public void joinProcess(JoinDTO joinDTO) {

        if (userRepository.findByUsername(joinDTO.getUsername()).isPresent()) {
            throw new ApiException(ALREADY_EXIST_USERNAME);
        }

        //비밀번호 암호화
        String bCryptPassword = bCryptPasswordEncoder.encode(joinDTO.getPassword());

        User user = User.builder()
                .username(joinDTO.getUsername())
                .nickname(joinDTO.getNickname())
                .password(bCryptPassword)
                .studyGoal(joinDTO.getStudyGoal())
                .role("ROLE_USER")
                .build();

        userRepository.save(user);
    }
}
