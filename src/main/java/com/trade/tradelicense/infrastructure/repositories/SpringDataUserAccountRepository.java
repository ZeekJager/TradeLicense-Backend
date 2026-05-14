package com.trade.tradelicense.infrastructure.repositories;

import com.trade.tradelicense.domain.enums.UserRole;
import com.trade.tradelicense.infrastructure.persistence.JpaUserAccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface SpringDataUserAccountRepository extends JpaRepository<JpaUserAccountEntity, UUID> {
    Optional<JpaUserAccountEntity> findByEmailIgnoreCase(String email);

    boolean existsByEmailIgnoreCase(String email);

    boolean existsByTinNumber(String tinNumber);

    boolean existsByBankAccountNumber(String bankAccountNumber);

    List<JpaUserAccountEntity> findByRole(UserRole role);
}
