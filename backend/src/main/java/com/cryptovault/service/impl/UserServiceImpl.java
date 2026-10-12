package com.cryptovault.service.impl;

import com.cryptovault.dto.PasswordChangeDto;
import com.cryptovault.dto.UserRegisterDto;
import com.cryptovault.dto.UserUpdateDto;
import com.cryptovault.entity.*;
import com.cryptovault.repository.CurrencyRepository;
import com.cryptovault.repository.NotificationRepository;
import com.cryptovault.repository.UserRepository;
import com.cryptovault.repository.WalletRepository;
import com.cryptovault.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

    private final UserRepository userRepository;
    private final CurrencyRepository currencyRepository;
    private final WalletRepository walletRepository;
    private final NotificationRepository notificationRepository;
    private final PasswordEncoder passwordEncoder;
    private final SecureRandom secureRandom = new SecureRandom();

    public UserServiceImpl(UserRepository userRepository,
                           CurrencyRepository currencyRepository,
                           WalletRepository walletRepository,
                           NotificationRepository notificationRepository,
                           PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.currencyRepository = currencyRepository;
        this.walletRepository = walletRepository;
        this.notificationRepository = notificationRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public User registerUser(UserRegisterDto registerDto) {
        if (userRepository.existsByUsername(registerDto.getUsername())) {
            throw new IllegalArgumentException("Username is already taken");
        }
        if (userRepository.existsByEmail(registerDto.getEmail())) {
            throw new IllegalArgumentException("Email address is already registered");
        }
        if (!registerDto.getPassword().equals(registerDto.getConfirmPassword())) {
            throw new IllegalArgumentException("Passwords do not match");
        }

        User user = User.builder()
                .username(registerDto.getUsername().toLowerCase().trim())
                .email(registerDto.getEmail().toLowerCase().trim())
                .fullName(registerDto.getFullName().trim())
                .phoneNumber(registerDto.getPhoneNumber())
                .password(passwordEncoder.encode(registerDto.getPassword()))
                .role(Role.ROLE_USER)
                .enabled(true)
                .accountNonLocked(true)
                .build();

        User savedUser = userRepository.save(user);

        // Provision default starter wallets for BTC, ETH, and USDT
        List<Currency> defaultCurrencies = currencyRepository.findAllByActiveTrueOrderByCodeAsc();
        for (Currency currency : defaultCurrencies) {
            if ("BTC".equalsIgnoreCase(currency.getCode()) ||
                "ETH".equalsIgnoreCase(currency.getCode()) ||
                "USDT".equalsIgnoreCase(currency.getCode())) {
                
                String address = generateCryptoAddress(currency.getCode());
                Wallet wallet = Wallet.builder()
                        .user(savedUser)
                        .currency(currency)
                        .walletAddress(address)
                        .walletName(savedUser.getFullName() + "'s " + currency.getCode() + " Vault")
                        .balance(BigDecimal.ZERO)
                        .active(true)
                        .build();
                walletRepository.save(wallet);
            }
        }

        // Welcome notification
        Notification welcomeNotice = Notification.builder()
                .user(savedUser)
                .title("Welcome to CryptoVault!")
                .message("Your secure multi-currency cryptocurrency vault has been created. You can now deposit test crypto, send, and receive assets with zero latency.")
                .type(NotificationType.SYSTEM)
                .read(false)
                .referenceUrl("/wallets")
                .build();
        notificationRepository.save(welcomeNotice);

        log.info("Successfully registered new user: {}", savedUser.getUsername());
        return savedUser;
    }

    @Override
    @Transactional
    public User updateProfile(Long userId, UserUpdateDto updateDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + userId));

        // Check email uniqueness if modified
        if (!user.getEmail().equalsIgnoreCase(updateDto.getEmail()) && userRepository.existsByEmail(updateDto.getEmail())) {
            throw new IllegalArgumentException("Email is already used by another account");
        }

        user.setFullName(updateDto.getFullName().trim());
        user.setEmail(updateDto.getEmail().toLowerCase().trim());
        user.setPhoneNumber(updateDto.getPhoneNumber());

        return userRepository.save(user);
    }

    @Override
    @Transactional
    public boolean changePassword(Long userId, PasswordChangeDto passwordDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + userId));

        if (!passwordEncoder.matches(passwordDto.getCurrentPassword(), user.getPassword())) {
            throw new IllegalArgumentException("Current password is incorrect");
        }

        if (!passwordDto.getNewPassword().equals(passwordDto.getConfirmNewPassword())) {
            throw new IllegalArgumentException("New passwords do not match");
        }

        user.setPassword(passwordEncoder.encode(passwordDto.getNewPassword()));
        userRepository.save(user);

        // Security Notification
        Notification securityNotice = Notification.builder()
                .user(user)
                .title("Password Changed Successfully")
                .message("Your CryptoVault security password was updated on " + LocalDateTime.now() + ". If you did not perform this action, contact support immediately.")
                .type(NotificationType.SECURITY_ALERT)
                .read(false)
                .referenceUrl("/profile")
                .build();
        notificationRepository.save(securityNotice);

        return true;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    @Transactional
    public User toggleUserActive(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + userId));
        user.setEnabled(!user.isEnabled());
        return userRepository.save(user);
    }

    @Override
    @Transactional(readOnly = true)
    public long countTotalUsers() {
        return userRepository.count();
    }

    private String generateCryptoAddress(String currencyCode) {
        StringBuilder sb = new StringBuilder();
        if ("BTC".equalsIgnoreCase(currencyCode)) {
            sb.append("bc1q");
        } else if ("ETH".equalsIgnoreCase(currencyCode) || "USDT".equalsIgnoreCase(currencyCode)) {
            sb.append("0x");
        } else if ("SOL".equalsIgnoreCase(currencyCode)) {
            sb.append("sol");
        } else {
            sb.append("0x");
        }

        String hexChars = "0123456789abcdefABCDEF";
        for (int i = 0; i < 38; i++) {
            sb.append(hexChars.charAt(secureRandom.nextInt(hexChars.length())));
        }
        return sb.toString();
    }
}
