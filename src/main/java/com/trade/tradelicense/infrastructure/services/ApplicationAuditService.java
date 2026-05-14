package com.trade.tradelicense.infrastructure.services;

import com.trade.tradelicense.domain.enums.ApplicationStatus;
import com.trade.tradelicense.domain.enums.UserRole;
import com.trade.tradelicense.infrastructure.persistence.JpaApplicationAuditEventEntity;
import com.trade.tradelicense.infrastructure.repositories.SpringDataApplicationAuditEventRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class ApplicationAuditService {
    private final SpringDataApplicationAuditEventRepository repository;

    public ApplicationAuditService(SpringDataApplicationAuditEventRepository repository) {
        this.repository = repository;
    }

    public void record(
            UUID applicationId,
            String action,
            ApplicationStatus status,
            UUID actorId,
            UserRole actorRole,
            String comment
    ) {
        repository.save(new JpaApplicationAuditEventEntity(
                UUID.randomUUID(),
                applicationId,
                action,
                status,
                actorId,
                actorRole,
                comment,
                LocalDateTime.now()
        ));
    }

    public List<JpaApplicationAuditEventEntity> timeline(UUID applicationId) {
        return repository.findByApplicationIdOrderByOccurredAtAsc(applicationId);
    }
}
