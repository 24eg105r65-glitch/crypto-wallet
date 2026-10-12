package com.cryptovault.service;

import com.cryptovault.dto.CreateWalletDto;
import com.cryptovault.dto.DepositDto;
import com.cryptovault.dto.PortfolioSummaryDto;
import com.cryptovault.entity.Wallet;

import java.util.List;
import java.util.Optional;

public interface WalletService {
    Wallet createWallet(Long userId, CreateWalletDto dto);
    List<Wallet> getUserWallets(Long userId);
    Optional<Wallet> getWalletById(Long walletId);
    Optional<Wallet> getWalletByAddress(String address);
    Wallet depositFunds(Long userId, DepositDto depositDto);
    Wallet claimFaucet(Long userId, Long walletId);
    PortfolioSummaryDto getPortfolioSummary(Long userId);
    long countActiveWallets();
}
