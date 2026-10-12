package com.cryptovault.dto;

import java.math.BigDecimal;

public class PortfolioSummaryDto {
    private BigDecimal totalBalanceInr;
    private BigDecimal totalBalanceUsd;
    private int walletCount;
    private int transactionCount;
    private BigDecimal change24hPercentage;
    private String topAsset;
    private BigDecimal topAssetBalance;

    public PortfolioSummaryDto() {
    }

    public PortfolioSummaryDto(BigDecimal totalBalanceInr, BigDecimal totalBalanceUsd, int walletCount,
                               int transactionCount, BigDecimal change24hPercentage, String topAsset,
                               BigDecimal topAssetBalance) {
        this.totalBalanceInr = totalBalanceInr;
        this.totalBalanceUsd = totalBalanceUsd;
        this.walletCount = walletCount;
        this.transactionCount = transactionCount;
        this.change24hPercentage = change24hPercentage;
        this.topAsset = topAsset;
        this.topAssetBalance = topAssetBalance;
    }

    public BigDecimal getTotalBalanceInr() {
        return totalBalanceInr;
    }

    public void setTotalBalanceInr(BigDecimal totalBalanceInr) {
        this.totalBalanceInr = totalBalanceInr;
    }

    public BigDecimal getTotalBalanceUsd() {
        return totalBalanceUsd;
    }

    public void setTotalBalanceUsd(BigDecimal totalBalanceUsd) {
        this.totalBalanceUsd = totalBalanceUsd;
    }

    public int getWalletCount() {
        return walletCount;
    }

    public void setWalletCount(int walletCount) {
        this.walletCount = walletCount;
    }

    public int getTransactionCount() {
        return transactionCount;
    }

    public void setTransactionCount(int transactionCount) {
        this.transactionCount = transactionCount;
    }

    public BigDecimal getChange24hPercentage() {
        return change24hPercentage;
    }

    public void setChange24hPercentage(BigDecimal change24hPercentage) {
        this.change24hPercentage = change24hPercentage;
    }

    public String getTopAsset() {
        return topAsset;
    }

    public void setTopAsset(String topAsset) {
        this.topAsset = topAsset;
    }

    public BigDecimal getTopAssetBalance() {
        return topAssetBalance;
    }

    public void setTopAssetBalance(BigDecimal topAssetBalance) {
        this.topAssetBalance = topAssetBalance;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private BigDecimal totalBalanceInr;
        private BigDecimal totalBalanceUsd;
        private int walletCount;
        private int transactionCount;
        private BigDecimal change24hPercentage;
        private String topAsset;
        private BigDecimal topAssetBalance;

        public Builder totalBalanceInr(BigDecimal totalBalanceInr) {
            this.totalBalanceInr = totalBalanceInr;
            return this;
        }

        public Builder totalBalanceUsd(BigDecimal totalBalanceUsd) {
            this.totalBalanceUsd = totalBalanceUsd;
            return this;
        }

        public Builder walletCount(int walletCount) {
            this.walletCount = walletCount;
            return this;
        }

        public Builder transactionCount(int transactionCount) {
            this.transactionCount = transactionCount;
            return this;
        }

        public Builder change24hPercentage(BigDecimal change24hPercentage) {
            this.change24hPercentage = change24hPercentage;
            return this;
        }

        public Builder topAsset(String topAsset) {
            this.topAsset = topAsset;
            return this;
        }

        public Builder topAssetBalance(BigDecimal topAssetBalance) {
            this.topAssetBalance = topAssetBalance;
            return this;
        }

        public PortfolioSummaryDto build() {
            return new PortfolioSummaryDto(totalBalanceInr, totalBalanceUsd, walletCount, transactionCount, change24hPercentage, topAsset, topAssetBalance);
        }
    }
}
