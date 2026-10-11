# CryptoVault — Server-Rendered Full-Stack Cryptocurrency Wallet Platform

**Author:** Adabala Pushkaran • **Roll No:** 24EG105R65  
**Working Stack:** Maven • Spring Boot 3.3.4 • Thymeleaf • Lombok • MySQL 8.0 / H2 • Spring Security 6 • DevTools • Vanilla CSS

---

## 🚀 1. Overview & Architecture

**CryptoVault** is a full-stack, server-rendered cryptocurrency wallet platform designed for high performance, auditable security, and instant First Contentful Paint across all devices. The project strictly implements a **5-layer separation of concerns**:

```
┌─────────────────────────────────────────────────────────────┐
│ Presentation: Thymeleaf + Vanilla CSS + Minimal JS Islands   │
└──────────────────────────────┬──────────────────────────────┘
                               │
┌──────────────────────────────▼──────────────────────────────┐
│ Security & Interception: Spring Security 6 (BCrypt & CSRF)  │
└──────────────────────────────┬──────────────────────────────┘
                               │
┌──────────────────────────────▼──────────────────────────────┐
│ Controller Layer: Spring MVC & REST (@Controller, @Valid)   │
└──────────────────────────────┬──────────────────────────────┘
                               │
┌──────────────────────────────▼──────────────────────────────┐
│ Business Logic & Service Layer: @Transactional Services     │
└──────────────────────────────┬──────────────────────────────┘
                               │
┌──────────────────────────────▼──────────────────────────────┐
│ Data Access & Persistence: Spring Data JPA + MySQL / H2     │
└─────────────────────────────────────────────────────────────┘
```

---

## 🗄️ 2. Relational Database Schema (6 Core Entities)

1. **`users`**: Unique credentials, BCrypt salted hashed passwords, full names, phone numbers, role flags (`ROLE_USER`, `ROLE_ADMIN`), activity timestamps.
2. **`currencies`**: Supported assets (`BTC`, `ETH`, `SOL`, `USDT`, `XRP`, `ADA`), exchange rates in INR (₹) and USD ($), 24h market movements, market cap, and volume.
3. **`wallets`**: User-owned cryptographic wallets denominated in specific currencies, unique public receiving addresses, and atomic balances.
4. **`transactions`**: Immutable ledger records with cryptographic transaction hashes (`0x...`), sender/receiver wallet mappings, amount, network gas fee, block height, confirmations, and timestamp.
5. **`auth_sessions`**: Auditable authentication session tracker capturing IP addresses, user agents, device classifications, and revocation states.
6. **`notifications`**: In-app alert dispatch center for incoming payments, broadcast confirmations, and security alerts.

---

## 🔑 3. Pre-Seeded Accounts (Demo & Viva Ready)

| Role | Username | Password | Email | Description |
| :--- | :--- | :--- | :--- | :--- |
| **Primary User (Author)** | `pushkaran` | `Password123!` | `pushkaran@cryptovault.io` | **Adabala Pushkaran (24EG105R65)** — Pre-funded BTC, ETH, SOL, and USDT vaults |
| **Administrator** | `admin` | `AdminSecret123!` | `admin@cryptovault.io` | Full master command center access & exchange rate manager |
| **Peer User (P2P)** | `alice` | `Password123!` | `alice@cryptovault.io` | Second demo user for testing peer-to-peer cryptocurrency transfers |

---

## ⚡ 4. Quick Start & Execution

### Prerequisites
- **Java:** JDK 17, 21, or 25
- **Maven:** 3.8+ (Included or system Maven)

### Run in Zero-Setup Mode (Instant H2 in-memory MySQL Mode)
```bash
mvn spring-boot:run
```
The application will launch on **`http://localhost:8085/`**.

### Run with Production MySQL 8.0
1. Ensure MySQL service is running on `localhost:3306`.
2. Start the application with the MySQL profile:
```bash
mvn spring-boot:run -Dspring-boot.run.profiles=mysql
```
*Note: MySQL credentials can be overridden via `MYSQL_USER` and `MYSQL_PASSWORD` environment variables or in `src/main/resources/application-mysql.properties`.*

---

## 🌐 5. Core Application Routes & Features

### 🌟 Public & SEO Delivery
- **`/`**: High-performance landing page with hero banner, live market ticker island, and instant crypto calculator.
- **`/about`**: 5-layer architecture specification, technical documentation, and author attribution.
- **`/faq`**: Frequently Asked Questions & system mechanics.
- **`/sitemap.xml` & `/robots.txt`**: Dynamic search engine crawler endpoints.

### 💼 Authenticated Vault Workflows
- **`/dashboard`**: Total portfolio net worth calculation in INR (₹) and USD ($), multi-asset balance cards, and interactive trend chart island.
- **`/wallets`**: Manage multi-chain addresses, create new currency vaults, and access simulated deposit gateway.
- **`/wallets/{id}`**: Vault detail view with interactive Canvas QR code generator and testnet faucet claim.
- **`/transactions/send`**: Instant P2P cryptographic transfer with real-time address validation, network gas calculation, and atomic execution.
- **`/transactions/receive`**: Dedicated payment receiving interface with selectable currency vaults and QR codes.
- **`/transactions/{id}/receipt`**: Printable cryptographic transaction receipt and blockchain explorer proof.
- **`/notifications`**: Real-time security alerts and transfer notifications.
- **`/profile`**: Account settings, password change with BCrypt, and active login session management.

### 🛡️ Admin Command Center
- **`/admin`**: System overview metrics (Total volume in INR, registered users, confirmed transactions).
- **`/admin/users`**: Manage platform user accounts (activate / suspend).
- **`/admin/currencies`**: Real-time exchange rate management (update INR & USD prices).
- **`/admin/transactions`**: System-wide ledger audit log.

---

## 🎨 6. Design Philosophy: Framework-Light & Device-First
- Handcrafted **Vanilla CSS** with design tokens for dark mode, glassmorphism, and responsive flex/grid layouts.
- Zero heavyweight client frameworks for core pages — fast first paint under 50ms.
- **Minimal JavaScript Islands** only where live interactivity matters (Ticker Poller, Canvas QR Generator, Chart.js trend visualizer, Real-time address validator).
