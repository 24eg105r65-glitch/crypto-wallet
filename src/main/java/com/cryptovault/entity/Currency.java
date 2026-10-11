package com.cryptovault.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "currencies")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Currency {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 10)
    private String code; // e.g. BTC, ETH, SOL, USDT, XRP, ADA

    @Column(nullable = false, length = 50)
    private String name; // e.g. Bitcoin, Ethereum

    @Column(nullable = false, length = 10)
    private String symbol; // e.g. ₿, Ξ, ◎, ₮

    @Column(name = "exchange_rate_inr", nullable = false, precision = 18, scale = 2)
    private BigDecimal exchangeRateInr;

    @Column(name = "exchange_rate_usd", nullable = false, precision = 18, scale = 4)
    private BigDecimal exchangeRateUsd;

    @Column(name = "change_24h", precision = 6, scale = 2)
    @Builder.Default
    private BigDecimal change24h = BigDecimal.ZERO;

    @Column(name = "market_cap_usd", precision = 20, scale = 2)
    private BigDecimal marketCapUsd;

    @Column(name = "volume_24h_usd", precision = 20, scale = 2)
    private BigDecimal volume24hUsd;

    @Column(name = "icon_class", length = 50)
    private String iconClass;

    @Column(name = "badge_color", length = 30)
    private String badgeColor;

    @Column(name = "is_active", nullable = false)
    @Builder.Default
    private boolean active = true;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
