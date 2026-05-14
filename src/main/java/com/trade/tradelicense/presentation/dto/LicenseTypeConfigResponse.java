package com.trade.tradelicense.presentation.dto;

import com.trade.tradelicense.infrastructure.persistence.JpaLicenseTypeConfigEntity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record LicenseTypeConfigResponse(
        UUID id,
        String code,
        String name,
        List<String> requiredDocuments,
        boolean active,
        LocalDateTime updatedAt
) {
    public static LicenseTypeConfigResponse fromEntity(JpaLicenseTypeConfigEntity entity) {
        return new LicenseTypeConfigResponse(
                entity.getId(),
                entity.getCode(),
                entity.getName(),
                entity.getRequiredDocuments(),
                entity.isActive(),
                entity.getUpdatedAt()
        );
    }
}
