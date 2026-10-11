package com.cryptovault.controller;

import com.cryptovault.dto.PortfolioSummaryDto;
import com.cryptovault.dto.SendTransactionDto;
import com.cryptovault.entity.User;
import com.cryptovault.entity.Wallet;
import com.cryptovault.security.CustomUserDetails;
import com.cryptovault.service.CurrencyService;
import com.cryptovault.service.NotificationService;
import com.cryptovault.service.TransactionService;
import com.cryptovault.service.WalletService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class DashboardController {

    private final WalletService walletService;
    private final TransactionService transactionService;
    private final CurrencyService currencyService;
    private final NotificationService notificationService;

    @GetMapping("/dashboard")
    public String dashboard(@AuthenticationPrincipal CustomUserDetails userDetails, Model model) {
        if (userDetails == null) {
            return "redirect:/login";
        }

        User user = userDetails.getUser();
        Long userId = user.getId();

        PortfolioSummaryDto summary = walletService.getPortfolioSummary(userId);
        List<Wallet> wallets = walletService.getUserWallets(userId);

        model.addAttribute("user", user);
        model.addAttribute("summary", summary);
        model.addAttribute("wallets", wallets);
        model.addAttribute("recentTransactions", transactionService.getRecentTransactions(userId, 6));
        model.addAttribute("tickers", currencyService.getMarketTickers());
        model.addAttribute("unreadNotificationsCount", notificationService.getUnreadCount(userId));
        model.addAttribute("recentNotifications", notificationService.getRecentNotifications(userId));
        model.addAttribute("sendDto", new SendTransactionDto());
        model.addAttribute("pageTitle", "Dashboard Overview — CryptoVault");

        return "dashboard/index";
    }
}
