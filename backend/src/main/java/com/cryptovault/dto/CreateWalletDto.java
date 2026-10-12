package com.cryptovault.dto;

import jakarta.validation.constraints.NotBlank;

public class CreateWalletDto {

    @NotBlank(message = "Please select a currency")
    private String currencyCode;

    private String walletName;

    public CreateWalletDto() {
    }

    public CreateWalletDto(String currencyCode, String walletName) {
        this.currencyCode = currencyCode;
        this.walletName = walletName;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public String getWalletName() {
        return walletName;
    }

    public void setWalletName(String walletName) {
        this.walletName = walletName;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String currencyCode;
        private String walletName;

        public Builder currencyCode(String currencyCode) {
            this.currencyCode = currencyCode;
            return this;
        }

        public Builder walletName(String walletName) {
            this.walletName = walletName;
            return this;
        }

        public CreateWalletDto build() {
            return new CreateWalletDto(currencyCode, walletName);
        }
    }
}
