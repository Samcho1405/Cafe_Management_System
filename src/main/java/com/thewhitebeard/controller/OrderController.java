package com.thewhitebeard.controller;

import com.thewhitebeard.config.JwtContextUtil;
import com.thewhitebeard.dto.request.OrderRequest;
import com.thewhitebeard.dto.request.OrderStatusRequest;
import com.thewhitebeard.dto.response.OrderResponse;
import com.thewhitebeard.service.OrderService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    // GET /api/orders
    //   ADMIN / STAFF  →  all orders
    //   CUSTOMER       →  only their own orders
    @GetMapping
    public ResponseEntity<List<OrderResponse>> getOrders(
            @RequestParam(required = false) Long customerId,
            HttpServletRequest request) {

        JwtContextUtil.requireRole(request, "ADMIN", "STAFF", "CUSTOMER");
        String role   = JwtContextUtil.getRole(request);
        Long   userId = JwtContextUtil.getUserId(request);

        if ("CUSTOMER".equals(role)) {
            return ResponseEntity.ok(orderService.getOrdersByCustomer(userId));
        }

        // Admin/Staff: can filter by customerId or get all
        if (customerId != null) {
            return ResponseEntity.ok(orderService.getOrdersByCustomer(customerId));
        }

        return ResponseEntity.ok(orderService.getAllOrders());
    }

    // POST /api/orders  →  CUSTOMER only
    @PostMapping
    public ResponseEntity<OrderResponse> placeOrder(
            @Valid @RequestBody OrderRequest body,
            HttpServletRequest request) {
        JwtContextUtil.requireRole(request, "CUSTOMER");
        Long customerId = JwtContextUtil.getUserId(request);
        return ResponseEntity.ok(orderService.placeOrder(body, customerId));
    }

    // PUT /api/orders/{id}  →  ADMIN, STAFF
    @PutMapping("/{id}")
    public ResponseEntity<OrderResponse> updateOrderStatus(
            @PathVariable Long id,
            @Valid @RequestBody OrderStatusRequest body,
            HttpServletRequest request) {
        JwtContextUtil.requireRole(request, "ADMIN", "STAFF");
        return ResponseEntity.ok(orderService.updateOrderStatus(id, body));
    }
}
