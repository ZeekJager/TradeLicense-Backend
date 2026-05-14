package com.trade.tradelicense.infrastructure.persistence;

import com.trade.tradelicense.domain.enums.ApplicationStatus;
import com.trade.tradelicense.domain.enums.UserRole;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "application_audit_events")
public class JpaApplicationAuditEventEntity {
    @Id
    private UUID id;
    private UUID applicationId;
    private String action;

    @Enumerated(EnumType.STRING)
    private ApplicationStatus status;

    private UUID actorId;

    @Enumerated(EnumType.STRING)
    private UserRole actorRole;

    private String comment;
    private LocalDateTime occurredAt;

    protected JpaApplicationAuditEventEntity() {
    }

    public JpaApplicationAuditEventEntity(
            UUID id,
            UUID applicationId,
            String action,
            ApplicationStatus status,
            UUID actorId,
            UserRole actorRole,
            String comment,
            LocalDateTime occurredAt
    ) {
        this.id = id;
        this.applicationId = applicationId;
        this.action = action;
        this.status = status;
        this.actorId = actorId;
        this.actorRole = actorRole;
        this.comment = comment;
        this.occurredAt = occurredAt;
    }

    public UUID getId() {
        return id;
    }

    public UUID getApplicationId() {
        return applicationId;
    }

    public String getAction() {
        return action;
    }

    public ApplicationStatus getStatus() {
        return status;
    }

    public UUID getActorId() {
        return actorId;
    }

    public UserRole getActorRole() {
        return actorRole;
    }

    public String getComment() {
        return comment;
    }

    public LocalDateTime getOccurredAt() {
        return occurredAt;
    }
}
