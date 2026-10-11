package com.cryptovault.controller;

import com.cryptovault.entity.Currency;
import com.cryptovault.entity.User;
import com.cryptovault.security.CustomUserDetails;
import com.cryptovault.service.CurrencyService;
import com.cryptovault.service.NotificationService;
import com.cryptovault.service.TransactionService;
import com.cryptovault.service.UserService;
import com.cryptovault.service.WalletService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.util.List;

@Slf4j
@Controller
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
public class AdminController {

    private final UserService userService;
    private final WalletService walletService;
    private final TransactionService transactionService;
    private final CurrencyService currencyService;
    private final NotificationService notificationService;

    @GetMapping
    public String adminOverview(@AuthenticationPrincipal CustomUserDetails userDetails, Model model) {
        model.addAttribute("totalUsers", userService.countTotalUsers());
        model.addAttribute("activeWallets", walletService.countActiveWallets());
        model.addAttribute("totalTransactions", transactionService.countConfirmedTransactions());
        model.addAttribute("totalVolumeInr", transactionService.getTotalVolumeInr());
        model.addAttribute("recentTransactions", transactionService.getAllTransactions());
        model.addAttribute("currencies", currencyService.getAllCurrencies());
        model.addAttribute("unreadNotificationsCount", notificationService.getUnreadCount(userDetails.getId()));
        model.addAttribute("pageTitle", "Admin Command Center — CryptoVault");

        return "admin/index";
    }

    @GetMapping("/users")
    public String manageUsers(@AuthenticationPrincipal CustomUserDetails userDetails, Model model) {
        List<User> users = userService.getAllUsers();
        model.addAttribute("users", users);
        model.addAttribute("unreadNotificationsCount", notificationService.getUnreadCount(userDetails.getId()));
        model.addAttribute("pageTitle", "User Administration — CryptoVault");
        return "admin/users";
    }

    @PostMapping("/users/toggle/{id}")
    public String toggleUserStatus(@PathVariable("id") Long id,
                                   @AuthenticationPrincipal CustomUserDetails userDetails,
                                   RedirectAttributes redirectAttributes) {
        if (id.equals(userDetails.getId())) {
            redirectAttributes.addFlashAttribute("errorMessage", "Cannot disable your own admin account.");
            return "redirect:/admin/users";
        }
        User updated = userService.toggleUserActive(id);
        redirectAttributes.addFlashAttribute("successMessage",
                "User " + updated.getUsername() + " status is now " + (updated.isEnabled() ? "Active" : "Suspended"));
        return "redirect:/admin/users";
    }

    @GetMapping("/currencies")
    public String manageCurrencies(@AuthenticationPrincipal CustomUserDetails userDetails, Model model) {
        model.addAttribute("currencies", currencyService.getAllCurrencies());
        model.addAttribute("unreadNotificationsCount", notificationService.getUnreadCount(userDetails.getId()));
        model.addAttribute("pageTitle", "Currency & Exchange Rate Management — CryptoVault");
        return "admin/currencies";
    }

    @PostMapping("/currencies/update")
    public String updateCurrencyRate(@RequestParam("currencyId") Long currencyId,
                                     @RequestParam("inrRate") BigDecimal inrRate,
                                     @RequestParam("usdRate") BigDecimal usdRate,
                                     @RequestParam(value = "change24h", required = false) BigDecimal change24h,
                                     RedirectAttributes redirectAttributes) {
        try {
            Currency updated = currencyService.updateExchangeRates(currencyId, inrRate, usdRate, change24h);
            redirectAttributes.addFlashAttribute("successMessage",
                    "Exchange rate for " + updated.getCode() + " updated successfully!");
        } catch (Exception ex) {
            redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
        }
        return "redirect:/admin/currencies";
    }

    @GetMapping("/transactions")
    public String systemTransactions(@AuthenticationPrincipal CustomUserDetails userDetails, Model model) {
        model.addAttribute("transactions", transactionService.getAllTransactions());
        model.addAttribute("unreadNotificationsCount", notificationService.getUnreadCount(userDetails.getId()));
        model.addAttribute("pageTitle", "System-Wide Ledger Audit — CryptoVault");
        return "admin/transactions";
    }
}
