package com.trade.tradelicense.presentation.dto;

import com.trade.tradelicense.domain.enums.UserRole;
import com.trade.tradelicense.infrastructure.persistence.JpaUserAccountEntity;

import java.util.UUID;

public record CurrentUserResponse(
        UUID id,
        String fullName,
        String tinNumber,
        String email,
        String region,
        String businessAddress,
        String legalCondition,
        String bankAccountNumber,
        UserRole role
) {
    public static CurrentUserResponse fromEntity(JpaUserAccountEntity entity) {
        return new CurrentUserResponse(
                entity.getId(),
                entity.getFullName(),
                entity.getTinNumber(),
                entity.getEmail(),
                entity.getRegion(),
                entity.getBusinessAddress(),
                entity.getLegalCondition(),
                entity.getBankAccountNumber(),
                entity.getRole()
        );
    }
}
