package com.trade.tradelicense.infrastructure.security;

import com.trade.tradelicense.domain.enums.UserRole;

import java.util.UUID;

public record AuthenticatedUser(UUID id, String fullName, String email, UserRole role) {
}
