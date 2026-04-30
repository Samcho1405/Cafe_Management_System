package com.thewhitebeard.controller;

import com.thewhitebeard.config.JwtContextUtil;
import com.thewhitebeard.dto.request.FeedbackRequest;
import com.thewhitebeard.dto.response.FeedbackResponse;
import com.thewhitebeard.service.FeedbackService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/feedback")
@RequiredArgsConstructor
public class FeedbackController {

    private final FeedbackService feedbackService;

    // GET /api/feedback  →  ADMIN only
    @GetMapping
    public ResponseEntity<List<FeedbackResponse>> getAllFeedback(HttpServletRequest request) {
        JwtContextUtil.requireRole(request, "ADMIN");
        return ResponseEntity.ok(feedbackService.getAllFeedback());
    }

    // GET /api/feedback/order/{orderId}  →  CUSTOMER — apna feedback dekho
    @GetMapping("/order/{orderId}")
    public ResponseEntity<FeedbackResponse> getByOrder(@PathVariable Long orderId, HttpServletRequest request) {
        JwtContextUtil.requireRole(request, "CUSTOMER");
        return ResponseEntity.ok(feedbackService.getByOrderId(orderId));
    }

    // POST /api/feedback  →  CUSTOMER only
    @PostMapping
    public ResponseEntity<FeedbackResponse> submitFeedback(
            @Valid @RequestBody FeedbackRequest body,
            HttpServletRequest request) {
        JwtContextUtil.requireRole(request, "CUSTOMER");
        Long customerId = JwtContextUtil.getUserId(request);
        return ResponseEntity.ok(feedbackService.submitFeedback(body, customerId));
    }
}
