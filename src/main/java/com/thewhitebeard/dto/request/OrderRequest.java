package com.thewhitebeard.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class OrderRequest {

    @NotEmpty(message = "Cart cannot be empty")
    private List<CartItemRequest> cartItems;

    @NotNull(message = "Payment method is required")
    private String paymentMethod; // "COD", "Online", or "Coins" (reward orders)

    @Data
    public static class CartItemRequest {
        private Long id;
        private String name;
        private double price;
        private int qty;
        private String image;
        @com.fasterxml.jackson.annotation.JsonProperty("isReward")
        private boolean isReward; // true = reward item, ₹0 pe
    }
}
