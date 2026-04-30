package com.thewhitebeard.dto.request;

import jakarta.validation.constraints.Email;
import lombok.Data;

@Data
public class ProfileUpdateRequest {
    private String name;

    @Email(message = "Invalid email format")
    private String email;

    private String phone;
    private String address;
}
