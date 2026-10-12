package com.cryptovault.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "currencies")
public class Currency {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 10)
    private String code;

    @Column(nullable = false, length = 50)
    private String name;

    @Column(nullable = false, length = 10)
    private String symbol;

    @Column(name = "exchange_rate_inr", nullable = false, precision = 18, scale = 2)
    private BigDecimal exchangeRateInr;

    @Column(name = "exchange_rate_usd", nullable = false, precision = 18, scale = 4)
    private BigDecimal exchangeRateUsd;

    @Column(name = "change_24h", precision = 6, scale = 2)
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
    private boolean active = true;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public Currency() {}

    public Currency(Long id, String code, String name, String symbol, BigDecimal exchangeRateInr, BigDecimal exchangeRateUsd, BigDecimal change24h, BigDecimal marketCapUsd, BigDecimal volume24hUsd, String iconClass, String badgeColor, boolean active, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.code = code;
        this.name = name;
        this.symbol = symbol;
        this.exchangeRateInr = exchangeRateInr;
        this.exchangeRateUsd = exchangeRateUsd;
        this.change24h = change24h != null ? change24h : BigDecimal.ZERO;
        this.marketCapUsd = marketCapUsd;
        this.volume24hUsd = volume24hUsd;
        this.iconClass = iconClass;
        this.badgeColor = badgeColor;
        this.active = active;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static CurrencyBuilder builder() {
        return new CurrencyBuilder();
    }

    public static class CurrencyBuilder {
        private Long id;
        private String code;
        private String name;
        private String symbol;
        private BigDecimal exchangeRateInr;
        private BigDecimal exchangeRateUsd;
        private BigDecimal change24h = BigDecimal.ZERO;
        private BigDecimal marketCapUsd;
        private BigDecimal volume24hUsd;
        private String iconClass;
        private String badgeColor;
        private boolean active = true;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        public CurrencyBuilder id(Long id) { this.id = id; return this; }
        public CurrencyBuilder code(String code) { this.code = code; return this; }
        public CurrencyBuilder name(String name) { this.name = name; return this; }
        public CurrencyBuilder symbol(String symbol) { this.symbol = symbol; return this; }
        public CurrencyBuilder exchangeRateInr(BigDecimal exchangeRateInr) { this.exchangeRateInr = exchangeRateInr; return this; }
        public CurrencyBuilder exchangeRateUsd(BigDecimal exchangeRateUsd) { this.exchangeRateUsd = exchangeRateUsd; return this; }
        public CurrencyBuilder change24h(BigDecimal change24h) { this.change24h = change24h; return this; }
        public CurrencyBuilder marketCapUsd(BigDecimal marketCapUsd) { this.marketCapUsd = marketCapUsd; return this; }
        public CurrencyBuilder volume24hUsd(BigDecimal volume24hUsd) { this.volume24hUsd = volume24hUsd; return this; }
        public CurrencyBuilder iconClass(String iconClass) { this.iconClass = iconClass; return this; }
        public CurrencyBuilder badgeColor(String badgeColor) { this.badgeColor = badgeColor; return this; }
        public CurrencyBuilder active(boolean active) { this.active = active; return this; }
        public CurrencyBuilder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }
        public CurrencyBuilder updatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; return this; }

        public Currency build() {
            return new Currency(id, code, name, symbol, exchangeRateInr, exchangeRateUsd, change24h, marketCapUsd, volume24hUsd, iconClass, badgeColor, active, createdAt, updatedAt);
        }
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getSymbol() { return symbol; }
    public void setSymbol(String symbol) { this.symbol = symbol; }
    public BigDecimal getExchangeRateInr() { return exchangeRateInr; }
    public void setExchangeRateInr(BigDecimal exchangeRateInr) { this.exchangeRateInr = exchangeRateInr; }
    public BigDecimal getExchangeRateUsd() { return exchangeRateUsd; }
    public void setExchangeRateUsd(BigDecimal exchangeRateUsd) { this.exchangeRateUsd = exchangeRateUsd; }
    public BigDecimal getChange24h() { return change24h; }
    public void setChange24h(BigDecimal change24h) { this.change24h = change24h; }
    public BigDecimal getMarketCapUsd() { return marketCapUsd; }
    public void setMarketCapUsd(BigDecimal marketCapUsd) { this.marketCapUsd = marketCapUsd; }
    public BigDecimal getVolume24hUsd() { return volume24hUsd; }
    public void setVolume24hUsd(BigDecimal volume24hUsd) { this.volume24hUsd = volume24hUsd; }
    public String getIconClass() { return iconClass; }
    public void setIconClass(String iconClass) { this.iconClass = iconClass; }
    public String getBadgeColor() { return badgeColor; }
    public void setBadgeColor(String badgeColor) { this.badgeColor = badgeColor; }
    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
