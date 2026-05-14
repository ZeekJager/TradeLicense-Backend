package com.trade.tradelicense.presentation.dto;

import com.trade.tradelicense.domain.enums.UserRole;

public record CreateUserRequest(String fullName, String email, String password, UserRole role) {
}
