package com.cryptovault.repository;

import com.cryptovault.entity.Transaction;
import com.cryptovault.entity.Wallet;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    Optional<Transaction> findByTxHash(String txHash);

    @Query("SELECT t FROM Transaction t " +
           "LEFT JOIN FETCH t.senderWallet sw " +
           "LEFT JOIN FETCH t.receiverWallet rw " +
           "JOIN FETCH t.currency c " +
           "WHERE (sw.user.id = :userId OR rw.user.id = :userId) " +
           "ORDER BY t.createdAt DESC")
    List<Transaction> findRecentByUserId(@Param("userId") Long userId, Pageable pageable);

    @Query("SELECT t FROM Transaction t " +
           "LEFT JOIN FETCH t.senderWallet sw " +
           "LEFT JOIN FETCH t.receiverWallet rw " +
           "JOIN FETCH t.currency c " +
           "WHERE (sw.user.id = :userId OR rw.user.id = :userId) " +
           "ORDER BY t.createdAt DESC")
    List<Transaction> findAllByUserId(@Param("userId") Long userId);

    @Query("SELECT t FROM Transaction t " +
           "LEFT JOIN FETCH t.senderWallet sw " +
           "LEFT JOIN FETCH t.receiverWallet rw " +
           "JOIN FETCH t.currency c " +
           "WHERE sw = :wallet OR rw = :wallet " +
           "ORDER BY t.createdAt DESC")
    List<Transaction> findByWallet(@Param("wallet") Wallet wallet);

    @Query("SELECT COALESCE(SUM(t.fiatEquivalentInr), 0) FROM Transaction t WHERE t.status = 'CONFIRMED'")
    BigDecimal sumTotalVolumeInr();

    @Query("SELECT COUNT(t) FROM Transaction t WHERE t.status = 'CONFIRMED'")
    long countConfirmedTransactions();

    List<Transaction> findTop10ByOrderByCreatedAtDesc();
}
