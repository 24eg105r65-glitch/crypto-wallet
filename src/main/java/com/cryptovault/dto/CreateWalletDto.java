package com.cryptovault.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateWalletDto {

    @NotBlank(message = "Please select a currency")
    private String currencyCode;

    private String walletName;
}
