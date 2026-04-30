package com.thewhitebeard.controller;

import com.thewhitebeard.config.JwtContextUtil;
import com.thewhitebeard.dto.request.CategoryRequest;
import com.thewhitebeard.dto.response.ApiResponse;
import com.thewhitebeard.dto.response.CategoryResponse;
import com.thewhitebeard.service.CategoryService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    // GET /api/categories  →  Public (no login needed for menu)
    @GetMapping
    public ResponseEntity<List<CategoryResponse>> getAllCategories() {
        return ResponseEntity.ok(categoryService.getAllCategories());
    }

    // POST /api/categories  →  ADMIN only
    @PostMapping
    public ResponseEntity<CategoryResponse> createCategory(
            @Valid @RequestBody CategoryRequest body,
            HttpServletRequest request) {
        JwtContextUtil.requireRole(request, "ADMIN");
        return ResponseEntity.ok(categoryService.createCategory(body));
    }

    // PUT /api/categories/{id}  →  ADMIN only
    @PutMapping("/{id}")
    public ResponseEntity<CategoryResponse> updateCategory(
            @PathVariable Long id,
            @Valid @RequestBody CategoryRequest body,
            HttpServletRequest request) {
        JwtContextUtil.requireRole(request, "ADMIN");
        return ResponseEntity.ok(categoryService.updateCategory(id, body));
    }

    // DELETE /api/categories/{id}  →  ADMIN only
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteCategory(
            @PathVariable Long id,
            HttpServletRequest request) {
        JwtContextUtil.requireRole(request, "ADMIN");
        categoryService.deleteCategory(id);
        return ResponseEntity.ok(ApiResponse.ok("Category deleted successfully"));
    }
}
