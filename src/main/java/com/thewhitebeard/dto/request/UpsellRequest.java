package com.thewhitebeard.dto.request;

import lombok.Data;

@Data
public class UpsellRequest {
    private Long productId;
    private int triggerQuantity;
    private String message;
    private double price;
    private boolean enabled = true;
}
