package com.thewhitebeard.controller;

import com.thewhitebeard.config.JwtContextUtil;
import com.thewhitebeard.model.CoinSetting;
import com.thewhitebeard.repository.CoinSettingRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/coin-settings")
@RequiredArgsConstructor
public class CoinSettingController {

    private final CoinSettingRepository coinSettingRepo;

    // GET /api/coin-settings  →  current setting
    @GetMapping
    public ResponseEntity<CoinSetting> getSetting() {
        CoinSetting setting = coinSettingRepo.findAll()
                .stream().findFirst()
                .orElse(CoinSetting.builder().id(1L).amountPerCoin(100).build());
        return ResponseEntity.ok(setting);
    }

    // PUT /api/coin-settings  →  ADMIN only
    @PutMapping
    public ResponseEntity<CoinSetting> updateSetting(
            @RequestBody Map<String, Double> body,
            HttpServletRequest request) {
        JwtContextUtil.requireRole(request, "ADMIN");

        double amountPerCoin = body.getOrDefault("amountPerCoin", 100.0);

        CoinSetting setting = coinSettingRepo.findAll()
                .stream().findFirst()
                .orElse(new CoinSetting());

        setting.setAmountPerCoin(amountPerCoin);
        return ResponseEntity.ok(coinSettingRepo.save(setting));
    }
}
