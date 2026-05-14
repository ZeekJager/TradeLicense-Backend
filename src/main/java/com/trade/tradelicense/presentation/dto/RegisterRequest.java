package com.trade.tradelicense.presentation.dto;

public record RegisterRequest(
        String tinNumber,
        String fullName,
        String email,
        String region,
        String businessAddress,
        String password,
        String legalCondition,
        String bankAccountNumber
) {
}
