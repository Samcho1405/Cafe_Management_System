package com.thewhitebeard.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CreateStaffRequest {

    @NotBlank(message = "Name required")
    private String name;

    @Email(message = "Valid email required")
    @NotBlank(message = "Email required")
    private String email;

    @NotBlank(message = "Password required")
    @Size(min = 6, message = "Password min 6 characters")
    private String password;

    private String phone;
}
