package com.cryptovault.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "transactions")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "tx_hash", nullable = false, unique = true, length = 66)
    private String txHash;

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
    private BigDecimal fee = BigDecimal.ZERO;

    @Column(name = "fiat_equivalent_inr", precision = 18, scale = 2)
    private BigDecimal fiatEquivalentInr;

    @Column(name = "fiat_equivalent_usd", precision = 18, scale = 2)
    private BigDecimal fiatEquivalentUsd;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private TransactionStatus status = TransactionStatus.CONFIRMED;

    @Enumerated(EnumType.STRING)
    @Column(name = "tx_type", nullable = false, length = 20)
    private TransactionType txType;

    @Column(length = 255)
    private String note;

    @Column(name = "block_number")
    private Long blockNumber = 18492042L;

    @Column(name = "confirmations")
    private Integer confirmations = 12;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    public Transaction() {}

    public Transaction(Long id, String txHash, Wallet senderWallet, Wallet receiverWallet, Currency currency, BigDecimal amount, BigDecimal fee, BigDecimal fiatEquivalentInr, BigDecimal fiatEquivalentUsd, TransactionStatus status, TransactionType txType, String note, Long blockNumber, Integer confirmations, LocalDateTime createdAt) {
        this.id = id;
        this.txHash = txHash;
        this.senderWallet = senderWallet;
        this.receiverWallet = receiverWallet;
        this.currency = currency;
        this.amount = amount;
        this.fee = fee != null ? fee : BigDecimal.ZERO;
        this.fiatEquivalentInr = fiatEquivalentInr;
        this.fiatEquivalentUsd = fiatEquivalentUsd;
        this.status = status != null ? status : TransactionStatus.CONFIRMED;
        this.txType = txType;
        this.note = note;
        this.blockNumber = blockNumber != null ? blockNumber : 18492042L;
        this.confirmations = confirmations != null ? confirmations : 12;
        this.createdAt = createdAt;
    }

    public static TransactionBuilder builder() {
        return new TransactionBuilder();
    }

    public static class TransactionBuilder {
        private Long id;
        private String txHash;
        private Wallet senderWallet;
        private Wallet receiverWallet;
        private Currency currency;
        private BigDecimal amount;
        private BigDecimal fee = BigDecimal.ZERO;
        private BigDecimal fiatEquivalentInr;
        private BigDecimal fiatEquivalentUsd;
        private TransactionStatus status = TransactionStatus.CONFIRMED;
        private TransactionType txType;
        private String note;
        private Long blockNumber = 18492042L;
        private Integer confirmations = 12;
        private LocalDateTime createdAt;

        public TransactionBuilder id(Long id) { this.id = id; return this; }
        public TransactionBuilder txHash(String txHash) { this.txHash = txHash; return this; }
        public TransactionBuilder senderWallet(Wallet senderWallet) { this.senderWallet = senderWallet; return this; }
        public TransactionBuilder receiverWallet(Wallet receiverWallet) { this.receiverWallet = receiverWallet; return this; }
        public TransactionBuilder currency(Currency currency) { this.currency = currency; return this; }
        public TransactionBuilder amount(BigDecimal amount) { this.amount = amount; return this; }
        public TransactionBuilder fee(BigDecimal fee) { this.fee = fee; return this; }
        public TransactionBuilder fiatEquivalentInr(BigDecimal fiatEquivalentInr) { this.fiatEquivalentInr = fiatEquivalentInr; return this; }
        public TransactionBuilder fiatEquivalentUsd(BigDecimal fiatEquivalentUsd) { this.fiatEquivalentUsd = fiatEquivalentUsd; return this; }
        public TransactionBuilder status(TransactionStatus status) { this.status = status; return this; }
        public TransactionBuilder txType(TransactionType txType) { this.txType = txType; return this; }
        public TransactionBuilder note(String note) { this.note = note; return this; }
        public TransactionBuilder blockNumber(Long blockNumber) { this.blockNumber = blockNumber; return this; }
        public TransactionBuilder confirmations(Integer confirmations) { this.confirmations = confirmations; return this; }
        public TransactionBuilder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }

        public Transaction build() {
            return new Transaction(id, txHash, senderWallet, receiverWallet, currency, amount, fee, fiatEquivalentInr, fiatEquivalentUsd, status, txType, note, blockNumber, confirmations, createdAt);
        }
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTxHash() { return txHash; }
    public void setTxHash(String txHash) { this.txHash = txHash; }
    public Wallet getSenderWallet() { return senderWallet; }
    public void setSenderWallet(Wallet senderWallet) { this.senderWallet = senderWallet; }
    public Wallet getReceiverWallet() { return receiverWallet; }
    public void setReceiverWallet(Wallet receiverWallet) { this.receiverWallet = receiverWallet; }
    public Currency getCurrency() { return currency; }
    public void setCurrency(Currency currency) { this.currency = currency; }
    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
    public BigDecimal getFee() { return fee; }
    public void setFee(BigDecimal fee) { this.fee = fee; }
    public BigDecimal getFiatEquivalentInr() { return fiatEquivalentInr; }
    public void setFiatEquivalentInr(BigDecimal fiatEquivalentInr) { this.fiatEquivalentInr = fiatEquivalentInr; }
    public BigDecimal getFiatEquivalentUsd() { return fiatEquivalentUsd; }
    public void setFiatEquivalentUsd(BigDecimal fiatEquivalentUsd) { this.fiatEquivalentUsd = fiatEquivalentUsd; }
    public TransactionStatus getStatus() { return status; }
    public void setStatus(TransactionStatus status) { this.status = status; }
    public TransactionType getTxType() { return txType; }
    public void setTxType(TransactionType txType) { this.txType = txType; }
    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }
    public Long getBlockNumber() { return blockNumber; }
    public void setBlockNumber(Long blockNumber) { this.blockNumber = blockNumber; }
    public Integer getConfirmations() { return confirmations; }
    public void setConfirmations(Integer confirmations) { this.confirmations = confirmations; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
