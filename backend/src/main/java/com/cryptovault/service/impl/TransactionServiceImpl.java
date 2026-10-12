package com.cryptovault.service.impl;

import com.cryptovault.dto.SendTransactionDto;
import com.cryptovault.entity.*;
import com.cryptovault.repository.NotificationRepository;
import com.cryptovault.repository.TransactionRepository;
import com.cryptovault.repository.UserRepository;
import com.cryptovault.repository.WalletRepository;
import com.cryptovault.service.TransactionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.security.SecureRandom;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class TransactionServiceImpl implements TransactionService {

    private static final Logger log = LoggerFactory.getLogger(TransactionServiceImpl.class);

    private final TransactionRepository transactionRepository;
    private final WalletRepository walletRepository;
    private final UserRepository userRepository;
    private final NotificationRepository notificationRepository;
    private final SecureRandom secureRandom = new SecureRandom();

    public TransactionServiceImpl(TransactionRepository transactionRepository,
                                  WalletRepository walletRepository,
                                  UserRepository userRepository,
                                  NotificationRepository notificationRepository) {
        this.transactionRepository = transactionRepository;
        this.walletRepository = walletRepository;
        this.userRepository = userRepository;
        this.notificationRepository = notificationRepository;
    }

    @Override
    @Transactional
    public Transaction sendTransaction(Long userId, SendTransactionDto dto) {
        User senderUser = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Wallet senderWallet = walletRepository.findById(dto.getSenderWalletId())
                .orElseThrow(() -> new IllegalArgumentException("Source wallet not found"));

        if (!senderWallet.getUser().getId().equals(senderUser.getId())) {
            throw new SecurityException("Unauthorized source wallet access");
        }

        if (!senderWallet.isActive()) {
            throw new IllegalStateException("Source wallet is currently inactive or frozen");
        }

        Currency currency = senderWallet.getCurrency();
        BigDecimal sendAmount = dto.getAmount().setScale(8, RoundingMode.HALF_UP);

        if (sendAmount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Transfer amount must be strictly greater than 0");
        }

        // Calculate dynamic network fee based on asset
        BigDecimal networkFee = calculateNetworkFee(currency.getCode());
        BigDecimal totalDeduction = sendAmount.add(networkFee);

        if (senderWallet.getBalance().compareTo(totalDeduction) < 0) {
            throw new IllegalArgumentException(
                    String.format("Insufficient funds. Required: %s %s (Amount: %s + Fee: %s), Available: %s %s",
                            totalDeduction.stripTrailingZeros().toPlainString(),
                            currency.getCode(),
                            sendAmount.stripTrailingZeros().toPlainString(),
                            networkFee.stripTrailingZeros().toPlainString(),
                            senderWallet.getBalance().stripTrailingZeros().toPlainString(),
                            currency.getCode()
                    )
            );
        }

        String recipientAddress = dto.getReceiverAddress().trim();
        if (senderWallet.getWalletAddress().equalsIgnoreCase(recipientAddress)) {
            throw new IllegalArgumentException("Cannot transfer crypto to the same wallet address");
        }

        // Debit sender wallet
        senderWallet.setBalance(senderWallet.getBalance().subtract(totalDeduction));
        walletRepository.save(senderWallet);

        // Check if destination wallet is registered on internal CryptoVault network
        Optional<Wallet> optionalReceiverWallet = walletRepository.findByWalletAddress(recipientAddress);
        Wallet receiverWallet = null;

        BigDecimal inrEquiv = sendAmount.multiply(currency.getExchangeRateInr()).setScale(2, RoundingMode.HALF_UP);
        BigDecimal usdEquiv = sendAmount.multiply(currency.getExchangeRateUsd()).setScale(2, RoundingMode.HALF_UP);

        String txHash = "0x" + UUID.randomUUID().toString().replace("-", "") + UUID.randomUUID().toString().substring(0, 8);
        long blockNum = 18493000L + (long)(secureRandom.nextInt(5000));

        if (optionalReceiverWallet.isPresent()) {
            receiverWallet = optionalReceiverWallet.get();
            // Verify currency match
            if (!receiverWallet.getCurrency().getId().equals(currency.getId())) {
                throw new IllegalArgumentException("Destination wallet address belongs to a different currency network (" + receiverWallet.getCurrency().getCode() + ")");
            }

            // Credit receiver wallet
            receiverWallet.setBalance(receiverWallet.getBalance().add(sendAmount));
            walletRepository.save(receiverWallet);

            // Notify Receiver
            Notification receiverNotice = Notification.builder()
                    .user(receiverWallet.getUser())
                    .title("Incoming Crypto Received: +" + sendAmount + " " + currency.getCode())
                    .message(String.format("You received %s %s (₹%s / $%s) from %s.",
                            sendAmount, currency.getCode(), inrEquiv, usdEquiv, senderWallet.getWalletAddress().substring(0, 10) + "..."))
                    .type(NotificationType.TX_RECEIVED)
                    .referenceUrl("/transactions")
                    .build();
            notificationRepository.save(receiverNotice);
        }

        // Save Transaction record
        Transaction tx = Transaction.builder()
                .txHash(txHash)
                .senderWallet(senderWallet)
                .receiverWallet(receiverWallet)
                .currency(currency)
                .amount(sendAmount)
                .fee(networkFee)
                .fiatEquivalentInr(inrEquiv)
                .fiatEquivalentUsd(usdEquiv)
                .status(TransactionStatus.CONFIRMED)
                .txType(TransactionType.TRANSFER)
                .note(dto.getNote() != null && !dto.getNote().isEmpty() ? dto.getNote() : "P2P Cryptographic Vault Transfer")
                .blockNumber(blockNum)
                .confirmations(18)
                .build();

        Transaction savedTx = transactionRepository.save(tx);

        // Notify Sender
        Notification senderNotice = Notification.builder()
                .user(senderUser)
                .title("Transaction Confirmed: -" + sendAmount + " " + currency.getCode())
                .message(String.format("Transferred %s %s (Fee: %s %s) to %s. Tx Hash: %s",
                        sendAmount, currency.getCode(), networkFee, currency.getCode(),
                        recipientAddress.substring(0, Math.min(12, recipientAddress.length())) + "...",
                        txHash.substring(0, 16) + "..."))
                .type(NotificationType.TX_SUCCESS)
                .referenceUrl("/transactions")
                .build();
        notificationRepository.save(senderNotice);

        log.info("Processed transfer of {} {} from {} to {}. Tx: {}", sendAmount, currency.getCode(), senderWallet.getWalletAddress(), recipientAddress, txHash);
        return savedTx;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Transaction> getUserTransactions(Long userId) {
        return transactionRepository.findAllByUserId(userId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Transaction> getRecentTransactions(Long userId, int limit) {
        return transactionRepository.findRecentByUserId(userId, PageRequest.of(0, limit));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Transaction> getTransactionByHash(String txHash) {
        return transactionRepository.findByTxHash(txHash);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Transaction> getTransactionById(Long id) {
        return transactionRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Transaction> getAllTransactions() {
        return transactionRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public BigDecimal getTotalVolumeInr() {
        return transactionRepository.sumTotalVolumeInr();
    }

    @Override
    @Transactional(readOnly = true)
    public long countConfirmedTransactions() {
        return transactionRepository.countConfirmedTransactions();
    }

    private BigDecimal calculateNetworkFee(String code) {
        if ("BTC".equalsIgnoreCase(code)) {
            return new BigDecimal("0.00005000");
        } else if ("ETH".equalsIgnoreCase(code)) {
            return new BigDecimal("0.00080000");
        } else if ("SOL".equalsIgnoreCase(code)) {
            return new BigDecimal("0.00002000");
        } else if ("USDT".equalsIgnoreCase(code)) {
            return new BigDecimal("0.50000000");
        } else if ("XRP".equalsIgnoreCase(code)) {
            return new BigDecimal("0.01000000");
        } else {
            return new BigDecimal("0.00010000");
        }
    }
}
