package com.trade.tradelicense.domain.entities;

import com.trade.tradelicense.domain.enums.DocumentStatus;
import com.trade.tradelicense.domain.valueobjects.DocumentId;
import com.trade.tradelicense.domain.valueobjects.DocumentReference;
import com.trade.tradelicense.domain.valueobjects.DocumentType;

import java.time.LocalDateTime;
import java.util.Objects;

public class ApplicationDocument {
    private final DocumentId id;
    private final DocumentType documentType;
    private final DocumentReference documentReference;
    private DocumentStatus status;
    private final LocalDateTime uploadedAt;

    public ApplicationDocument(DocumentId id, DocumentType documentType, DocumentReference documentReference, DocumentStatus status, LocalDateTime uploadedAt) {
        this.id = Objects.requireNonNull(id, "Document id is required");
        this.documentType = Objects.requireNonNull(documentType, "Document type is required");
        this.documentReference = Objects.requireNonNull(documentReference, "Document reference is required");
        this.status = Objects.requireNonNull(status, "Document status is required");
        this.uploadedAt = Objects.requireNonNull(uploadedAt, "Uploaded date is required");
    }

    public static ApplicationDocument upload(DocumentType documentType, DocumentReference documentReference) {
        return new ApplicationDocument(DocumentId.newId(), documentType, documentReference, DocumentStatus.UPLOADED, LocalDateTime.now());
    }

    public void verify() {
        this.status = DocumentStatus.VERIFIED;
    }

    public void reject() {
        this.status = DocumentStatus.REJECTED;
    }

    public DocumentId id() {
        return id;
    }

    public DocumentType documentType() {
        return documentType;
    }

    public DocumentReference documentReference() {
        return documentReference;
    }

    public DocumentStatus status() {
        return status;
    }

    public LocalDateTime uploadedAt() {
        return uploadedAt;
    }
}
