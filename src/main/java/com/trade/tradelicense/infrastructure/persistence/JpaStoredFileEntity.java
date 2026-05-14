package com.trade.tradelicense.infrastructure.persistence;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "stored_files")
public class JpaStoredFileEntity {
    @Id
    private UUID id;
    private UUID applicationId;
    private UUID documentId;

    @Enumerated(EnumType.STRING)
    private StoredFileKind kind;

    private String documentType;
    private String originalFileName;
    private String storedFileName;
    private String contentType;
    private long size;
    private LocalDateTime uploadedAt;

    protected JpaStoredFileEntity() {
    }

    public JpaStoredFileEntity(
            UUID id,
            UUID applicationId,
            UUID documentId,
            StoredFileKind kind,
            String documentType,
            String originalFileName,
            String storedFileName,
            String contentType,
            long size,
            LocalDateTime uploadedAt
    ) {
        this.id = id;
        this.applicationId = applicationId;
        this.documentId = documentId;
        this.kind = kind;
        this.documentType = documentType;
        this.originalFileName = originalFileName;
        this.storedFileName = storedFileName;
        this.contentType = contentType;
        this.size = size;
        this.uploadedAt = uploadedAt;
    }

    public UUID getId() {
        return id;
    }

    public UUID getApplicationId() {
        return applicationId;
    }

    public UUID getDocumentId() {
        return documentId;
    }

    public StoredFileKind getKind() {
        return kind;
    }

    public String getDocumentType() {
        return documentType;
    }

    public String getOriginalFileName() {
        return originalFileName;
    }

    public String getStoredFileName() {
        return storedFileName;
    }

    public String getContentType() {
        return contentType;
    }

    public long getSize() {
        return size;
    }

    public LocalDateTime getUploadedAt() {
        return uploadedAt;
    }
}
