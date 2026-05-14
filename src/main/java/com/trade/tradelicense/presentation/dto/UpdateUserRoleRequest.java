package com.trade.tradelicense.presentation.dto;

import com.trade.tradelicense.domain.enums.UserRole;

public record UpdateUserRoleRequest(UserRole role) {
}
