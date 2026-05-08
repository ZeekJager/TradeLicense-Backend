package com.trade.tradelicense.presentation.dto;

import com.trade.tradelicense.domain.enums.UserRole;

import java.util.UUID;

public record SubmitTradeLicenseApplicationRequest(UUID actorId, UserRole role) {
}
