package com.thewhitebeard.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "coin_settings")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CoinSetting {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Har X rupees pe 1 coin milega
    // e.g. amountPerCoin = 100 → ₹100 spend karo, 1 coin pao
    private double amountPerCoin;
}
