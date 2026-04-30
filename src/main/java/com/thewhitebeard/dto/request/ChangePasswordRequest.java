package com.thewhitebeard.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ChangePasswordRequest {

    @NotBlank(message = "New password required")
    @Size(min = 6, message = "Password min 6 characters")
    private String newPassword;
}
