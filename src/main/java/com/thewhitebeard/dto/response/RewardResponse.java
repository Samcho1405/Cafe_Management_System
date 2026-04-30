package com.thewhitebeard.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RewardResponse {
    private Long id;
    private Long productId;
    private String productName;
    private String productImage;
    private int coinsRequired;
    private boolean enabled;
    private int coinsDeducted;  // redeem ke baad kitne coins kate
    private int remainingCoins; // redeem ke baad kitne coins bache
}
