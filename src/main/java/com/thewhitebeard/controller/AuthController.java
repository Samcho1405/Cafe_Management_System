package com.thewhitebeard.controller;

import com.thewhitebeard.dto.request.LoginRequest;
import com.thewhitebeard.dto.request.RegisterRequest;
import com.thewhitebeard.dto.response.ApiResponse;
import com.thewhitebeard.dto.response.UserResponse;
import com.thewhitebeard.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    // POST /api/login
    @PostMapping("/login")
    public ResponseEntity<UserResponse> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    // POST /api/register
    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(@Valid @RequestBody RegisterRequest request) {
        return ResponseEntity.ok(authService.register(request));
    }

    // POST /api/logout  →  Frontend token delete karega, server pe kuch nahi
    @PostMapping("/logout")
    public ResponseEntity<ApiResponse> logout() {
        return ResponseEntity.ok(ApiResponse.ok("Logged out successfully"));
    }

    // POST /api/forgot-password  →  Public (no login needed)
    @PostMapping("/forgot-password")
    public ResponseEntity<ApiResponse> forgotPassword(@RequestBody java.util.Map<String, String> body) {
        String email = body.get("email");
        String newPassword = body.get("newPassword");
        if (email == null || email.isBlank()) {
            throw new com.thewhitebeard.exception.BadRequestException("Email required hai");
        }
        authService.forgotPassword(email, newPassword);
        return ResponseEntity.ok(ApiResponse.ok("Password successfully change ho gaya!"));
    }
}
