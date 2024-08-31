package com.partimestudy.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class JoinDTO {

    @NotEmpty
    @Size(max = 30)
    private String username;
    @NotEmpty
    @Size(min = 6)
    private String password;
    @NotEmpty
    private String studyGoal;
    @NotEmpty
    @Size(max = 50)
    private String nickname;
}

