package com.cryptovault.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public class DepositDto {

    @NotNull(message = "Please select a wallet")
    private Long walletId;

    @NotNull(message = "Deposit amount is required")
    @DecimalMin(value = "0.00000001", message = "Deposit amount must be positive")
    private BigDecimal amount;

    private String note;

    public DepositDto() {
    }

    public DepositDto(Long walletId, BigDecimal amount, String note) {
        this.walletId = walletId;
        this.amount = amount;
        this.note = note;
    }

    public Long getWalletId() {
        return walletId;
    }

    public void setWalletId(Long walletId) {
        this.walletId = walletId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Long walletId;
        private BigDecimal amount;
        private String note;

        public Builder walletId(Long walletId) {
            this.walletId = walletId;
            return this;
        }

        public Builder amount(BigDecimal amount) {
            this.amount = amount;
            return this;
        }

        public Builder note(String note) {
            this.note = note;
            return this;
        }

        public DepositDto build() {
            return new DepositDto(walletId, amount, note);
        }
    }
}
