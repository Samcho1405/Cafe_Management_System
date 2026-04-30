package com.thewhitebeard.controller;

import com.thewhitebeard.config.JwtContextUtil;
import com.thewhitebeard.dto.request.ChangePasswordRequest;
import com.thewhitebeard.dto.request.CreateStaffRequest;
import com.thewhitebeard.dto.response.ApiResponse;
import com.thewhitebeard.dto.response.UserResponse;
import com.thewhitebeard.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AdminController {

    private final UserService userService;

    // GET /api/customers  →  ADMIN only
    @GetMapping("/customers")
    public ResponseEntity<List<UserResponse>> getAllCustomers(HttpServletRequest request) {
        JwtContextUtil.requireRole(request, "ADMIN");
        return ResponseEntity.ok(userService.getAllCustomers());
    }

    // GET /api/staff  →  ADMIN only
    @GetMapping("/staff")
    public ResponseEntity<List<UserResponse>> getAllStaff(HttpServletRequest request) {
        JwtContextUtil.requireRole(request, "ADMIN");
        return ResponseEntity.ok(userService.getAllStaff());
    }

    // POST /api/staff  →  ADMIN only
    @PostMapping("/staff")
    public ResponseEntity<UserResponse> createStaff(
            @Valid @RequestBody CreateStaffRequest staffRequest,
            HttpServletRequest request) {
        JwtContextUtil.requireRole(request, "ADMIN");
        return ResponseEntity.ok(userService.createStaff(staffRequest));
    }

    // PUT /api/staff/{id}/password  →  ADMIN only
    @PutMapping("/staff/{id}/password")
    public ResponseEntity<ApiResponse> changeStaffPassword(
            @PathVariable Long id,
            @Valid @RequestBody ChangePasswordRequest passRequest,
            HttpServletRequest request) {
        JwtContextUtil.requireRole(request, "ADMIN");
        userService.changeStaffPassword(id, passRequest);
        return ResponseEntity.ok(ApiResponse.ok("Password changed successfully"));
    }

    // DELETE /api/staff/{id}  →  ADMIN only
    @DeleteMapping("/staff/{id}")
    public ResponseEntity<ApiResponse> deleteStaff(
            @PathVariable Long id,
            HttpServletRequest request) {
        JwtContextUtil.requireRole(request, "ADMIN");
        userService.deleteStaff(id);
        return ResponseEntity.ok(ApiResponse.ok("Staff deleted successfully"));
    }
}
