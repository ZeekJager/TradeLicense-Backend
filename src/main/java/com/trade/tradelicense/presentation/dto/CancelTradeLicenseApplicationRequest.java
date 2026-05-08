package com.trade.tradelicense.presentation.dto;

import com.trade.tradelicense.domain.enums.UserRole;

import java.util.UUID;

public record CancelTradeLicenseApplicationRequest(UUID actorId, UserRole role) {
}
