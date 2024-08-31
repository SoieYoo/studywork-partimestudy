package com.partimestudy.type;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    FORBIDDEN(HttpStatus.FORBIDDEN, "권한이 없습니다."),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "인증이 필요합니다."),
    ALREADY_EXIST_USERNAME(HttpStatus.BAD_REQUEST, "이미 등록된 아이디입니다."),
    USER_ALREADY_ENROLLED(HttpStatus.BAD_REQUEST, "이미 신청한 챌린지입니다."),
    INVALID_LOGIN(HttpStatus.BAD_REQUEST, "아이디 혹은 비밀번호를 다시 확인해주세요."),
    INVALID_DEPOSIT(HttpStatus.BAD_REQUEST, "보증금을 확인해주세요."),
    BAD_REQUEST(HttpStatus.BAD_REQUEST, "잘못된 요청입니다."),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "내부 서부 오류가 발생했습니다."),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다."),
    CHALLENGE_NOT_FOUND(HttpStatus.NOT_FOUND, "챌린지를 찾을 수 없습니다."),
    CHALLENGE_NOT_ACTIVE(HttpStatus.NOT_FOUND, "참여할 수 없는 챌린지입니다."),
    ORDER_NOT_FOUND(HttpStatus.NOT_FOUND, "주문을 찾을 수 없습니다.");

    private final HttpStatus status;
    private final String message;
}
