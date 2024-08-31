package com.partimestudy.service;

import static com.partimestudy.type.ErrorCode.USER_NOT_FOUND;

import com.partimestudy.entity.User;
import com.partimestudy.exception.ApiException;
import com.partimestudy.repository.UserRepository;
import java.security.Principal;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public User getMyInfo(String username) {

        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ApiException(USER_NOT_FOUND));
    }

}
