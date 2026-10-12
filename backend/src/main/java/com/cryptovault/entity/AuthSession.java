package com.cryptovault.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "auth_sessions")
public class AuthSession {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "session_token", length = 100)
    private String sessionToken;

    @Column(name = "ip_address", length = 50)
    private String ipAddress;

    @Column(name = "user_agent", length = 255)
    private String userAgent;

    @Column(name = "device_type", length = 50)
    private String deviceType;

    @Column(name = "location", length = 100)
    private String location;

    @Column(name = "is_active", nullable = false)
    private boolean active = true;

    @CreationTimestamp
    @Column(name = "login_time", updatable = false)
    private LocalDateTime loginTime;

    @Column(name = "last_activity")
    private LocalDateTime lastActivity;

    public AuthSession() {}

    public AuthSession(Long id, User user, String sessionToken, String ipAddress, String userAgent, String deviceType, String location, boolean active, LocalDateTime loginTime, LocalDateTime lastActivity) {
        this.id = id;
        this.user = user;
        this.sessionToken = sessionToken;
        this.ipAddress = ipAddress;
        this.userAgent = userAgent;
        this.deviceType = deviceType;
        this.location = location;
        this.active = active;
        this.loginTime = loginTime;
        this.lastActivity = lastActivity;
    }

    public static AuthSessionBuilder builder() {
        return new AuthSessionBuilder();
    }

    public static class AuthSessionBuilder {
        private Long id;
        private User user;
        private String sessionToken;
        private String ipAddress;
        private String userAgent;
        private String deviceType;
        private String location;
        private boolean active = true;
        private LocalDateTime loginTime;
        private LocalDateTime lastActivity;

        public AuthSessionBuilder id(Long id) { this.id = id; return this; }
        public AuthSessionBuilder user(User user) { this.user = user; return this; }
        public AuthSessionBuilder sessionToken(String sessionToken) { this.sessionToken = sessionToken; return this; }
        public AuthSessionBuilder ipAddress(String ipAddress) { this.ipAddress = ipAddress; return this; }
        public AuthSessionBuilder userAgent(String userAgent) { this.userAgent = userAgent; return this; }
        public AuthSessionBuilder deviceType(String deviceType) { this.deviceType = deviceType; return this; }
        public AuthSessionBuilder location(String location) { this.location = location; return this; }
        public AuthSessionBuilder active(boolean active) { this.active = active; return this; }
        public AuthSessionBuilder loginTime(LocalDateTime loginTime) { this.loginTime = loginTime; return this; }
        public AuthSessionBuilder lastActivity(LocalDateTime lastActivity) { this.lastActivity = lastActivity; return this; }

        public AuthSession build() {
            return new AuthSession(id, user, sessionToken, ipAddress, userAgent, deviceType, location, active, loginTime, lastActivity);
        }
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    public String getSessionToken() { return sessionToken; }
    public void setSessionToken(String sessionToken) { this.sessionToken = sessionToken; }
    public String getIpAddress() { return ipAddress; }
    public void setIpAddress(String ipAddress) { this.ipAddress = ipAddress; }
    public String getUserAgent() { return userAgent; }
    public void setUserAgent(String userAgent) { this.userAgent = userAgent; }
    public String getDeviceType() { return deviceType; }
    public void setDeviceType(String deviceType) { this.deviceType = deviceType; }
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
    public LocalDateTime getLoginTime() { return loginTime; }
    public void setLoginTime(LocalDateTime loginTime) { this.loginTime = loginTime; }
    public LocalDateTime getLastActivity() { return lastActivity; }
    public void setLastActivity(LocalDateTime lastActivity) { this.lastActivity = lastActivity; }
}
