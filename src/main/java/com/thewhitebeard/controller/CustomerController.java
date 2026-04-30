package com.thewhitebeard.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/customer")
public class CustomerController {
    // Saare endpoints dedicated controllers mein hain:
    // Auth     → AuthController
    // Profile  → ProfileController
    // Orders   → OrderController
    // Feedback → FeedbackController
    // Rewards  → RewardController
}
