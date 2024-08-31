package com.partimestudy.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ResponseMessage {

    JOIN_SUCCESS("회원가입 성공"),
    LOGIN_SUCCESS("로그인 성공"),
    CHALLENGE_APPLY_SUCCESS("챌린지 등록 성공"),
    INFO_RETRIEVED_SUCCESS("정보 조회 성공");

    private final String message;
}