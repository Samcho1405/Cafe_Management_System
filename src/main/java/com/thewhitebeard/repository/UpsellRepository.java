package com.thewhitebeard.repository;

import com.thewhitebeard.model.Upsell;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UpsellRepository extends JpaRepository<Upsell, Long> {
    List<Upsell> findByEnabledTrue();
    Optional<Upsell> findFirstByEnabledTrueAndTriggerQuantityLessThanEqualOrderByTriggerQuantityDesc(int qty);
}
