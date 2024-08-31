package com.partimestudy.controller;

import static com.partimestudy.type.ResponseMessage.INFO_RETRIEVED_SUCCESS;

import com.partimestudy.dto.ApiResponse;
import com.partimestudy.dto.UserDTO;
import com.partimestudy.entity.User;
import com.partimestudy.service.UserService;
import com.partimestudy.type.ResponseStatus;
import io.swagger.v3.oas.annotations.Operation;
import java.security.Principal;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;
    @GetMapping("/me")
    @Operation(summary = "회원 정보 조회", description = "회원 정보를 조회합니다.")
    public ResponseEntity<ApiResponse<UserDTO>> getMyInfo(Principal principal) {
        User user = userService.getMyInfo(principal.getName());

        return ResponseEntity.ok(
                new ApiResponse<>(
                        ResponseStatus.SUCCESS.getStatus()
                        , INFO_RETRIEVED_SUCCESS.getMessage()
                        , UserDTO.fromEntity(user)
                )
        );
    }
}
