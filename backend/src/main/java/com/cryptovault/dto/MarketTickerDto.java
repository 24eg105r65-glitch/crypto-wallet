package com.cryptovault.dto;

import java.math.BigDecimal;

public class MarketTickerDto {
    private Long id;
    private String code;
    private String name;
    private String symbol;
    private BigDecimal priceInr;
    private BigDecimal priceUsd;
    private BigDecimal change24h;
    private BigDecimal marketCapUsd;
    private BigDecimal volume24hUsd;
    private String iconClass;
    private String badgeColor;

    public MarketTickerDto() {
    }

    public MarketTickerDto(Long id, String code, String name, String symbol, BigDecimal priceInr,
                           BigDecimal priceUsd, BigDecimal change24h, BigDecimal marketCapUsd,
                           BigDecimal volume24hUsd, String iconClass, String badgeColor) {
        this.id = id;
        this.code = code;
        this.name = name;
        this.symbol = symbol;
        this.priceInr = priceInr;
        this.priceUsd = priceUsd;
        this.change24h = change24h;
        this.marketCapUsd = marketCapUsd;
        this.volume24hUsd = volume24hUsd;
        this.iconClass = iconClass;
        this.badgeColor = badgeColor;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public BigDecimal getPriceInr() {
        return priceInr;
    }

    public void setPriceInr(BigDecimal priceInr) {
        this.priceInr = priceInr;
    }

    public BigDecimal getPriceUsd() {
        return priceUsd;
    }

    public void setPriceUsd(BigDecimal priceUsd) {
        this.priceUsd = priceUsd;
    }

    public BigDecimal getChange24h() {
        return change24h;
    }

    public void setChange24h(BigDecimal change24h) {
        this.change24h = change24h;
    }

    public BigDecimal getMarketCapUsd() {
        return marketCapUsd;
    }

    public void setMarketCapUsd(BigDecimal marketCapUsd) {
        this.marketCapUsd = marketCapUsd;
    }

    public BigDecimal getVolume24hUsd() {
        return volume24hUsd;
    }

    public void setVolume24hUsd(BigDecimal volume24hUsd) {
        this.volume24hUsd = volume24hUsd;
    }

    public String getIconClass() {
        return iconClass;
    }

    public void setIconClass(String iconClass) {
        this.iconClass = iconClass;
    }

    public String getBadgeColor() {
        return badgeColor;
    }

    public void setBadgeColor(String badgeColor) {
        this.badgeColor = badgeColor;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Long id;
        private String code;
        private String name;
        private String symbol;
        private BigDecimal priceInr;
        private BigDecimal priceUsd;
        private BigDecimal change24h;
        private BigDecimal marketCapUsd;
        private BigDecimal volume24hUsd;
        private String iconClass;
        private String badgeColor;

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder code(String code) {
            this.code = code;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder symbol(String symbol) {
            this.symbol = symbol;
            return this;
        }

        public Builder priceInr(BigDecimal priceInr) {
            this.priceInr = priceInr;
            return this;
        }

        public Builder priceUsd(BigDecimal priceUsd) {
            this.priceUsd = priceUsd;
            return this;
        }

        public Builder change24h(BigDecimal change24h) {
            this.change24h = change24h;
            return this;
        }

        public Builder marketCapUsd(BigDecimal marketCapUsd) {
            this.marketCapUsd = marketCapUsd;
            return this;
        }

        public Builder volume24hUsd(BigDecimal volume24hUsd) {
            this.volume24hUsd = volume24hUsd;
            return this;
        }

        public Builder iconClass(String iconClass) {
            this.iconClass = iconClass;
            return this;
        }

        public Builder badgeColor(String badgeColor) {
            this.badgeColor = badgeColor;
            return this;
        }

        public MarketTickerDto build() {
            return new MarketTickerDto(id, code, name, symbol, priceInr, priceUsd, change24h, marketCapUsd, volume24hUsd, iconClass, badgeColor);
        }
    }
}
