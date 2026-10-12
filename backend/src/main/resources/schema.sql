-- ====================================================================
-- CryptoVault — Relational Database Schema (MySQL 8.0+)
-- Author: Adabala Pushkaran • 24EG105R65
-- ====================================================================

CREATE DATABASE IF NOT EXISTS cryptovault;
USE cryptovault;

-- 1. Currencies Table
CREATE TABLE IF NOT EXISTS currencies (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    code VARCHAR(10) NOT NULL UNIQUE,
    name VARCHAR(50) NOT NULL,
    symbol VARCHAR(10) NOT NULL,
    exchange_rate_inr DECIMAL(18,2) NOT NULL,
    exchange_rate_usd DECIMAL(18,4) NOT NULL,
    change_24h DECIMAL(6,2) DEFAULT 0.00,
    market_cap_usd DECIMAL(20,2),
    volume_24h_usd DECIMAL(20,2),
    icon_class VARCHAR(50),
    badge_color VARCHAR(30),
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- 2. Users Table
CREATE TABLE IF NOT EXISTS users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    full_name VARCHAR(100) NOT NULL,
    phone_number VARCHAR(20),
    role VARCHAR(20) NOT NULL DEFAULT 'ROLE_USER',
    enabled BOOLEAN NOT NULL DEFAULT TRUE,
    account_non_locked BOOLEAN NOT NULL DEFAULT TRUE,
    last_login TIMESTAMP NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- 3. Wallets Table
CREATE TABLE IF NOT EXISTS wallets (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    currency_id BIGINT NOT NULL,
    wallet_address VARCHAR(64) NOT NULL UNIQUE,
    wallet_name VARCHAR(100),
    balance DECIMAL(24,8) NOT NULL DEFAULT 0.00000000,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (currency_id) REFERENCES currencies(id)
);

-- 4. Transactions Table
CREATE TABLE IF NOT EXISTS transactions (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    tx_hash VARCHAR(66) NOT NULL UNIQUE,
    sender_wallet_id BIGINT NULL,
    receiver_wallet_id BIGINT NULL,
    currency_id BIGINT NOT NULL,
    amount DECIMAL(24,8) NOT NULL,
    fee DECIMAL(24,8) NOT NULL DEFAULT 0.00000000,
    fiat_equivalent_inr DECIMAL(18,2),
    fiat_equivalent_usd DECIMAL(18,2),
    status VARCHAR(20) NOT NULL DEFAULT 'CONFIRMED',
    tx_type VARCHAR(20) NOT NULL,
    note VARCHAR(255),
    block_number BIGINT DEFAULT 18492042,
    confirmations INT DEFAULT 12,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (sender_wallet_id) REFERENCES wallets(id) ON DELETE SET NULL,
    FOREIGN KEY (receiver_wallet_id) REFERENCES wallets(id) ON DELETE SET NULL,
    FOREIGN KEY (currency_id) REFERENCES currencies(id)
);

-- 5. Auth Sessions Table
CREATE TABLE IF NOT EXISTS auth_sessions (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    session_token VARCHAR(100),
    ip_address VARCHAR(50),
    user_agent VARCHAR(255),
    device_type VARCHAR(50),
    location VARCHAR(100),
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    login_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    last_activity TIMESTAMP NULL,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- 6. Notifications Table
CREATE TABLE IF NOT EXISTS notifications (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    title VARCHAR(150) NOT NULL,
    message TEXT NOT NULL,
    type VARCHAR(30) NOT NULL DEFAULT 'SYSTEM',
    is_read BOOLEAN NOT NULL DEFAULT FALSE,
    reference_url VARCHAR(200),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);
