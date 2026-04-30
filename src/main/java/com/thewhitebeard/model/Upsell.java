package com.thewhitebeard.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "upsell")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Upsell {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(nullable = false)
    private int triggerQuantity;

    @Column(columnDefinition = "TEXT")
    private String message;

    @Column(nullable = false)
    private double price;

    @Column(nullable = false)
    @Builder.Default
    private boolean enabled = true;
}
