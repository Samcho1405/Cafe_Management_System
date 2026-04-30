package com.thewhitebeard.serviceimpl;

import com.thewhitebeard.config.JwtUtil;
import com.thewhitebeard.dto.request.LoginRequest;
import com.thewhitebeard.dto.request.RegisterRequest;
import com.thewhitebeard.dto.response.UserResponse;
import com.thewhitebeard.exception.BadRequestException;
import com.thewhitebeard.exception.UnauthorizedException;
import com.thewhitebeard.model.User;
import com.thewhitebeard.repository.UserRepository;
import com.thewhitebeard.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepo;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Override
    public UserResponse login(LoginRequest request) {
        User user = userRepo.findByEmail(request.getEmail())
                .orElseThrow(() -> new UnauthorizedException("Invalid email or password"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new UnauthorizedException("Invalid email or password");
        }

        String token = jwtUtil.generateToken(user.getId(), user.getRole().name());
        return toResponse(user, token);
    }

    @Override
    public UserResponse register(RegisterRequest request) {
        if (userRepo.existsByEmail(request.getEmail())) {
            throw new BadRequestException("Email already registered");
        }

        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .phone(request.getPhone())
                .address(request.getAddress())
                .role(User.Role.CUSTOMER)
                .coins(1)
                .build();

        user = userRepo.save(user);
        String token = jwtUtil.generateToken(user.getId(), user.getRole().name());
        return toResponse(user, token);
    }

    @Override
    public void forgotPassword(String email, String newPassword) {
        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new BadRequestException("Email registered nahi hai"));
        if (newPassword == null || newPassword.length() < 6) {
            throw new BadRequestException("Password kam se kam 6 characters ka hona chahiye");
        }
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepo.save(user);
    }

    private UserResponse toResponse(User user, String token) {
        return UserResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .phone(user.getPhone())
                .address(user.getAddress())
                .role(user.getRole().name())
                .coins(user.getCoins())
                .token(token)
                .build();
    }
}
