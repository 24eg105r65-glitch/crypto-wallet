package com.cryptovault.controller;

import com.cryptovault.dto.MarketTickerDto;
import com.cryptovault.entity.Currency;
import com.cryptovault.entity.Wallet;
import com.cryptovault.security.CustomUserDetails;
import com.cryptovault.service.CurrencyService;
import com.cryptovault.service.NotificationService;
import com.cryptovault.service.WalletService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

@RestController
@RequestMapping("/api")
public class ApiController {

    private final CurrencyService currencyService;
    private final WalletService walletService;
    private final NotificationService notificationService;

    public ApiController(CurrencyService currencyService,
                         WalletService walletService,
                         NotificationService notificationService) {
        this.currencyService = currencyService;
        this.walletService = walletService;
        this.notificationService = notificationService;
    }

    @GetMapping("/market/ticker")
    public ResponseEntity<List<MarketTickerDto>> getTicker() {
        return ResponseEntity.ok(currencyService.getMarketTickers());
    }

    @GetMapping("/rates")
    public ResponseEntity<Map<String, Object>> getRates() {
        List<Currency> currencies = currencyService.getAllActiveCurrencies();
        Map<String, Object> ratesMap = new HashMap<>();
        for (Currency c : currencies) {
            Map<String, Object> details = new HashMap<>();
            details.put("code", c.getCode());
            details.put("name", c.getName());
            details.put("symbol", c.getSymbol());
            details.put("inr", c.getExchangeRateInr());
            details.put("usd", c.getExchangeRateUsd());
            details.put("change24h", c.getChange24h());
            ratesMap.put(c.getCode(), details);
        }
        return ResponseEntity.ok(ratesMap);
    }

    @GetMapping("/wallet/validate-address")
    public ResponseEntity<Map<String, Object>> validateAddress(@RequestParam("address") String address) {
        Map<String, Object> response = new HashMap<>();
        if (address == null || address.trim().isEmpty()) {
            response.put("valid", false);
            response.put("message", "Address is required");
            return ResponseEntity.ok(response);
        }

        String clean = address.trim();
        Optional<Wallet> walletOpt = walletService.getWalletByAddress(clean);

        if (walletOpt.isPresent()) {
            Wallet w = walletOpt.get();
            response.put("valid", true);
            response.put("isInternal", true);
            response.put("currency", w.getCurrency().getCode());
            response.put("owner", w.getUser().getFullName());
            response.put("message", "Internal CryptoVault Address (" + w.getUser().getFullName() + " - " + w.getCurrency().getCode() + ")");
        } else if (clean.length() >= 26 && (clean.startsWith("0x") || clean.startsWith("bc1") || clean.startsWith("sol"))) {
            response.put("valid", true);
            response.put("isInternal", false);
            response.put("message", "Valid External Blockchain Address");
        } else {
            response.put("valid", false);
            response.put("message", "Invalid crypto address format");
        }

        return ResponseEntity.ok(response);
    }

    @GetMapping("/notifications/unread-count")
    public ResponseEntity<Map<String, Long>> getUnreadCount(@AuthenticationPrincipal CustomUserDetails userDetails) {
        Map<String, Long> res = new HashMap<>();
        if (userDetails != null) {
            res.put("unreadCount", notificationService.getUnreadCount(userDetails.getId()));
        } else {
            res.put("unreadCount", 0L);
        }
        return ResponseEntity.ok(res);
    }

    @GetMapping("/charts/history")
    public ResponseEntity<Map<String, Object>> getChartData(@RequestParam(value = "currency", defaultValue = "BTC") String currencyCode,
                                                           @RequestParam(value = "timeframe", defaultValue = "7D") String timeframe) {
        Map<String, Object> result = new HashMap<>();
        Currency currency = currencyService.getCurrencyByCode(currencyCode)
                .orElse(currencyService.getAllActiveCurrencies().get(0));

        BigDecimal currentPrice = currency.getExchangeRateInr();
        List<String> labels = new ArrayList<>();
        List<BigDecimal> dataPoints = new ArrayList<>();

        int points = 7;
        if ("24H".equalsIgnoreCase(timeframe)) points = 12;
        else if ("7D".equalsIgnoreCase(timeframe)) points = 7;
        else if ("1M".equalsIgnoreCase(timeframe)) points = 15;
        else if ("1Y".equalsIgnoreCase(timeframe)) points = 12;

        Random rand = new Random(currency.getCode().hashCode() + timeframe.hashCode());
        double basePrice = currentPrice.doubleValue();
        double runningPrice = basePrice * (1.0 - (points * 0.008));

        for (int i = 0; i < points; i++) {
            if ("24H".equalsIgnoreCase(timeframe)) {
                labels.add((i * 2) + ":00");
            } else if ("7D".equalsIgnoreCase(timeframe)) {
                labels.add("Day " + (i + 1));
            } else if ("1M".equalsIgnoreCase(timeframe)) {
                labels.add("Day " + ((i + 1) * 2));
            } else {
                labels.add("Month " + (i + 1));
            }

            double change = (rand.nextDouble() * 0.04) - 0.015;
            runningPrice = runningPrice * (1.0 + change);
            if (i == points - 1) {
                runningPrice = basePrice;
            }
            dataPoints.add(BigDecimal.valueOf(runningPrice).setScale(2, RoundingMode.HALF_UP));
        }

        result.put("currency", currency.getCode());
        result.put("symbol", currency.getSymbol());
        result.put("labels", labels);
        result.put("data", dataPoints);
        result.put("currentPrice", currentPrice);

        return ResponseEntity.ok(result);
    }
}
