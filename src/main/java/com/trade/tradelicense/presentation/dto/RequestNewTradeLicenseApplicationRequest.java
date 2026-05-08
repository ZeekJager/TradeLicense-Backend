package com.trade.tradelicense.presentation.dto;

import java.util.UUID;

public record RequestNewTradeLicenseApplicationRequest(
        UUID applicantId,
        String fullName,
        String nationalIdNumber,
        String email,
        String phoneNumber,
        String tradeLicenseType,
        String commodity
) {
}
