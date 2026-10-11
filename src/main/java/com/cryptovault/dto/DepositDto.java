package com.cryptovault.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DepositDto {

    @NotNull(message = "Please select a wallet")
    private Long walletId;

    @NotNull(message = "Deposit amount is required")
    @DecimalMin(value = "0.00000001", message = "Deposit amount must be positive")
    private BigDecimal amount;

    private String note;
}
