package com.partimestudy.controller;

import static com.partimestudy.type.ResponseMessage.JOIN_SUCCESS;

import com.partimestudy.dto.ApiResponse;
import com.partimestudy.dto.JoinDTO;
import com.partimestudy.service.JoinService;
import com.partimestudy.type.ResponseStatus;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class JoinController {

    private final JoinService joinService;

    @PostMapping("/join")
    @Operation(summary = "회원 가입", description = "회원 가입을 진행합니다.")
    public ResponseEntity<ApiResponse<JoinDTO>> joinProcess(@RequestBody @Valid JoinDTO joinDTO) {

        joinService.joinProcess(joinDTO);

        return ResponseEntity.ok(
                new ApiResponse<>(
                        ResponseStatus.SUCCESS.getStatus()
                        , JOIN_SUCCESS.getMessage()
                )
        );
    }
}