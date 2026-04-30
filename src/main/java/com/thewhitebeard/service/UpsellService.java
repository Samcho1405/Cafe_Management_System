package com.thewhitebeard.service;

import com.thewhitebeard.dto.request.UpsellRequest;
import com.thewhitebeard.dto.response.UpsellResponse;

import java.util.List;

public interface UpsellService {
    List<UpsellResponse> getAllUpsells();
    UpsellResponse checkUpsell(int cartQuantity);   // returns matching rule or null
    UpsellResponse createUpsell(UpsellRequest request);
    UpsellResponse updateUpsell(Long id, UpsellRequest request);
    void deleteUpsell(Long id);
}
