package com.thewhitebeard.service;

import com.thewhitebeard.dto.request.LoginRequest;
import com.thewhitebeard.dto.request.RegisterRequest;
import com.thewhitebeard.dto.response.UserResponse;

public interface AuthService {
    UserResponse login(LoginRequest request);
    UserResponse register(RegisterRequest request);
    void forgotPassword(String email, String newPassword);
}
