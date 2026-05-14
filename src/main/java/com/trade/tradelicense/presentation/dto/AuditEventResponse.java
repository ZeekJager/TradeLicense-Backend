package com.trade.tradelicense.presentation.dto;

import com.trade.tradelicense.domain.enums.ApplicationStatus;
import com.trade.tradelicense.domain.enums.UserRole;
import com.trade.tradelicense.infrastructure.persistence.JpaApplicationAuditEventEntity;

import java.time.LocalDateTime;
import java.util.UUID;

public record AuditEventResponse(
        UUID id,
        UUID applicationId,
        String action,
        ApplicationStatus status,
        UUID actorId,
        UserRole actorRole,
        String comment,
        LocalDateTime occurredAt
) {
    public static AuditEventResponse fromEntity(JpaApplicationAuditEventEntity entity) {
        return new AuditEventResponse(
                entity.getId(),
                entity.getApplicationId(),
                entity.getAction(),
                entity.getStatus(),
                entity.getActorId(),
                entity.getActorRole(),
                entity.getComment(),
                entity.getOccurredAt()
        );
    }
}
