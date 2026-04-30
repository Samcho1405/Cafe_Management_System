package com.thewhitebeard.dto.request;

import lombok.Data;

@Data
public class RewardRequest {
    private Long productId;
    private int coinsRequired;
    private boolean enabled = true;
}
