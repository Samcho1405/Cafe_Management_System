package com.thewhitebeard.service;

import com.thewhitebeard.dto.request.FeedbackRequest;
import com.thewhitebeard.dto.response.FeedbackResponse;
import java.util.List;

import java.util.List;

public interface FeedbackService {
    FeedbackResponse getByOrderId(Long orderId);
    List<FeedbackResponse> getAllFeedback();
    FeedbackResponse submitFeedback(FeedbackRequest request, Long customerId);
}
