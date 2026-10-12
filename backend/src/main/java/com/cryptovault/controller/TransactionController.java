package com.cryptovault.controller;

import com.cryptovault.dto.SendTransactionDto;
import com.cryptovault.entity.Transaction;
import com.cryptovault.entity.Wallet;
import com.cryptovault.security.CustomUserDetails;
import com.cryptovault.service.CurrencyService;
import com.cryptovault.service.NotificationService;
import com.cryptovault.service.TransactionService;
import com.cryptovault.service.WalletService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/transactions")
public class TransactionController {

    private static final Logger log = LoggerFactory.getLogger(TransactionController.class);

    private final TransactionService transactionService;
    private final WalletService walletService;
    private final CurrencyService currencyService;
    private final NotificationService notificationService;

    public TransactionController(TransactionService transactionService,
                                 WalletService walletService,
                                 CurrencyService currencyService,
                                 NotificationService notificationService) {
        this.transactionService = transactionService;
        this.walletService = walletService;
        this.currencyService = currencyService;
        this.notificationService = notificationService;
    }

    @GetMapping
    public String listTransactions(@RequestParam(value = "currency", required = false) String currencyCode,
                                   @RequestParam(value = "search", required = false) String search,
                                   @AuthenticationPrincipal CustomUserDetails userDetails,
                                   Model model) {
        Long userId = userDetails.getId();
        List<Transaction> transactions = transactionService.getUserTransactions(userId);

        // Optional filtering
        if (currencyCode != null && !currencyCode.isEmpty() && !currencyCode.equalsIgnoreCase("ALL")) {
            transactions = transactions.stream()
                    .filter(t -> t.getCurrency().getCode().equalsIgnoreCase(currencyCode))
                    .toList();
        }
        if (search != null && !search.trim().isEmpty()) {
            String s = search.trim().toLowerCase();
            transactions = transactions.stream()
                    .filter(t -> (t.getTxHash() != null && t.getTxHash().toLowerCase().contains(s)) ||
                                 (t.getNote() != null && t.getNote().toLowerCase().contains(s)))
                    .toList();
        }

        model.addAttribute("transactions", transactions);
        model.addAttribute("currencies", currencyService.getAllActiveCurrencies());
        model.addAttribute("selectedCurrency", currencyCode);
        model.addAttribute("searchQuery", search);
        model.addAttribute("unreadNotificationsCount", notificationService.getUnreadCount(userId));
        model.addAttribute("pageTitle", "Transaction History & Explorer — CryptoVault");

        return "transactions/index";
    }

    @GetMapping("/send")
    public String sendPage(@RequestParam(value = "walletId", required = false) Long walletId,
                           @RequestParam(value = "recipient", required = false) String recipient,
                           @AuthenticationPrincipal CustomUserDetails userDetails,
                           Model model) {
        Long userId = userDetails.getId();
        List<Wallet> wallets = walletService.getUserWallets(userId);

        SendTransactionDto dto = new SendTransactionDto();
        if (walletId != null) {
            dto.setSenderWalletId(walletId);
        } else if (!wallets.isEmpty()) {
            dto.setSenderWalletId(wallets.get(0).getId());
        }
        if (recipient != null) {
            dto.setReceiverAddress(recipient);
        }

        model.addAttribute("sendDto", dto);
        model.addAttribute("wallets", wallets);
        model.addAttribute("unreadNotificationsCount", notificationService.getUnreadCount(userId));
        model.addAttribute("pageTitle", "Send Cryptocurrency — Zero-Latency Transfer");

        return "transactions/send";
    }

    @PostMapping("/send")
    public String handleSend(@Valid @ModelAttribute("sendDto") SendTransactionDto sendDto,
                             BindingResult bindingResult,
                             @AuthenticationPrincipal CustomUserDetails userDetails,
                             RedirectAttributes redirectAttributes,
                             Model model) {
        Long userId = userDetails.getId();
        List<Wallet> wallets = walletService.getUserWallets(userId);

        if (bindingResult.hasErrors()) {
            model.addAttribute("wallets", wallets);
            model.addAttribute("unreadNotificationsCount", notificationService.getUnreadCount(userId));
            model.addAttribute("pageTitle", "Send Cryptocurrency — Zero-Latency Transfer");
            return "transactions/send";
        }

        try {
            Transaction tx = transactionService.sendTransaction(userId, sendDto);
            redirectAttributes.addFlashAttribute("successMessage",
                    "Transaction successfully broadcasted! Hash: " + tx.getTxHash());
            return "redirect:/transactions/" + tx.getId() + "/receipt";
        } catch (IllegalArgumentException | IllegalStateException | SecurityException ex) {
            model.addAttribute("errorMessage", ex.getMessage());
            model.addAttribute("wallets", wallets);
            model.addAttribute("unreadNotificationsCount", notificationService.getUnreadCount(userId));
            model.addAttribute("pageTitle", "Send Cryptocurrency — Zero-Latency Transfer");
            return "transactions/send";
        } catch (Exception ex) {
            log.error("Transaction failed: ", ex);
            model.addAttribute("errorMessage", "Transfer failed due to a system error. Please try again.");
            model.addAttribute("wallets", wallets);
            model.addAttribute("unreadNotificationsCount", notificationService.getUnreadCount(userId));
            model.addAttribute("pageTitle", "Send Cryptocurrency — Zero-Latency Transfer");
            return "transactions/send";
        }
    }

    @GetMapping("/receive")
    public String receivePage(@RequestParam(value = "walletId", required = false) Long walletId,
                              @AuthenticationPrincipal CustomUserDetails userDetails,
                              Model model) {
        Long userId = userDetails.getId();
        List<Wallet> wallets = walletService.getUserWallets(userId);

        Wallet selectedWallet = null;
        if (walletId != null) {
            selectedWallet = wallets.stream().filter(w -> w.getId().equals(walletId)).findFirst().orElse(null);
        }
        if (selectedWallet == null && !wallets.isEmpty()) {
            selectedWallet = wallets.get(0);
        }

        model.addAttribute("wallets", wallets);
        model.addAttribute("selectedWallet", selectedWallet);
        model.addAttribute("unreadNotificationsCount", notificationService.getUnreadCount(userId));
        model.addAttribute("pageTitle", "Receive Cryptocurrency — QR & Public Address");

        return "transactions/receive";
    }

    @GetMapping("/{id}/receipt")
    public String viewReceipt(@PathVariable("id") Long txId,
                              @AuthenticationPrincipal CustomUserDetails userDetails,
                              Model model) {
        Long userId = userDetails.getId();
        Transaction tx = transactionService.getTransactionById(txId)
                .orElseThrow(() -> new IllegalArgumentException("Transaction record not found"));

        // Verify transaction belongs to this user or admin
        boolean isParticipant = (tx.getSenderWallet() != null && tx.getSenderWallet().getUser().getId().equals(userId)) ||
                                (tx.getReceiverWallet() != null && tx.getReceiverWallet().getUser().getId().equals(userId)) ||
                                userDetails.getUser().getRole().name().equals("ROLE_ADMIN");

        if (!isParticipant) {
            return "redirect:/transactions?error=unauthorized";
        }

        model.addAttribute("tx", tx);
        model.addAttribute("unreadNotificationsCount", notificationService.getUnreadCount(userId));
        model.addAttribute("pageTitle", "Transaction Receipt #" + tx.getTxHash().substring(0, 10) + "... — CryptoVault");

        return "transactions/receipt";
    }
}
