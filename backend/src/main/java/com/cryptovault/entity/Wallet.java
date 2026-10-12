package com.cryptovault.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "wallets")
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
    private BigDecimal balance = BigDecimal.ZERO;

    @Column(name = "is_active", nullable = false)
    private boolean active = true;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public Wallet() {}

    public Wallet(Long id, User user, Currency currency, String walletAddress, String walletName, BigDecimal balance, boolean active, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.user = user;
        this.currency = currency;
        this.walletAddress = walletAddress;
        this.walletName = walletName;
        this.balance = balance != null ? balance : BigDecimal.ZERO;
        this.active = active;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static WalletBuilder builder() {
        return new WalletBuilder();
    }

    public static class WalletBuilder {
        private Long id;
        private User user;
        private Currency currency;
        private String walletAddress;
        private String walletName;
        private BigDecimal balance = BigDecimal.ZERO;
        private boolean active = true;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        public WalletBuilder id(Long id) { this.id = id; return this; }
        public WalletBuilder user(User user) { this.user = user; return this; }
        public WalletBuilder currency(Currency currency) { this.currency = currency; return this; }
        public WalletBuilder walletAddress(String walletAddress) { this.walletAddress = walletAddress; return this; }
        public WalletBuilder walletName(String walletName) { this.walletName = walletName; return this; }
        public WalletBuilder balance(BigDecimal balance) { this.balance = balance; return this; }
        public WalletBuilder active(boolean active) { this.active = active; return this; }
        public WalletBuilder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }
        public WalletBuilder updatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; return this; }

        public Wallet build() {
            return new Wallet(id, user, currency, walletAddress, walletName, balance, active, createdAt, updatedAt);
        }
    }

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

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    public Currency getCurrency() { return currency; }
    public void setCurrency(Currency currency) { this.currency = currency; }
    public String getWalletAddress() { return walletAddress; }
    public void setWalletAddress(String walletAddress) { this.walletAddress = walletAddress; }
    public String getWalletName() { return walletName; }
    public void setWalletName(String walletName) { this.walletName = walletName; }
    public BigDecimal getBalance() { return balance; }
    public void setBalance(BigDecimal balance) { this.balance = balance; }
    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
