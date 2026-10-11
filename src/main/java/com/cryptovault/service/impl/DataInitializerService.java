package com.cryptovault.service.impl;

import com.cryptovault.entity.*;
import com.cryptovault.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class DataInitializerService implements CommandLineRunner {

    private final CurrencyRepository currencyRepository;
    private final UserRepository userRepository;
    private final WalletRepository walletRepository;
    private final TransactionRepository transactionRepository;
    private final NotificationRepository notificationRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void run(String... args) {
        log.info("Initializing CryptoVault default system data...");

        initCurrencies();
        initUsersAndWallets();

        log.info("CryptoVault initialization completed successfully!");
    }

    private void initCurrencies() {
        if (currencyRepository.count() == 0) {
            List<Currency> currencies = Arrays.asList(
                Currency.builder()
                    .code("BTC")
                    .name("Bitcoin")
                    .symbol("₿")
                    .exchangeRateInr(new BigDecimal("6550000.00"))
                    .exchangeRateUsd(new BigDecimal("78500.0000"))
                    .change24h(new BigDecimal("2.45"))
                    .marketCapUsd(new BigDecimal("1540000000000.00"))
                    .volume24hUsd(new BigDecimal("34500000000.00"))
                    .iconClass("fab fa-bitcoin")
                    .badgeColor("#F7931A")
                    .active(true)
                    .build(),
                Currency.builder()
                    .code("ETH")
                    .name("Ethereum")
                    .symbol("Ξ")
                    .exchangeRateInr(new BigDecimal("285000.00"))
                    .exchangeRateUsd(new BigDecimal("3420.0000"))
                    .change24h(new BigDecimal("4.12"))
                    .marketCapUsd(new BigDecimal("410000000000.00"))
                    .volume24hUsd(new BigDecimal("18200000000.00"))
                    .iconClass("fab fa-ethereum")
                    .badgeColor("#627EEA")
                    .active(true)
                    .build(),
                Currency.builder()
                    .code("SOL")
                    .name("Solana")
                    .symbol("◎")
                    .exchangeRateInr(new BigDecimal("14200.00"))
                    .exchangeRateUsd(new BigDecimal("170.5000"))
                    .change24h(new BigDecimal("6.85"))
                    .marketCapUsd(new BigDecimal("82000000000.00"))
                    .volume24hUsd(new BigDecimal("5600000000.00"))
                    .iconClass("fas fa-sun")
                    .badgeColor("#14F195")
                    .active(true)
                    .build(),
                Currency.builder()
                    .code("USDT")
                    .name("Tether USD")
                    .symbol("₮")
                    .exchangeRateInr(new BigDecimal("83.50"))
                    .exchangeRateUsd(new BigDecimal("1.0000"))
                    .change24h(new BigDecimal("0.02"))
                    .marketCapUsd(new BigDecimal("115000000000.00"))
                    .volume24hUsd(new BigDecimal("45000000000.00"))
                    .iconClass("fas fa-dollar-sign")
                    .badgeColor("#26A17B")
                    .active(true)
                    .build(),
                Currency.builder()
                    .code("XRP")
                    .name("XRP Ripple")
                    .symbol("✕")
                    .exchangeRateInr(new BigDecimal("52.40"))
                    .exchangeRateUsd(new BigDecimal("0.6280"))
                    .change24h(new BigDecimal("-1.15"))
                    .marketCapUsd(new BigDecimal("35000000000.00"))
                    .volume24hUsd(new BigDecimal("2100000000.00"))
                    .iconClass("fas fa-times")
                    .badgeColor("#23292F")
                    .active(true)
                    .build(),
                Currency.builder()
                    .code("ADA")
                    .name("Cardano")
                    .symbol("₳")
                    .exchangeRateInr(new BigDecimal("41.20"))
                    .exchangeRateUsd(new BigDecimal("0.4940"))
                    .change24h(new BigDecimal("1.80"))
                    .marketCapUsd(new BigDecimal("17500000000.00"))
                    .volume24hUsd(new BigDecimal("850000000.00"))
                    .iconClass("fas fa-circle-notch")
                    .badgeColor("#0033AD")
                    .active(true)
                    .build()
            );
            currencyRepository.saveAll(currencies);
            log.info("Initialized {} core cryptocurrencies.", currencies.size());
        }
    }

    private void initUsersAndWallets() {
        if (!userRepository.existsByUsername("admin")) {
            User admin = User.builder()
                    .username("admin")
                    .email("admin@cryptovault.io")
                    .fullName("CryptoVault Administrator")
                    .password(passwordEncoder.encode("AdminSecret123!"))
                    .role(Role.ROLE_ADMIN)
                    .enabled(true)
                    .accountNonLocked(true)
                    .phoneNumber("+91 9876543210")
                    .lastLogin(LocalDateTime.now())
                    .build();
            userRepository.save(admin);
            log.info("Created Default Admin account: admin / AdminSecret123!");
        }

        if (!userRepository.existsByUsername("pushkaran")) {
            User pushkaran = User.builder()
                    .username("pushkaran")
                    .email("pushkaran@cryptovault.io")
                    .fullName("Adabala Pushkaran (24EG105R65)")
                    .password(passwordEncoder.encode("Password123!"))
                    .role(Role.ROLE_USER)
                    .enabled(true)
                    .accountNonLocked(true)
                    .phoneNumber("+91 9490123456")
                    .lastLogin(LocalDateTime.now())
                    .build();
            User savedPushkaran = userRepository.save(pushkaran);

            // Fetch currencies
            Currency btc = currencyRepository.findByCode("BTC").orElse(null);
            Currency eth = currencyRepository.findByCode("ETH").orElse(null);
            Currency sol = currencyRepository.findByCode("SOL").orElse(null);
            Currency usdt = currencyRepository.findByCode("USDT").orElse(null);

            // Create Pushkaran Wallets
            Wallet btcWallet = Wallet.builder()
                    .user(savedPushkaran)
                    .currency(btc)
                    .walletAddress("bc1q9x4a2e8c7f5d0b1a3e6f9d8c7b4a2e0f1d3b5a")
                    .walletName("Pushkaran Primary BTC Vault")
                    .balance(new BigDecimal("1.45000000"))
                    .active(true)
                    .build();

            Wallet ethWallet = Wallet.builder()
                    .user(savedPushkaran)
                    .currency(eth)
                    .walletAddress("0x71C2d3E4F5a6B7c8D9E0F1a2B3c4D5e6F7A8B9C0")
                    .walletName("Pushkaran Staking & DeFi ETH")
                    .balance(new BigDecimal("8.25000000"))
                    .active(true)
                    .build();

            Wallet solWallet = Wallet.builder()
                    .user(savedPushkaran)
                    .currency(sol)
                    .walletAddress("sol9kL3pQ8vM1zX5wR7tY2uI4oP6aS8dF0gH2jK4lM")
                    .walletName("Pushkaran High-Speed SOL")
                    .balance(new BigDecimal("45.00000000"))
                    .active(true)
                    .build();

            Wallet usdtWallet = Wallet.builder()
                    .user(savedPushkaran)
                    .currency(usdt)
                    .walletAddress("0x3B8e7F2a9D1c0E4B5A6f7C8D9E0f1A2B3C4D5E6F")
                    .walletName("Pushkaran Liquid USDT Reserves")
                    .balance(new BigDecimal("15000.00000000"))
                    .active(true)
                    .build();

            walletRepository.saveAll(Arrays.asList(btcWallet, ethWallet, solWallet, usdtWallet));

            // Create a Peer User "alice"
            User alice = User.builder()
                    .username("alice")
                    .email("alice@cryptovault.io")
                    .fullName("Alice Vance")
                    .password(passwordEncoder.encode("Password123!"))
                    .role(Role.ROLE_USER)
                    .enabled(true)
                    .accountNonLocked(true)
                    .phoneNumber("+91 9123456780")
                    .lastLogin(LocalDateTime.now().minusHours(2))
                    .build();
            User savedAlice = userRepository.save(alice);

            Wallet aliceBtc = Wallet.builder()
                    .user(savedAlice)
                    .currency(btc)
                    .walletAddress("bc1q8f7e6d5c4b3a2a1f0e9d8c7b6a5f4e3d2c1b0a")
                    .walletName("Alice Sovereign BTC")
                    .balance(new BigDecimal("0.50000000"))
                    .active(true)
                    .build();

            Wallet aliceEth = Wallet.builder()
                    .user(savedAlice)
                    .currency(eth)
                    .walletAddress("0x9F8E7D6C5B4A3A2F1E0D9C8B7A6F5E4D3C2B1A0F")
                    .walletName("Alice Smart Contract ETH")
                    .balance(new BigDecimal("2.10000000"))
                    .active(true)
                    .build();

            walletRepository.saveAll(Arrays.asList(aliceBtc, aliceEth));

            // Seed Historical Transactions
            Transaction tx1 = Transaction.builder()
                    .txHash("0x8f2d9c1e4b5a6f7c8d9e0f1a2b3c4d5e6f7a8b9c0d1e2f3a4b5c6d7e8f9a0b1c")
                    .receiverWallet(btcWallet)
                    .currency(btc)
                    .amount(new BigDecimal("1.50000000"))
                    .fee(BigDecimal.ZERO)
                    .fiatEquivalentInr(new BigDecimal("9825000.00"))
                    .fiatEquivalentUsd(new BigDecimal("117750.00"))
                    .status(TransactionStatus.CONFIRMED)
                    .txType(TransactionType.DEPOSIT)
                    .note("Initial Bank Gateway Fiat Inflow")
                    .blockNumber(18491204L)
                    .confirmations(128)
                    .createdAt(LocalDateTime.now().minusDays(5))
                    .build();

            Transaction tx2 = Transaction.builder()
                    .txHash("0x1a2b3c4d5e6f7a8b9c0d1e2f3a4b5c6d7e8f9a0b1c2d3e4f5a6b7c8d9e0f1a2b")
                    .senderWallet(btcWallet)
                    .receiverWallet(aliceBtc)
                    .currency(btc)
                    .amount(new BigDecimal("0.05000000"))
                    .fee(new BigDecimal("0.00005000"))
                    .fiatEquivalentInr(new BigDecimal("327500.00"))
                    .fiatEquivalentUsd(new BigDecimal("3925.00"))
                    .status(TransactionStatus.CONFIRMED)
                    .txType(TransactionType.TRANSFER)
                    .note("Payment for Cloud GPU Nodes")
                    .blockNumber(18491850L)
                    .confirmations(84)
                    .createdAt(LocalDateTime.now().minusDays(2))
                    .build();

            Transaction tx3 = Transaction.builder()
                    .txHash("0x4a5b6c7d8e9f0a1b2c3d4e5f6a7b8c9d0e1f2a3b4c5d6e7f8a9b0c1d2e3f4a5b")
                    .receiverWallet(ethWallet)
                    .currency(eth)
                    .amount(new BigDecimal("8.25000000"))
                    .fee(BigDecimal.ZERO)
                    .fiatEquivalentInr(new BigDecimal("2351250.00"))
                    .fiatEquivalentUsd(new BigDecimal("28215.00"))
                    .status(TransactionStatus.CONFIRMED)
                    .txType(TransactionType.DEPOSIT)
                    .note("Mining Pool Reward Settlement")
                    .blockNumber(18492100L)
                    .confirmations(60)
                    .createdAt(LocalDateTime.now().minusDays(1))
                    .build();

            Transaction tx4 = Transaction.builder()
                    .txHash("0x7c8d9e0f1a2b3c4d5e6f7a8b9c0d1e2f3a4b5c6d7e8f9a0b1c2d3e4f5a6b7c8d")
                    .receiverWallet(solWallet)
                    .currency(sol)
                    .amount(new BigDecimal("45.00000000"))
                    .fee(BigDecimal.ZERO)
                    .fiatEquivalentInr(new BigDecimal("639000.00"))
                    .fiatEquivalentUsd(new BigDecimal("7672.50"))
                    .status(TransactionStatus.CONFIRMED)
                    .txType(TransactionType.FAUCET)
                    .note("Solana Ecosystem Developer Grant")
                    .blockNumber(18492800L)
                    .confirmations(42)
                    .createdAt(LocalDateTime.now().minusHours(8))
                    .build();

            transactionRepository.saveAll(Arrays.asList(tx1, tx2, tx3, tx4));

            // Seed Notifications for Pushkaran
            Notification n1 = Notification.builder()
                    .user(savedPushkaran)
                    .title("Welcome to CryptoVault Pro")
                    .message("Welcome Adabala Pushkaran! Your multi-asset sovereign vault is ready with institutional grade security.")
                    .type(NotificationType.SYSTEM)
                    .read(false)
                    .referenceUrl("/wallets")
                    .createdAt(LocalDateTime.now().minusDays(5))
                    .build();

            Notification n2 = Notification.builder()
                    .user(savedPushkaran)
                    .title("Transfer Confirmed: -0.05000000 BTC")
                    .message("Your transfer of 0.05 BTC to Alice Vance (" + aliceBtc.getWalletAddress().substring(0, 10) + "...) has been confirmed with 84 blocks.")
                    .type(NotificationType.TX_SUCCESS)
                    .read(true)
                    .referenceUrl("/transactions")
                    .createdAt(LocalDateTime.now().minusDays(2))
                    .build();

            Notification n3 = Notification.builder()
                    .user(savedPushkaran)
                    .title("Deposit Confirmed: +8.25000000 ETH")
                    .message("Mining pool deposit of 8.25 ETH (₹23,51,250) successfully credited.")
                    .type(NotificationType.DEPOSIT_CONFIRMED)
                    .read(false)
                    .referenceUrl("/transactions")
                    .createdAt(LocalDateTime.now().minusDays(1))
                    .build();

            notificationRepository.saveAll(Arrays.asList(n1, n2, n3));

            log.info("Initialized Demo User: pushkaran / Password123! with 4 funded vaults and history.");
        }
    }
}
