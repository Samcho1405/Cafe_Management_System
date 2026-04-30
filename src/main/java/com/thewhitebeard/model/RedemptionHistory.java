package com.thewhitebeard.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "redemption_history")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RedemptionHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "customer_id", nullable = false)
    private User customer;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "reward_id", nullable = false)
    private Reward reward;

    @Column(nullable = false)
    private String productName;

    @Column(nullable = false)
    private int coinsUsed;

    @Column(nullable = false)
    @Builder.Default
    private LocalDateTime redeemedAt = LocalDateTime.now();
}
