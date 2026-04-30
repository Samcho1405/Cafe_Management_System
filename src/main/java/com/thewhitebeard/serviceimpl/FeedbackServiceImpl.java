package com.thewhitebeard.serviceimpl;

import com.thewhitebeard.dto.request.FeedbackRequest;
import com.thewhitebeard.dto.response.FeedbackResponse;
import com.thewhitebeard.exception.BadRequestException;
import com.thewhitebeard.exception.ResourceNotFoundException;
import com.thewhitebeard.model.Feedback;
import com.thewhitebeard.model.Order;
import com.thewhitebeard.model.Product;
import com.thewhitebeard.model.User;
import com.thewhitebeard.repository.FeedbackRepository;
import com.thewhitebeard.repository.OrderRepository;
import com.thewhitebeard.repository.ProductRepository;
import com.thewhitebeard.repository.UserRepository;
import com.thewhitebeard.service.FeedbackService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FeedbackServiceImpl implements FeedbackService {

    private final FeedbackRepository feedbackRepo;
    private final OrderRepository orderRepo;
    private final UserRepository userRepo;
    private final ProductRepository productRepo;

    @Override
    public List<FeedbackResponse> getAllFeedback() {
        return feedbackRepo.findAll()
                .stream().map(this::toResponse).collect(Collectors.toList());
    }

    @Override
    public FeedbackResponse getByOrderId(Long orderId) {
        return feedbackRepo.findByOrder_Id(orderId)
                .stream().findFirst()
                .map(this::toResponse)
                .orElse(null);
    }

    @Override
    public FeedbackResponse submitFeedback(FeedbackRequest request, Long customerId) {
        Order order = orderRepo.findById(request.getOrderId())
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + request.getOrderId()));

        // Only Delivered orders can have feedback
        if (order.getStatus() != Order.Status.Delivered) {
            throw new BadRequestException("Feedback can only be given for delivered orders");
        }

        // Prevent duplicate feedback for same order
        if (feedbackRepo.existsByOrder_IdAndCustomer_Id(request.getOrderId(), customerId)) {
            throw new BadRequestException("You have already submitted feedback for this order");
        }

        User customer = userRepo.findById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Feedback.FeedbackBuilder builder = Feedback.builder()
                .order(order)
                .customer(customer)
                .message(request.getMessage())
                .rating(request.getRating());

        if (request.getProductId() != null) {
            Product product = productRepo.findById(request.getProductId())
                    .orElse(null);
            builder.product(product);
        }

        return toResponse(feedbackRepo.save(builder.build()));
    }

    private FeedbackResponse toResponse(Feedback f) {
        return FeedbackResponse.builder()
                .id(f.getId())
                .orderId(f.getOrder().getId())
                .customerName(f.getCustomer().getName())
                .productName(f.getProduct() != null ? f.getProduct().getName() : null)
                .message(f.getMessage())
                .rating(f.getRating())
                .build();
    }
}
