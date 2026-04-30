package com.thewhitebeard.repository;

import com.thewhitebeard.model.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FeedbackRepository extends JpaRepository<Feedback, Long> {
    List<Feedback> findByOrder_Id(Long orderId);
    boolean existsByOrder_IdAndCustomer_Id(Long orderId, Long customerId);
}
