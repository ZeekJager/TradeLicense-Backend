package com.trade.tradelicense.infrastructure.repositories;

import com.trade.tradelicense.infrastructure.persistence.JpaLicenseTypeConfigEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface SpringDataLicenseTypeConfigRepository extends JpaRepository<JpaLicenseTypeConfigEntity, UUID> {
    List<JpaLicenseTypeConfigEntity> findByActiveTrueOrderByNameAsc();

    Optional<JpaLicenseTypeConfigEntity> findByCodeIgnoreCase(String code);

    boolean existsByCodeIgnoreCase(String code);
}
