package com.trade.tradelicense.infrastructure.services;

import com.trade.tradelicense.domain.valueobjects.TradeLicenseType;
import com.trade.tradelicense.infrastructure.persistence.JpaLicenseTypeConfigEntity;
import com.trade.tradelicense.infrastructure.repositories.SpringDataLicenseTypeConfigRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class LicenseTypeConfigService {
    private final SpringDataLicenseTypeConfigRepository repository;

    public LicenseTypeConfigService(SpringDataLicenseTypeConfigRepository repository) {
        this.repository = repository;
    }

    @PostConstruct
    public void seedDefaults() {
        for (TradeLicenseType licenseType : TradeLicenseType.supportedTypes()) {
            seedLicenseType(licenseType);
        }
    }

    private void seedLicenseType(TradeLicenseType licenseType) {
        if (!repository.existsByCodeIgnoreCase(licenseType.code())) {
            repository.save(new JpaLicenseTypeConfigEntity(
                    UUID.randomUUID(),
                    licenseType.code(),
                    licenseType.name(),
                    List.of(
                            "TIN Certificate",
                            "Bank Letter Supporting Capital of the Business",
                            "Formation Letter from Government Office"
                    ),
                    true,
                    LocalDateTime.now()
            ));
        }
    }

    public List<JpaLicenseTypeConfigEntity> list(boolean activeOnly) {
        return activeOnly ? repository.findByActiveTrueOrderByNameAsc() : repository.findAll();
    }

    public JpaLicenseTypeConfigEntity create(String code, String name, List<String> requiredDocuments) {
        requireText(code, "License type code is required");
        requireText(name, "License type name is required");
        if (repository.existsByCodeIgnoreCase(code.trim())) {
            throw new IllegalArgumentException("License type code already exists");
        }
        return repository.save(new JpaLicenseTypeConfigEntity(
                UUID.randomUUID(),
                code.trim(),
                name.trim(),
                requiredDocuments,
                true,
                LocalDateTime.now()
        ));
    }

    public JpaLicenseTypeConfigEntity update(UUID id, String name, List<String> requiredDocuments, boolean active) {
        JpaLicenseTypeConfigEntity entity = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("License type config not found"));
        requireText(name, "License type name is required");
        entity.update(name.trim(), requiredDocuments, active);
        return repository.save(entity);
    }

    private void requireText(String value, String message) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(message);
        }
    }
}
