package com.trade.tradelicense.infrastructure.security;

import com.trade.tradelicense.infrastructure.persistence.JpaUserAccountEntity;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.Instant;
import java.util.Base64;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class TokenSessionService {
    private static final long SESSION_TTL_SECONDS = 8L * 60L * 60L;

    private final SecureRandom secureRandom = new SecureRandom();
    private final Map<String, SessionRecord> sessions = new ConcurrentHashMap<>();

    public String issue(JpaUserAccountEntity user) {
        byte[] bytes = new byte[48];
        secureRandom.nextBytes(bytes);
        String token = Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
        AuthenticatedUser authenticatedUser = new AuthenticatedUser(
                user.getId(),
                user.getFullName(),
                user.getEmail(),
                user.getRole()
        );
        sessions.put(token, new SessionRecord(authenticatedUser, Instant.now().plusSeconds(SESSION_TTL_SECONDS)));
        return token;
    }

    public Optional<AuthenticatedUser> authenticate(String token) {
        if (token == null || token.isBlank()) {
            return Optional.empty();
        }
        SessionRecord session = sessions.get(token);
        if (session == null) {
            return Optional.empty();
        }
        if (session.expiresAt().isBefore(Instant.now())) {
            sessions.remove(token);
            return Optional.empty();
        }
        return Optional.of(session.user());
    }

    public void revoke(String token) {
        if (token != null) {
            sessions.remove(token);
        }
    }

    private record SessionRecord(AuthenticatedUser user, Instant expiresAt) {
    }
}
