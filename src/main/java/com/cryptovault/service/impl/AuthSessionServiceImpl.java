package com.cryptovault.service.impl;

import com.cryptovault.entity.AuthSession;
import com.cryptovault.entity.User;
import com.cryptovault.repository.AuthSessionRepository;
import com.cryptovault.repository.UserRepository;
import com.cryptovault.service.AuthSessionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthSessionServiceImpl implements AuthSessionService {

    private final AuthSessionRepository authSessionRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public List<AuthSession> getUserSessions(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        return authSessionRepository.findByUserOrderByLoginTimeDesc(user);
    }

    @Override
    @Transactional
    public void revokeSession(Long sessionId, Long userId) {
        AuthSession session = authSessionRepository.findById(sessionId)
                .orElseThrow(() -> new IllegalArgumentException("Session not found"));
        if (session.getUser().getId().equals(userId)) {
            session.setActive(false);
            authSessionRepository.save(session);
        }
    }
}
