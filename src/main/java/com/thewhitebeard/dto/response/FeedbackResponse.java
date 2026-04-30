package com.thewhitebeard.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FeedbackResponse {
    private Long id;
    private Long orderId;
    private String customerName;
    private String productName;
    private String message;
    private int rating;
}
