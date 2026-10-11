package com.cryptovault.repository;

import com.cryptovault.entity.AuthSession;
import com.cryptovault.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AuthSessionRepository extends JpaRepository<AuthSession, Long> {
    List<AuthSession> findByUserOrderByLoginTimeDesc(User user);
    Optional<AuthSession> findBySessionToken(String sessionToken);
}
