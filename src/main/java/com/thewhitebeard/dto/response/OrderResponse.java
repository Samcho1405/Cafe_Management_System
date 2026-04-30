package com.thewhitebeard.dto.response;

import com.thewhitebeard.dto.request.OrderRequest.CartItemRequest;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class OrderResponse {
    private Long id;
    private Long customerId;
    private String customerName;
    private List<CartItemRequest> items;
    private double total;
    private String status;
    private String paymentMethod;
    private LocalDateTime createdAt;
}
