package com.thewhitebeard.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "orders")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "customer_id", nullable = false)
    private User customer;

    // JSON Array string: [{"id":1,"name":"Cappuccino","price":120.0,"qty":2}]
    @Column(columnDefinition = "TEXT", nullable = false)
    private String items;

    @Column(nullable = false)
    private double total;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private Status status = Status.Pending;

    @Column(nullable = false)
    private String paymentMethod;

    @Column(nullable = false)
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();

    public enum Status {
        Pending, Accepted, Preparing, Ready, Completed, Delivered
    }
}
