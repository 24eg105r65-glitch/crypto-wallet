package com.cryptovault.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public class SendTransactionDto {

    @NotNull(message = "Please select a source wallet")
    private Long senderWalletId;

    @NotBlank(message = "Destination wallet address is required")
    private String receiverAddress;

    @NotNull(message = "Amount is required")
    @DecimalMin(value = "0.00000001", message = "Amount must be greater than 0")
    private BigDecimal amount;

    private String note;

    public SendTransactionDto() {
    }

    public SendTransactionDto(Long senderWalletId, String receiverAddress, BigDecimal amount, String note) {
        this.senderWalletId = senderWalletId;
        this.receiverAddress = receiverAddress;
        this.amount = amount;
        this.note = note;
    }

    public Long getSenderWalletId() {
        return senderWalletId;
    }

    public void setSenderWalletId(Long senderWalletId) {
        this.senderWalletId = senderWalletId;
    }

    public String getReceiverAddress() {
        return receiverAddress;
    }

    public void setReceiverAddress(String receiverAddress) {
        this.receiverAddress = receiverAddress;
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
        private Long senderWalletId;
        private String receiverAddress;
        private BigDecimal amount;
        private String note;

        public Builder senderWalletId(Long senderWalletId) {
            this.senderWalletId = senderWalletId;
            return this;
        }

        public Builder receiverAddress(String receiverAddress) {
            this.receiverAddress = receiverAddress;
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

        public SendTransactionDto build() {
            return new SendTransactionDto(senderWalletId, receiverAddress, amount, note);
        }
    }
}
