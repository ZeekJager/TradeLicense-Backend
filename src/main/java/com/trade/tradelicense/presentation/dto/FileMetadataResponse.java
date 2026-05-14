package com.trade.tradelicense.presentation.dto;

import com.trade.tradelicense.infrastructure.persistence.JpaStoredFileEntity;
import com.trade.tradelicense.infrastructure.persistence.StoredFileKind;

import java.time.LocalDateTime;
import java.util.UUID;

public record FileMetadataResponse(
        UUID id,
        UUID applicationId,
        UUID documentId,
        StoredFileKind kind,
        String documentType,
        String originalFileName,
        String contentType,
        long size,
        LocalDateTime uploadedAt
) {
    public static FileMetadataResponse fromEntity(JpaStoredFileEntity entity) {
        return new FileMetadataResponse(
                entity.getId(),
                entity.getApplicationId(),
                entity.getDocumentId(),
                entity.getKind(),
                entity.getDocumentType(),
                entity.getOriginalFileName(),
                entity.getContentType(),
                entity.getSize(),
                entity.getUploadedAt()
        );
    }
}
