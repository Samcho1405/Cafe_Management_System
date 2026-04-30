package com.thewhitebeard.controller;

import com.thewhitebeard.config.JwtContextUtil;
import com.thewhitebeard.dto.request.OrderStatusRequest;
import com.thewhitebeard.dto.response.OrderResponse;
import com.thewhitebeard.service.OrderService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * StaffController — Staff sirf orders dekh sakta hai aur status update kar sakta hai.
 * Products, Categories, Feedback, Upsell, Rewards access nahi.
 *
 * Note: GET /api/orders aur PUT /api/orders/{id} dono
 *       OrderController mein bhi hain — wahan bhi STAFF allowed hai.
 *       Yeh controller staff-specific convenience endpoint hai.
 */
@RestController
@RequestMapping("/api/staff")
@RequiredArgsConstructor
public class StaffController {

    private final OrderService orderService;

    // GET /api/staff/orders  →  STAFF only — sabhi orders
    @GetMapping("/orders")
    public ResponseEntity<List<OrderResponse>> getOrders(
            @RequestParam(required = false) Long customerId,
            HttpServletRequest request) {
        JwtContextUtil.requireRole(request, "STAFF");

        if (customerId != null) {
            return ResponseEntity.ok(orderService.getOrdersByCustomer(customerId));
        }
        return ResponseEntity.ok(orderService.getAllOrders());
    }

    // PUT /api/staff/orders/{id}  →  STAFF only — status update
    @PutMapping("/orders/{id}")
    public ResponseEntity<OrderResponse> updateOrderStatus(
            @PathVariable Long id,
            @Valid @RequestBody OrderStatusRequest body,
            HttpServletRequest request) {
        JwtContextUtil.requireRole(request, "STAFF");
        return ResponseEntity.ok(orderService.updateOrderStatus(id, body));
    }
}
