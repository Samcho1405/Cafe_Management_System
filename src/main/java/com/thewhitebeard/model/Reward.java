package com.thewhitebeard.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "rewards")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Reward {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(nullable = false)
    private int coinsRequired;

    @Column(nullable = false)
    @Builder.Default
    private boolean enabled = true;
}
