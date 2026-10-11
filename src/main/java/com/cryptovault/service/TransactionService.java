package com.cryptovault.service;

import com.cryptovault.dto.SendTransactionDto;
import com.cryptovault.entity.Transaction;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface TransactionService {
    Transaction sendTransaction(Long userId, SendTransactionDto dto);
    List<Transaction> getUserTransactions(Long userId);
    List<Transaction> getRecentTransactions(Long userId, int limit);
    Optional<Transaction> getTransactionByHash(String txHash);
    Optional<Transaction> getTransactionById(Long id);
    List<Transaction> getAllTransactions();
    BigDecimal getTotalVolumeInr();
    long countConfirmedTransactions();
}
