# CryptoVault — Backend Architecture

**Author:** Adabala Pushkaran (`24EG105R65`)  
**Stack:** Java 17+ (Fully compatible with Oracle JDK 25) • Spring Boot 3.3.4 • Spring Security 6 • Spring Data JPA • MySQL 8 / H2 Fallback

---

## 📁 Directory Structure

```
backend/
├── pom.xml                  # Backend Maven build configuration
└── src/
    ├── main/
    │   ├── java/
    │   │   └── com/
    │   │       └── cryptovault/
    │   │           ├── CryptoVaultApplication.java
    │   │           ├── controller/     # Spring MVC Web & REST Controllers
    │   │           ├── dto/            # Data Transfer Objects & Validation
    │   │           ├── entity/         # JPA Entities & Relational Mappings
    │   │           ├── repository/     # Spring Data Repositories
    │   │           ├── security/       # Spring Security 6 Configuration & Handlers
    │   │           └── service/        # Business Logic & Service Implementations
    │   └── resources/
    │       ├── application.properties        # Default development configuration (H2)
    │       ├── application-mysql.properties  # Production MySQL configuration
    │       ├── schema.sql                    # Normalized relational database schema
    │       ├── static/                       # Static web resources
    │       └── templates/                    # Server-rendered Thymeleaf HTML templates
    └── test/                                 # Unit & Integration tests
```

---

## ⚡ Core Features
- **Deterministic 5-Tier Architecture:** Controller ➔ Service ➔ Repository ➔ Entity ➔ Database.
- **Relational Integrity:** Fully normalized relational model for Users, Currencies, Wallets, Transactions, Audit Sessions, and Notifications.
- **Spring Security 6:** BCrypt password hashing, session concurrency tracking, role-based access control (`ROLE_USER`, `ROLE_ADMIN`), and remember-me tokens.
- **JDK 25 Ready:** Standardized explicit constructors, builder patterns, and SLF4J loggers, eliminating compiler crashes with Lombok on modern JDKs.
- **Dual Database Profiles:** Fast local development with H2 in MySQL compatibility mode, plus seamless switch to external MySQL server.
