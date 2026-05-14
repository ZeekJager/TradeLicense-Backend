package com.trade.tradelicense.infrastructure.repositories;

import com.trade.tradelicense.infrastructure.persistence.JpaStoredFileEntity;
import com.trade.tradelicense.infrastructure.persistence.StoredFileKind;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface SpringDataStoredFileRepository extends JpaRepository<JpaStoredFileEntity, UUID> {
    List<JpaStoredFileEntity> findByApplicationIdAndKindOrderByUploadedAtDesc(UUID applicationId, StoredFileKind kind);

    Optional<JpaStoredFileEntity> findFirstByApplicationIdAndKindOrderByUploadedAtDesc(UUID applicationId, StoredFileKind kind);
}
