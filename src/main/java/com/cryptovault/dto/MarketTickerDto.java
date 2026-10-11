package com.cryptovault.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
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
}
