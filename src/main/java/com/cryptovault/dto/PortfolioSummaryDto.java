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
public class PortfolioSummaryDto {
    private BigDecimal totalBalanceInr;
    private BigDecimal totalBalanceUsd;
    private int walletCount;
    private int transactionCount;
    private BigDecimal change24hPercentage;
    private String topAsset;
    private BigDecimal topAssetBalance;
}
