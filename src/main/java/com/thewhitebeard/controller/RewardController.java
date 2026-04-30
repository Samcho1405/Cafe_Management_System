package com.thewhitebeard.controller;

import com.thewhitebeard.config.JwtContextUtil;
import com.thewhitebeard.dto.request.RewardRequest;
import com.thewhitebeard.dto.response.ApiResponse;
import com.thewhitebeard.dto.response.RedemptionHistoryResponse;
import com.thewhitebeard.dto.response.RewardResponse;
import com.thewhitebeard.serviceimpl.RewardServiceImpl;
import com.thewhitebeard.service.RewardService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rewards")
@RequiredArgsConstructor
public class RewardController {

    private final RewardService rewardService;
    private final RewardServiceImpl rewardServiceImpl;

    // GET /api/rewards  →  ADMIN, CUSTOMER
    @GetMapping
    public ResponseEntity<List<RewardResponse>> getAllRewards(HttpServletRequest request) {
        JwtContextUtil.requireRole(request, "ADMIN", "CUSTOMER");
        return ResponseEntity.ok(rewardService.getAllRewards());
    }

    // POST /api/rewards  →  ADMIN only
    @PostMapping
    public ResponseEntity<RewardResponse> createReward(
            @Valid @RequestBody RewardRequest body,
            HttpServletRequest request) {
        JwtContextUtil.requireRole(request, "ADMIN");
        return ResponseEntity.ok(rewardService.createReward(body));
    }

    // PUT /api/rewards/{id}  →  ADMIN only
    @PutMapping("/{id}")
    public ResponseEntity<RewardResponse> updateReward(
            @PathVariable Long id,
            @Valid @RequestBody RewardRequest body,
            HttpServletRequest request) {
        JwtContextUtil.requireRole(request, "ADMIN");
        return ResponseEntity.ok(rewardService.updateReward(id, body));
    }

    // DELETE /api/rewards/{id}  →  ADMIN only
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteReward(
            @PathVariable Long id,
            HttpServletRequest request) {
        JwtContextUtil.requireRole(request, "ADMIN");
        rewardService.deleteReward(id);
        return ResponseEntity.ok(ApiResponse.ok("Reward deleted successfully"));
    }

    // GET /api/rewards/redemptions  →  ADMIN only
    @GetMapping("/redemptions")
    public ResponseEntity<List<RedemptionHistoryResponse>> getAllRedemptions(HttpServletRequest request) {
        JwtContextUtil.requireRole(request, "ADMIN");
        return ResponseEntity.ok(rewardServiceImpl.getAllRedemptions());
    }

    // GET /api/rewards/redemptions/my  →  CUSTOMER only
    @GetMapping("/redemptions/my")
    public ResponseEntity<List<RedemptionHistoryResponse>> getMyRedemptions(HttpServletRequest request) {
        JwtContextUtil.requireRole(request, "CUSTOMER");
        Long customerId = JwtContextUtil.getUserId(request);
        return ResponseEntity.ok(rewardServiceImpl.getCustomerRedemptions(customerId));
    }

    // POST /api/rewards/redeem/{id}  →  CUSTOMER only
    @PostMapping("/redeem/{id}")
    public ResponseEntity<RewardResponse> redeemReward(
            @PathVariable Long id,
            HttpServletRequest request) {
        JwtContextUtil.requireRole(request, "CUSTOMER");
        Long customerId = JwtContextUtil.getUserId(request);
        return ResponseEntity.ok(rewardService.redeemReward(id, customerId));
    }
}
