package com.thewhitebeard.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UpsellResponse {
    private Long id;
    private Long productId;
    private String productName;
    private String productImage;
    private int triggerQuantity;
    private String message;
    private double price;
    private boolean enabled;
}
