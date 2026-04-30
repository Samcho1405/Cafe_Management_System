package com.thewhitebeard.controller;

import com.thewhitebeard.config.JwtContextUtil;
import com.thewhitebeard.dto.request.AboutRequest;
import com.thewhitebeard.dto.response.AboutResponse;
import com.thewhitebeard.service.AboutService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/about")
@RequiredArgsConstructor
public class AboutController {

    private final AboutService aboutService;

    // GET /api/about  →  public (no login needed)
    @GetMapping
    public ResponseEntity<AboutResponse> getAbout() {
        return ResponseEntity.ok(aboutService.getAbout());
    }

    // PUT /api/about  →  ADMIN only
    @PutMapping
    public ResponseEntity<AboutResponse> updateAbout(
            @RequestBody AboutRequest body,
            HttpServletRequest request) {
        JwtContextUtil.requireRole(request, "ADMIN");
        return ResponseEntity.ok(aboutService.updateAbout(body));
    }
}
