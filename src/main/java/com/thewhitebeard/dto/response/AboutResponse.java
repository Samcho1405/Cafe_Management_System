package com.thewhitebeard.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AboutResponse {
    private Long id;
    private String content;
}
