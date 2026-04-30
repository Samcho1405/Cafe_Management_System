package com.thewhitebeard.config;

import com.thewhitebeard.model.User;
import com.thewhitebeard.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepo;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {

        if (!userRepo.existsByEmail("admin@whitebeard.com")) {
            userRepo.save(User.builder()
                    .name("Admin")
                    .email("admin@whitebeard.com")
                    .password(passwordEncoder.encode("admin123"))
                    .role(User.Role.ADMIN)
                    .coins(0)
                    .build());
            System.out.println("✅ Admin created  →  admin@whitebeard.com  /  admin123");
        }

        if (!userRepo.existsByEmail("staff@whitebeard.com")) {
            userRepo.save(User.builder()
                    .name("Staff Member")
                    .email("staff@whitebeard.com")
                    .password(passwordEncoder.encode("staff123"))
                    .role(User.Role.STAFF)
                    .coins(0)
                    .build());
            System.out.println("✅ Staff created   →  staff@whitebeard.com  /  staff123");
        }
    }
}
