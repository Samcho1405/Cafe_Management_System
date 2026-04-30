package com.thewhitebeard.service;

import com.thewhitebeard.dto.request.OrderRequest;
import com.thewhitebeard.dto.request.OrderStatusRequest;
import com.thewhitebeard.dto.response.OrderResponse;

import java.util.List;

public interface OrderService {
    List<OrderResponse> getAllOrders();
    List<OrderResponse> getOrdersByCustomer(Long customerId);
    OrderResponse placeOrder(OrderRequest request, Long customerId);
    OrderResponse updateOrderStatus(Long orderId, OrderStatusRequest request);
}
