package com.trade.tradelicense.application.commands;

import java.util.UUID;

public record RequestNewTradeLicenseApplicationCommand(
        UUID applicantId,
        String fullName,
        String nationalIdNumber,
        String email,
        String phoneNumber,
        String tradeLicenseType,
        String commodity
) {
}
