package com.url_shortner.user_service.controllers;

import com.url_shortner.user_service.dtos.UserInfoDTO;
import com.url_shortner.user_service.dtos.UserUpdateRequest;
import com.url_shortner.user_service.entities.User;
import com.url_shortner.user_service.services.auth.AuthService;
import com.url_shortner.user_service.services.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final AuthService authService;

    // ✅ Get current user details
    @GetMapping("/me")
    public ResponseEntity<UserInfoDTO> getCurrentUser() {
        User user = authService.getCurrentUser();
        return ResponseEntity.ok(UserInfoDTO.builder()
                .username(user.getUsername())
                .phone(user.getPhoneNo())
                .role(user.getRole())
                .email(user.getEmail()).build());
    }

    // ✅ Update current user's profile
    @PutMapping("/me")
    public ResponseEntity<UserInfoDTO> updateProfile(@RequestBody UserUpdateRequest request) {
        User user = userService.updateUser(request);
        return ResponseEntity.ok(UserInfoDTO.builder()
                        .username(user.getUsername())
                        .phone(user.getPhoneNo())
                        .role(user.getRole())
                        .email(user.getEmail())

                .build());
    }
}
