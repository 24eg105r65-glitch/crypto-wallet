# CryptoVault — Server-Rendered Full-Stack Cryptocurrency Wallet Platform

**Author:** Adabala Pushkaran • **Roll No:** 24EG105R65  
**Working Stack:** Maven Multi-Module • Spring Boot 3.3.4 • Thymeleaf • MySQL 8.0 / H2 • Spring Security 6 • Vanilla CSS Design System • Modern JS

### 🔗 Repositories
- **Backend Repository:** [https://github.com/24eg105r65-glitch/crypto-wallet](https://github.com/24eg105r65-glitch/crypto-wallet)
- **Frontend Repository:** [https://github.com/24eg105r65-glitch/crypto-wallet-frontend](https://github.com/24eg105r65-glitch/crypto-wallet-frontend)

---

## 📂 Project Architecture: Separated Backend & Frontend

The codebase is organized into cleanly segregated **`backend/`** and **`frontend/`** directory structures:

```
TT project/
├── pom.xml                  # Parent Aggregator POM
├── backend/                 # Backend Java Spring Boot Service
│   ├── pom.xml              # Spring Boot Maven POM
│   ├── README.md            # Backend Architecture Guide
│   └── src/
│       ├── main/
│       │   ├── java/        # Controllers, Services, Entities, DTOs, Security
│       │   └── resources/   # Configuration, schema.sql, templates & static
│       └── test/            # Integration & unit test suite
├── frontend/                # Frontend UI, Templates, CSS & JS
│   ├── package.json         # Frontend metadata & scripts
│   ├── README.md            # Frontend Design System Guide
│   ├── css/                 # Vanilla CSS stylesheets (design system tokens)
│   ├── js/                  # Client-side JavaScript islands & live ticker polling
│   └── templates/           # Semantic Thymeleaf view templates
│       ├── admin/           # Administrative command views
│       ├── auth/            # Sign in & Registration
│       ├── dashboard/       # Portfolio metrics & overview
│       ├── fragments/       # Common reusable navigation & layout
│       ├── notifications/   # In-app notifications
│       ├── profile/         # Profile & security session manager
│       ├── transactions/    # Ledger, Send crypto, Receive QR & Receipts
│       └── wallets/         # Multi-asset vault management
```

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
- **Maven:** 3.8+ (or bundled wrapper)

### Build
From the project root:
```bash
mvn clean compile
```

### Run
Launch the application:
```bash
mvn --projects backend spring-boot:run
```
Or navigate into `backend/` and run:
```bash
cd backend
mvn spring-boot:run
```

Access the application in your browser:
- **Web App URL:** [http://localhost:8085](http://localhost:8085)
- **H2 Database Console:** [http://localhost:8085/h2-console](http://localhost:8085/h2-console) (JDBC URL: `jdbc:h2:mem:cryptovaultdb`, User: `sa`, Password: *empty*)
