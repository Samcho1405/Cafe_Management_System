package com.thewhitebeard.serviceimpl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thewhitebeard.dto.request.OrderRequest;
import com.thewhitebeard.dto.request.OrderRequest.CartItemRequest;
import com.thewhitebeard.dto.request.OrderStatusRequest;
import com.thewhitebeard.dto.response.OrderResponse;
import com.thewhitebeard.exception.BadRequestException;
import com.thewhitebeard.exception.ResourceNotFoundException;
import com.thewhitebeard.model.Order;
import com.thewhitebeard.model.User;
import com.thewhitebeard.repository.OrderRepository;
import com.thewhitebeard.repository.UserRepository;
import com.thewhitebeard.repository.ProductRepository;
import com.thewhitebeard.repository.CoinSettingRepository;
import com.thewhitebeard.repository.RewardRepository;
import com.thewhitebeard.repository.RedemptionHistoryRepository;
import com.thewhitebeard.model.Reward;
import com.thewhitebeard.model.RedemptionHistory;
import com.thewhitebeard.model.Product;
import com.thewhitebeard.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepo;
    private final UserRepository userRepo;
    private final ProductRepository productRepo;
    private final CoinSettingRepository coinSettingRepo;
    private final RewardRepository rewardRepo;
    private final RedemptionHistoryRepository redemptionHistoryRepo;
    private final ObjectMapper objectMapper;

    @Override
    public List<OrderResponse> getAllOrders() {
        return orderRepo.findAllByOrderByCreatedAtDesc()
                .stream().map(this::toResponse).collect(Collectors.toList());
    }

    @Override
    public List<OrderResponse> getOrdersByCustomer(Long customerId) {
        return orderRepo.findByCustomerIdOrderByCreatedAtDesc(customerId)
                .stream().map(this::toResponse).collect(Collectors.toList());
    }

    @Override
    public OrderResponse placeOrder(OrderRequest request, Long customerId) {
        User customer = userRepo.findById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        // Stock check, reduce aur price calculate
        double total = 0;
        for (CartItemRequest item : request.getCartItems()) {
            Product product = productRepo.findById(item.getId())
                    .orElseThrow(() -> new BadRequestException("Product not found: " + item.getId()));

            if (product.getStock() < item.getQty()) {
                throw new BadRequestException(
                    product.getName() + " ka stock sirf " + product.getStock() + " bacha hai");
            }

            // Reward item → ₹0, Normal item → DB se actual price
            if (item.isReward()) {
                item.setPrice(0);  // force ₹0
            } else {
                item.setPrice(product.getPrice()); // DB se real price
                total += product.getPrice() * item.getQty();
            }

            product.setStock(product.getStock() - item.getQty());
            productRepo.save(product);
        }

        // Serialize cart items to JSON
        String itemsJson;
        try {
            itemsJson = objectMapper.writeValueAsString(request.getCartItems());
        } catch (Exception e) {
            throw new BadRequestException("Invalid cart data");
        }

        Order order = Order.builder()
                .customer(customer)
                .items(itemsJson)
                .total(total)
                .paymentMethod(request.getPaymentMethod())
                .status(Order.Status.Pending)
                .build();

        Order saved = orderRepo.save(order);

        // ✅ Reward items ke coins order place hone pe kato
        for (CartItemRequest item : request.getCartItems()) {
            if (item.isReward()) {
                // Reward ID se coinsRequired fetch karo
                Reward reward = rewardRepo.findAll().stream()
                        .filter(r -> r.getProduct() != null && r.getProduct().getId().equals(item.getId()))
                        .findFirst().orElse(null);
                if (reward != null) {
                    if (customer.getCoins() < reward.getCoinsRequired()) {
                        throw new BadRequestException("Coins kam ho gaye! Reward order cancel karo.");
                    }
                    customer.setCoins(customer.getCoins() - reward.getCoinsRequired());

                    // ✅ Redemption history order place hone pe save karo
                    RedemptionHistory history = RedemptionHistory.builder()
                            .customer(customer)
                            .reward(reward)
                            .productName(item.getName())
                            .coinsUsed(reward.getCoinsRequired())
                            .build();
                    redemptionHistoryRepo.save(history);
                }
            }
        }

        // Normal items pe coins earn karo
        boolean isFullyRewardOrder = request.getCartItems().stream().allMatch(CartItemRequest::isReward);
        if (!isFullyRewardOrder && total > 0) {
            double amountPerCoin = coinSettingRepo.findAll()
                    .stream().findFirst()
                    .map(s -> s.getAmountPerCoin())
                    .orElse(100.0);
            int coinsEarned = (int) Math.floor(total / amountPerCoin);
            if (coinsEarned < 1) 
                coinsEarned = 1;
            customer.setCoins(customer.getCoins() + coinsEarned);
        }
        userRepo.save(customer);

        return toResponse(saved);
    }

    @Override
    public OrderResponse updateOrderStatus(Long orderId, OrderStatusRequest request) {
        Order order = orderRepo.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + orderId));

        try {
            order.setStatus(Order.Status.valueOf(request.getStatus()));
        } catch (IllegalArgumentException e) {
            throw new BadRequestException("Invalid status: " + request.getStatus());
        }

        return toResponse(orderRepo.save(order));
    }

    private OrderResponse toResponse(Order o) {
        List<CartItemRequest> items;
        try {
            items = objectMapper.readValue(o.getItems(), new TypeReference<>() {});
        } catch (Exception e) {
            items = List.of();
        }

        return OrderResponse.builder()
                .id(o.getId())
                .customerId(o.getCustomer() != null ? o.getCustomer().getId() : null)
                .customerName(o.getCustomer() != null ? o.getCustomer().getName() : null)
                .items(items)
                .total(o.getTotal())
                .status(o.getStatus().name())
                .paymentMethod(o.getPaymentMethod())
                .createdAt(o.getCreatedAt())
                .build();
    }
}
