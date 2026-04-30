package com.thewhitebeard.service;

import com.thewhitebeard.dto.request.ChangePasswordRequest;
import com.thewhitebeard.dto.request.CustomerChangePasswordRequest;
import com.thewhitebeard.dto.request.CreateStaffRequest;
import com.thewhitebeard.dto.request.ProfileUpdateRequest;
import com.thewhitebeard.dto.response.UserResponse;

import java.util.List;

public interface UserService {
    List<UserResponse> getAllCustomers();
    List<UserResponse> getAllStaff();
    UserResponse createStaff(CreateStaffRequest request);
    void deleteStaff(Long staffId);
    void changeStaffPassword(Long staffId, ChangePasswordRequest request);
    void changeOwnPassword(Long userId, CustomerChangePasswordRequest request);
    UserResponse getProfile(Long userId);
    UserResponse updateProfile(Long userId, ProfileUpdateRequest request);
}
