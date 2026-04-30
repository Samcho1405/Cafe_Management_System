package com.thewhitebeard.service;

import com.thewhitebeard.dto.request.RewardRequest;
import com.thewhitebeard.dto.response.RewardResponse;

import java.util.List;

public interface RewardService {
    List<RewardResponse> getAllRewards();
    RewardResponse createReward(RewardRequest request);
    RewardResponse updateReward(Long id, RewardRequest request);
    void deleteReward(Long id);
    RewardResponse redeemReward(Long rewardId, Long customerId); // ✅ RewardResponse return karo
}
