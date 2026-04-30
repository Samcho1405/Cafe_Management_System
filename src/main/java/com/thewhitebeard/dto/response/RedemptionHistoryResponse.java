package com.thewhitebeard.dto.response;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Builder
public class RedemptionHistoryResponse {
    private Long id;
    private Long customerId;
    private String customerName;
    private String customerEmail;
    private Long rewardId;
    private String productName;
    private int coinsUsed;
    private LocalDateTime redeemedAt;
}
