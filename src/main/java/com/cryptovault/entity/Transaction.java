package com.cryptovault.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "transactions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "tx_hash", nullable = false, unique = true, length = 66)
    private String txHash; // e.g. 0x4f2a7b8c...

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_wallet_id")
    private Wallet senderWallet;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receiver_wallet_id")
    private Wallet receiverWallet;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "currency_id", nullable = false)
    private Currency currency;

    @Column(nullable = false, precision = 24, scale = 8)
    private BigDecimal amount;

    @Column(nullable = false, precision = 24, scale = 8)
    @Builder.Default
    private BigDecimal fee = BigDecimal.ZERO;

    @Column(name = "fiat_equivalent_inr", precision = 18, scale = 2)
    private BigDecimal fiatEquivalentInr;

    @Column(name = "fiat_equivalent_usd", precision = 18, scale = 2)
    private BigDecimal fiatEquivalentUsd;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    @Builder.Default
    private TransactionStatus status = TransactionStatus.CONFIRMED;

    @Enumerated(EnumType.STRING)
    @Column(name = "tx_type", nullable = false, length = 20)
    private TransactionType txType;

    @Column(length = 255)
    private String note;

    @Column(name = "block_number")
    @Builder.Default
    private Long blockNumber = 18492042L;

    @Column(name = "confirmations")
    @Builder.Default
    private Integer confirmations = 12;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
}
