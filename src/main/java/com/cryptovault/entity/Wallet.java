package com.cryptovault.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "wallets")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Wallet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "currency_id", nullable = false)
    private Currency currency;

    @Column(name = "wallet_address", nullable = false, unique = true, length = 64)
    private String walletAddress;

    @Column(name = "wallet_name", length = 100)
    private String walletName;

    @Column(nullable = false, precision = 24, scale = 8)
    @Builder.Default
    private BigDecimal balance = BigDecimal.ZERO;

    @Column(name = "is_active", nullable = false)
    @Builder.Default
    private boolean active = true;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // Helper methods for calculating fiat values
    public BigDecimal getFiatValueInr() {
        if (balance == null || currency == null || currency.getExchangeRateInr() == null) {
            return BigDecimal.ZERO;
        }
        return balance.multiply(currency.getExchangeRateInr());
    }

    public BigDecimal getFiatValueUsd() {
        if (balance == null || currency == null || currency.getExchangeRateUsd() == null) {
            return BigDecimal.ZERO;
        }
        return balance.multiply(currency.getExchangeRateUsd());
    }
}
