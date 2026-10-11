package com.cryptovault.security;

import com.cryptovault.entity.AuthSession;
import com.cryptovault.entity.User;
import com.cryptovault.repository.AuthSessionRepository;
import com.cryptovault.repository.UserRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuthenticationSuccessHandlerImpl extends SavedRequestAwareAuthenticationSuccessHandler {

    private final UserRepository userRepository;
    private final AuthSessionRepository authSessionRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        try {
            if (authentication.getPrincipal() instanceof CustomUserDetails userDetails) {
                User user = userDetails.getUser();
                user.setLastLogin(LocalDateTime.now());
                userRepository.save(user);

                String userAgent = request.getHeader("User-Agent");
                String ipAddress = request.getRemoteAddr();
                if (ipAddress == null || ipAddress.equals("0:0:0:0:0:0:0:1")) {
                    ipAddress = "127.0.0.1 (Localhost)";
                }

                String deviceType = "Desktop Browser";
                if (userAgent != null) {
                    if (userAgent.contains("Mobile") || userAgent.contains("Android") || userAgent.contains("iPhone")) {
                        deviceType = "Mobile Device";
                    } else if (userAgent.contains("Tablet") || userAgent.contains("iPad")) {
                        deviceType = "Tablet";
                    }
                }

                AuthSession session = AuthSession.builder()
                        .user(user)
                        .sessionToken(UUID.randomUUID().toString())
                        .ipAddress(ipAddress)
                        .userAgent(userAgent != null ? (userAgent.length() > 250 ? userAgent.substring(0, 250) : userAgent) : "Unknown")
                        .deviceType(deviceType)
                        .location("Secured Connection")
                        .active(true)
                        .lastActivity(LocalDateTime.now())
                        .build();

                authSessionRepository.save(session);
            }
        } catch (Exception e) {
            log.error("Failed to record login session: {}", e.getMessage());
        }

        setDefaultTargetUrl("/dashboard");
        super.onAuthenticationSuccess(request, response, authentication);
    }
}
