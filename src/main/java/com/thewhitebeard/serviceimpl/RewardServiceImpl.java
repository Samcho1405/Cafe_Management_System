package com.thewhitebeard.serviceimpl;

import com.thewhitebeard.dto.request.RewardRequest;
import com.thewhitebeard.dto.response.RedemptionHistoryResponse;
import com.thewhitebeard.model.RedemptionHistory;
import com.thewhitebeard.repository.RedemptionHistoryRepository;
import com.thewhitebeard.dto.response.RewardResponse;
import com.thewhitebeard.exception.BadRequestException;
import com.thewhitebeard.exception.ResourceNotFoundException;
import com.thewhitebeard.model.Product;
import com.thewhitebeard.model.Reward;
import com.thewhitebeard.model.User;
import com.thewhitebeard.repository.ProductRepository;
import com.thewhitebeard.repository.RewardRepository;
import com.thewhitebeard.repository.UserRepository;
import com.thewhitebeard.service.RewardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RewardServiceImpl implements RewardService {

    private final RewardRepository rewardRepo;
    private final ProductRepository productRepo;
    private final UserRepository userRepo;
    private final RedemptionHistoryRepository redemptionHistoryRepo;

    @Override
    public List<RewardResponse> getAllRewards() {
        return rewardRepo.findAll()
                .stream().map(this::toResponse).collect(Collectors.toList());
    }

    @Override
    public RewardResponse createReward(RewardRequest request) {
        Product product = productRepo.findById(request.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + request.getProductId()));
        Reward reward = Reward.builder()
                .product(product)
                .coinsRequired(request.getCoinsRequired())
                .enabled(request.isEnabled())
                .build();
        return toResponse(rewardRepo.save(reward));
    }

    @Override
    public RewardResponse updateReward(Long id, RewardRequest request) {
        Reward reward = rewardRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Reward not found with id: " + id));

        if (request.getProductId() != null) {
            Product product = productRepo.findById(request.getProductId())
                    .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + request.getProductId()));
            reward.setProduct(product);
        }
        if (request.getCoinsRequired() > 0) reward.setCoinsRequired(request.getCoinsRequired());
        reward.setEnabled(request.isEnabled());

        return toResponse(rewardRepo.save(reward));
    }

    @Override
    public void deleteReward(Long id) {
        if (!rewardRepo.existsById(id)) {
            throw new ResourceNotFoundException("Reward not found with id: " + id);
        }
        rewardRepo.deleteById(id);
    }

    @Override
    public RewardResponse redeemReward(Long rewardId, Long customerId) {
        Reward reward = rewardRepo.findById(rewardId)
                .orElseThrow(() -> new ResourceNotFoundException("Reward not found with id: " + rewardId));

        if (!reward.isEnabled()) {
            throw new BadRequestException("This reward is currently unavailable");
        }

        User customer = userRepo.findById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (customer.getCoins() < reward.getCoinsRequired()) {
            throw new BadRequestException("Not enough coins. You have " + customer.getCoins()
                    + " coins but need " + reward.getCoinsRequired());
        }

        // ✅ Sirf validate karo — history aur coins order place hone pe save/deduct honge
        RewardResponse response = toResponse(reward);
        response.setCoinsDeducted(reward.getCoinsRequired());
        response.setRemainingCoins(customer.getCoins());
        return response;
    }

    public List<RedemptionHistoryResponse> getAllRedemptions() {
        return redemptionHistoryRepo.findAllByOrderByRedeemedAtDesc()
                .stream().map(this::toHistoryResponse).collect(Collectors.toList());
    }

    public List<RedemptionHistoryResponse> getCustomerRedemptions(Long customerId) {
        return redemptionHistoryRepo.findByCustomerIdOrderByRedeemedAtDesc(customerId)
                .stream().map(this::toHistoryResponse).collect(Collectors.toList());
    }

    private RedemptionHistoryResponse toHistoryResponse(RedemptionHistory h) {
        return RedemptionHistoryResponse.builder()
                .id(h.getId())
                .customerId(h.getCustomer().getId())
                .customerName(h.getCustomer().getName())
                .customerEmail(h.getCustomer().getEmail())
                .rewardId(h.getReward().getId())
                .productName(h.getProductName())
                .coinsUsed(h.getCoinsUsed())
                .redeemedAt(h.getRedeemedAt())
                .build();
    }

    private RewardResponse toResponse(Reward r) {
        return RewardResponse.builder()
                .id(r.getId())
                .productId(r.getProduct() != null ? r.getProduct().getId() : null)
                .productName(r.getProduct() != null ? r.getProduct().getName() : null)
                .productImage(r.getProduct() != null ? r.getProduct().getImage() : null)
                .coinsRequired(r.getCoinsRequired())
                .enabled(r.isEnabled())
                .build();
    }
}
