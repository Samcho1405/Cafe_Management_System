package com.thewhitebeard.repository;

import com.thewhitebeard.model.RedemptionHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface RedemptionHistoryRepository extends JpaRepository<RedemptionHistory, Long> {
    List<RedemptionHistory> findAllByOrderByRedeemedAtDesc();
    List<RedemptionHistory> findByCustomerIdOrderByRedeemedAtDesc(Long customerId);
}
