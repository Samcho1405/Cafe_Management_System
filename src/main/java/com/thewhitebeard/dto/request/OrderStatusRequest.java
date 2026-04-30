package com.thewhitebeard.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class OrderStatusRequest {
    @NotBlank(message = "Status is required")
    private String status;
}
