package com.trade.tradelicense.infrastructure.repositories;

import com.trade.tradelicense.infrastructure.persistence.JpaApplicationAuditEventEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface SpringDataApplicationAuditEventRepository extends JpaRepository<JpaApplicationAuditEventEntity, UUID> {
    List<JpaApplicationAuditEventEntity> findByApplicationIdOrderByOccurredAtAsc(UUID applicationId);
}
