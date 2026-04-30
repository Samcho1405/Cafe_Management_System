package com.thewhitebeard.serviceimpl;

import com.thewhitebeard.dto.request.ChangePasswordRequest;
import com.thewhitebeard.dto.request.CustomerChangePasswordRequest;
import com.thewhitebeard.dto.request.CreateStaffRequest;
import com.thewhitebeard.dto.request.ProfileUpdateRequest;
import com.thewhitebeard.dto.response.UserResponse;
import com.thewhitebeard.exception.BadRequestException;
import com.thewhitebeard.exception.ResourceNotFoundException;
import com.thewhitebeard.model.User;
import com.thewhitebeard.repository.UserRepository;
import com.thewhitebeard.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepo;
    private final PasswordEncoder passwordEncoder;

    @Override
    public List<UserResponse> getAllCustomers() {
        return userRepo.findAll().stream()
                .filter(u -> u.getRole() == User.Role.CUSTOMER)
                .map(this::toResponse).collect(Collectors.toList());
    }

    @Override
    public List<UserResponse> getAllStaff() {
        return userRepo.findAll().stream()
                .filter(u -> u.getRole() == User.Role.STAFF)
                .map(this::toResponse).collect(Collectors.toList());
    }

    @Override
    public UserResponse createStaff(CreateStaffRequest request) {
        if (userRepo.existsByEmail(request.getEmail())) {
            throw new BadRequestException("Email already registered");
        }
        User staff = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .phone(request.getPhone())
                .role(User.Role.STAFF)
                .coins(0)
                .build();
        return toResponse(userRepo.save(staff));
    }

    @Override
    public void deleteStaff(Long staffId) {
        User user = userRepo.findById(staffId)
                .orElseThrow(() -> new ResourceNotFoundException("Staff not found"));
        if (user.getRole() != User.Role.STAFF) {
            throw new BadRequestException("User is not a staff member");
        }
        userRepo.delete(user);
    }

    @Override
    public UserResponse getProfile(Long userId) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return toResponse(user);
    }

    @Override
    public UserResponse updateProfile(Long userId, ProfileUpdateRequest request) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        if (request.getName() != null && !request.getName().isBlank()) user.setName(request.getName());
        if (request.getEmail() != null && !request.getEmail().isBlank()) user.setEmail(request.getEmail());
        if (request.getPhone() != null) user.setPhone(request.getPhone());
        if (request.getAddress() != null) user.setAddress(request.getAddress());
        return toResponse(userRepo.save(user));
    }

    @Override
    public void changeOwnPassword(Long userId, CustomerChangePasswordRequest request) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        // Pehle current password verify karo
        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            throw new BadRequestException("Current password galat hai");
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepo.save(user);
    }

    @Override
    public void changeStaffPassword(Long staffId, ChangePasswordRequest request) {
        User user = userRepo.findById(staffId)
                .orElseThrow(() -> new ResourceNotFoundException("Staff not found"));
        if (user.getRole() != User.Role.STAFF) {
            throw new BadRequestException("User is not a staff member");
        }
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepo.save(user);
    }

    private UserResponse toResponse(User u) {
        return UserResponse.builder()
                .id(u.getId())
                .name(u.getName())
                .email(u.getEmail())
                .phone(u.getPhone())
                .address(u.getAddress())
                .role(u.getRole().name())
                .coins(u.getCoins())
                .build();
    }
}
