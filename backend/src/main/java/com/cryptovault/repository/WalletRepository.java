package com.cryptovault.repository;

import com.cryptovault.entity.Currency;
import com.cryptovault.entity.User;
import com.cryptovault.entity.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WalletRepository extends JpaRepository<Wallet, Long> {
    List<Wallet> findByUserAndActiveTrue(User user);
    List<Wallet> findByUser(User user);
    Optional<Wallet> findByWalletAddress(String walletAddress);
    Optional<Wallet> findByUserAndCurrency(User user, Currency currency);
    boolean existsByWalletAddress(String walletAddress);

    @Query("SELECT w FROM Wallet w JOIN FETCH w.currency WHERE w.user = :user AND w.active = true")
    List<Wallet> findByUserWithCurrency(@Param("user") User user);

    @Query("SELECT COUNT(w) FROM Wallet w WHERE w.active = true")
    long countActiveWallets();
}
