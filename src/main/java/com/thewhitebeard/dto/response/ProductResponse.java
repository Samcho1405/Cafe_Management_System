package com.thewhitebeard.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProductResponse {
    private Long id;
    private String name;
    private Long categoryId;
    private String categoryName;
    private double price;
    private int stock;
    private String description;
    private String image;
}
