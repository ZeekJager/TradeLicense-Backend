package com.trade.tradelicense.infrastructure.persistence;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "license_type_configs")
public class JpaLicenseTypeConfigEntity {
    @Id
    private UUID id;

    @Column(unique = true, nullable = false)
    private String code;

    private String name;
    private String requiredDocuments;
    private boolean active;
    private LocalDateTime updatedAt;

    protected JpaLicenseTypeConfigEntity() {
    }

    public JpaLicenseTypeConfigEntity(
            UUID id,
            String code,
            String name,
            List<String> requiredDocuments,
            boolean active,
            LocalDateTime updatedAt
    ) {
        this.id = id;
        this.code = code;
        this.name = name;
        this.requiredDocuments = String.join(",", requiredDocuments == null ? List.of() : requiredDocuments);
        this.active = active;
        this.updatedAt = updatedAt;
    }

    public UUID getId() {
        return id;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public List<String> getRequiredDocuments() {
        if (requiredDocuments == null || requiredDocuments.isBlank()) {
            return List.of();
        }
        return Arrays.stream(requiredDocuments.split(","))
                .map(String::trim)
                .filter(value -> !value.isBlank())
                .toList();
    }

    public boolean isActive() {
        return active;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void update(String name, List<String> requiredDocuments, boolean active) {
        this.name = name;
        this.requiredDocuments = String.join(",", requiredDocuments == null ? List.of() : requiredDocuments);
        this.active = active;
        this.updatedAt = LocalDateTime.now();
    }
}
