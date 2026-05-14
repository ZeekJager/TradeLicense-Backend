package com.trade.tradelicense.infrastructure.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.trade.tradelicense.infrastructure.persistence.JpaTradeLicenseEntity;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface SpringDataTradeLicenseRepository extends JpaRepository<JpaTradeLicenseEntity, UUID> {
    Optional<JpaTradeLicenseEntity> findBySourceApplicationId(UUID sourceApplicationId);

    List<JpaTradeLicenseEntity> findByApplicantId(UUID applicantId);

    boolean existsByLicenseNumber(String licenseNumber);

    boolean existsByTinNumber(String tinNumber);
}
