package com.cryptovault.controller;

import com.cryptovault.dto.CreateWalletDto;
import com.cryptovault.dto.DepositDto;
import com.cryptovault.entity.Currency;
import com.cryptovault.entity.Transaction;
import com.cryptovault.entity.Wallet;
import com.cryptovault.repository.TransactionRepository;
import com.cryptovault.security.CustomUserDetails;
import com.cryptovault.service.CurrencyService;
import com.cryptovault.service.NotificationService;
import com.cryptovault.service.WalletService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Slf4j
@Controller
@RequestMapping("/wallets")
@RequiredArgsConstructor
public class WalletController {

    private final WalletService walletService;
    private final CurrencyService currencyService;
    private final TransactionRepository transactionRepository;
    private final NotificationService notificationService;

    @GetMapping
    public String listWallets(@AuthenticationPrincipal CustomUserDetails userDetails, Model model) {
        Long userId = userDetails.getId();
        List<Wallet> wallets = walletService.getUserWallets(userId);
        List<Currency> availableCurrencies = currencyService.getAllActiveCurrencies();

        model.addAttribute("wallets", wallets);
        model.addAttribute("availableCurrencies", availableCurrencies);
        model.addAttribute("createWalletDto", new CreateWalletDto());
        model.addAttribute("depositDto", new DepositDto());
        model.addAttribute("unreadNotificationsCount", notificationService.getUnreadCount(userId));
        model.addAttribute("pageTitle", "My Multi-Asset Crypto Wallets — CryptoVault");

        return "wallets/index";
    }

    @GetMapping("/{id}")
    public String walletDetail(@PathVariable("id") Long walletId,
                               @AuthenticationPrincipal CustomUserDetails userDetails,
                               Model model) {
        Long userId = userDetails.getId();
        Wallet wallet = walletService.getWalletById(walletId)
                .orElseThrow(() -> new IllegalArgumentException("Wallet not found"));

        if (!wallet.getUser().getId().equals(userId)) {
            return "redirect:/wallets?error=unauthorized";
        }

        List<Transaction> walletTransactions = transactionRepository.findByWallet(wallet);

        model.addAttribute("wallet", wallet);
        model.addAttribute("transactions", walletTransactions);
        model.addAttribute("depositDto", DepositDto.builder().walletId(wallet.getId()).build());
        model.addAttribute("unreadNotificationsCount", notificationService.getUnreadCount(userId));
        model.addAttribute("pageTitle", wallet.getWalletName() + " (" + wallet.getCurrency().getCode() + ") — CryptoVault");

        return "wallets/detail";
    }

    @PostMapping("/create")
    public String createWallet(@Valid @ModelAttribute("createWalletDto") CreateWalletDto dto,
                               BindingResult bindingResult,
                               @AuthenticationPrincipal CustomUserDetails userDetails,
                               RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Invalid currency selection for wallet creation.");
            return "redirect:/wallets";
        }

        try {
            Wallet wallet = walletService.createWallet(userDetails.getId(), dto);
            redirectAttributes.addFlashAttribute("successMessage",
                    "Created new " + wallet.getCurrency().getCode() + " wallet: " + wallet.getWalletAddress());
        } catch (Exception ex) {
            redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
        }

        return "redirect:/wallets";
    }

    @PostMapping("/deposit")
    public String depositFunds(@Valid @ModelAttribute("depositDto") DepositDto depositDto,
                               BindingResult bindingResult,
                               @AuthenticationPrincipal CustomUserDetails userDetails,
                               RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Invalid deposit amount or wallet selected.");
            return "redirect:/wallets";
        }

        try {
            Wallet wallet = walletService.depositFunds(userDetails.getId(), depositDto);
            redirectAttributes.addFlashAttribute("successMessage",
                    "Successfully credited +" + depositDto.getAmount() + " " + wallet.getCurrency().getCode() + " to your vault!");
        } catch (Exception ex) {
            redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
        }

        return "redirect:/wallets";
    }

    @PostMapping("/faucet/{id}")
    public String claimFaucet(@PathVariable("id") Long walletId,
                              @AuthenticationPrincipal CustomUserDetails userDetails,
                              RedirectAttributes redirectAttributes) {
        try {
            Wallet wallet = walletService.claimFaucet(userDetails.getId(), walletId);
            redirectAttributes.addFlashAttribute("successMessage",
                    "Airdrop successful! Free testnet " + wallet.getCurrency().getCode() + " deposited to your vault.");
        } catch (Exception ex) {
            redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
        }

        return "redirect:/wallets/" + walletId;
    }
}
