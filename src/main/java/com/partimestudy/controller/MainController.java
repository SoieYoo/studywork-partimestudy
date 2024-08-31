package com.partimestudy.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MainController {

    @GetMapping("/")
    public String mainP() {
        return "[스터디워크 사전과제] 파트타임스터디 서비스의 챌린지 주문(신청) 프로그램";
    }
}

