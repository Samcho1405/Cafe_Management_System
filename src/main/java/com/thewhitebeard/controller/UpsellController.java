package com.thewhitebeard.controller;

import com.thewhitebeard.config.JwtContextUtil;
import com.thewhitebeard.dto.request.UpsellRequest;
import com.thewhitebeard.dto.response.ApiResponse;
import com.thewhitebeard.dto.response.UpsellResponse;
import com.thewhitebeard.service.UpsellService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/upsell")
@RequiredArgsConstructor
public class UpsellController {

    private final UpsellService upsellService;

    // GET /api/upsell             →  ADMIN (returns all rules)
    // GET /api/upsell?cartQuantity=3  →  CUSTOMER (returns matching rule or null)
    @GetMapping
    public ResponseEntity<?> getUpsell(
            @RequestParam(required = false) Integer cartQuantity,
            HttpServletRequest request) {
        JwtContextUtil.requireRole(request, "ADMIN", "STAFF", "CUSTOMER");

        if (cartQuantity != null) {
            // Customer cart check — may return null if no rule matches
            UpsellResponse result = upsellService.checkUpsell(cartQuantity);
            return ResponseEntity.ok(result);
        }

        List<UpsellResponse> all = upsellService.getAllUpsells();
        return ResponseEntity.ok(all);
    }

    // POST /api/upsell  →  ADMIN only
    @PostMapping
    public ResponseEntity<UpsellResponse> createUpsell(
            @Valid @RequestBody UpsellRequest body,
            HttpServletRequest request) {
        JwtContextUtil.requireRole(request, "ADMIN");
        return ResponseEntity.ok(upsellService.createUpsell(body));
    }

    // PUT /api/upsell/{id}  →  ADMIN only
    @PutMapping("/{id}")
    public ResponseEntity<UpsellResponse> updateUpsell(
            @PathVariable Long id,
            @Valid @RequestBody UpsellRequest body,
            HttpServletRequest request) {
        JwtContextUtil.requireRole(request, "ADMIN");
        return ResponseEntity.ok(upsellService.updateUpsell(id, body));
    }

    // DELETE /api/upsell/{id}  →  ADMIN only
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteUpsell(
            @PathVariable Long id,
            HttpServletRequest request) {
        JwtContextUtil.requireRole(request, "ADMIN");
        upsellService.deleteUpsell(id);
        return ResponseEntity.ok(ApiResponse.ok("Upsell rule deleted successfully"));
    }
}
