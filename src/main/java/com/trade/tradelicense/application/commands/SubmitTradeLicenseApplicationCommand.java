package com.trade.tradelicense.application.commands;

import com.trade.tradelicense.domain.enums.UserRole;

import java.util.UUID;

public record SubmitTradeLicenseApplicationCommand(UUID applicationId, UUID actorId, UserRole role) {
}
