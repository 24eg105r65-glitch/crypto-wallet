package com.cryptovault.service;

import com.cryptovault.entity.AuthSession;

import java.util.List;

public interface AuthSessionService {
    List<AuthSession> getUserSessions(Long userId);
    void revokeSession(Long sessionId, Long userId);
}
