package com.partimestudy.dto;

import com.partimestudy.entity.User;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UserDTO {
    private String username;
    private String nickname;
    private String studyGoal;
    public static UserDTO fromEntity(User user) {
        return UserDTO.builder()
                .username(user.getUsername())
                .nickname(user.getNickname())
                .studyGoal(user.getStudyGoal())
                .build();
    }
}
