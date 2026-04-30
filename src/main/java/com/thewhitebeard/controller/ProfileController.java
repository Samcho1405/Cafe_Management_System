package com.thewhitebeard.controller;

import com.thewhitebeard.config.JwtContextUtil;
import com.thewhitebeard.dto.request.CustomerChangePasswordRequest;
import com.thewhitebeard.dto.request.ProfileUpdateRequest;
import com.thewhitebeard.dto.response.ApiResponse;
import com.thewhitebeard.dto.response.UserResponse;
import com.thewhitebeard.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/profile")
@RequiredArgsConstructor
public class ProfileController {

    private final UserService userService;

    // GET /api/profile  →  Any logged in user
    @GetMapping
    public ResponseEntity<UserResponse> getProfile(HttpServletRequest request) {
        Long userId = JwtContextUtil.getUserId(request);
        return ResponseEntity.ok(userService.getProfile(userId));
    }

    // PUT /api/profile/password  →  Any logged in user
    @PutMapping("/password")
    public ResponseEntity<ApiResponse> changePassword(
            @RequestBody CustomerChangePasswordRequest request,
            HttpServletRequest servletRequest) {
        Long userId = JwtContextUtil.getUserId(servletRequest);
        userService.changeOwnPassword(userId, request);
        return ResponseEntity.ok(ApiResponse.ok("Password change ho gaya!"));
    }

    // PUT /api/profile  →  Any logged in user
    @PutMapping
    public ResponseEntity<UserResponse> updateProfile(
            @RequestBody ProfileUpdateRequest body,
            HttpServletRequest request) {
        Long userId = JwtContextUtil.getUserId(request);
        return ResponseEntity.ok(userService.updateProfile(userId, body));
    }
}
