package com.thewhitebeard.repository;

import com.thewhitebeard.model.CoinSetting;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CoinSettingRepository extends JpaRepository<CoinSetting, Long> {
}
