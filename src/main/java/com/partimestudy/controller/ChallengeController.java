package com.partimestudy.controller;

import static com.partimestudy.type.ResponseMessage.CHALLENGE_APPLY_SUCCESS;
import static com.partimestudy.type.ResponseMessage.INFO_RETRIEVED_SUCCESS;

import com.partimestudy.dto.ApiResponse;
import com.partimestudy.dto.ChallengeApplicationDTO;
import com.partimestudy.dto.UserDTO;
import com.partimestudy.service.ChallengeService;
import com.partimestudy.type.ResponseStatus;
import io.swagger.v3.oas.annotations.Operation;
import java.security.Principal;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/challenges")
public class ChallengeController {

    private final ChallengeService challengeService;

    @PostMapping("/apply")
    @Operation(summary = "챌린지 등록", description = "챌린지를 등록합니다.")
    public ResponseEntity<ApiResponse<ChallengeApplicationDTO>> applyForChallenge(@RequestBody ChallengeApplicationDTO applicationDTO, Principal principal) {
        return ResponseEntity.ok(
                new ApiResponse<>(
                        ResponseStatus.SUCCESS.getStatus()
                        , CHALLENGE_APPLY_SUCCESS.getMessage()
                        , challengeService.applyForChallenge(principal.getName(),
                        applicationDTO)
                )
        );
    }
}
