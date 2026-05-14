package com.trade.tradelicense.infrastructure.security;

import com.trade.tradelicense.domain.enums.UserRole;
import com.trade.tradelicense.infrastructure.persistence.JpaUserAccountEntity;
import com.trade.tradelicense.infrastructure.repositories.SpringDataUserAccountRepository;
import com.trade.tradelicense.presentation.dto.AuthResponse;
import com.trade.tradelicense.presentation.dto.CurrentUserResponse;
import com.trade.tradelicense.presentation.dto.LoginRequest;
import com.trade.tradelicense.presentation.dto.RegisterRequest;
import jakarta.annotation.PostConstruct;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
public class AuthService {
    private final SpringDataUserAccountRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final TokenSessionService tokenSessionService;

    public AuthService(
            SpringDataUserAccountRepository repository,
            PasswordEncoder passwordEncoder,
            TokenSessionService tokenSessionService
    ) {
        this.repository = Objects.requireNonNull(repository, "User account repository is required");
        this.passwordEncoder = Objects.requireNonNull(passwordEncoder, "Password encoder is required");
        this.tokenSessionService = Objects.requireNonNull(tokenSessionService, "Token session service is required");
    }

    @PostConstruct
    public void seedUsers() {
        seedUser("Admin User", "admin@tradelicense.test", "password", UserRole.ADMIN);
        seedUser("Customer User", "customer@tradelicense.test", "password", UserRole.CUSTOMER);
        seedUser("Reviewer User", "reviewer@tradelicense.test", "password", UserRole.REVIEWER);
        seedUser("Approver User", "approver@tradelicense.test", "password", UserRole.APPROVER);
    }

    public AuthResponse login(LoginRequest request) {
        if (request == null || request.email() == null || request.email().isBlank()
                || request.password() == null || request.password().isBlank()) {
            throw new BadCredentialsException("Email and password are required");
        }
        JpaUserAccountEntity user = repository.findByEmailIgnoreCase(request.email().trim())
                .filter(JpaUserAccountEntity::isActive)
                .orElseThrow(() -> new BadCredentialsException("Invalid email or password"));
        if (!passwordEncoder.matches(request.password(), user.getPasswordHash())) {
            throw new BadCredentialsException("Invalid email or password");
        }
        return new AuthResponse(tokenSessionService.issue(user), CurrentUserResponse.fromEntity(user));
    }

    public CurrentUserResponse me(AuthenticatedUser user) {
        return repository.findById(user.id())
                .map(CurrentUserResponse::fromEntity)
                .orElseGet(() -> new CurrentUserResponse(user.id(), user.fullName(), null, user.email(), null, null, null, null, user.role()));
    }

    public AuthResponse register(RegisterRequest request) {
        validateRegisterRequest(request);
        String email = request.email().trim().toLowerCase();
        String tinNumber = request.tinNumber().trim();
        String bankAccountNumber = request.bankAccountNumber().trim();
        if (repository.existsByEmailIgnoreCase(email)) {
            throw new IllegalArgumentException("Email already exists");
        }
        if (repository.existsByTinNumber(tinNumber)) {
            throw new IllegalArgumentException("TIN number already exists");
        }
        if (repository.existsByBankAccountNumber(bankAccountNumber)) {
            throw new IllegalArgumentException("Bank account number already exists");
        }

        JpaUserAccountEntity user = repository.save(new JpaUserAccountEntity(
                UUID.randomUUID(),
                request.fullName().trim(),
                tinNumber,
                email,
                request.region().trim(),
                request.businessAddress().trim(),
                request.legalCondition().trim(),
                bankAccountNumber,
                passwordEncoder.encode(request.password()),
                UserRole.CUSTOMER,
                true,
                LocalDateTime.now()
        ));
        return new AuthResponse(tokenSessionService.issue(user), CurrentUserResponse.fromEntity(user));
    }

    public List<CurrentUserResponse> listUsers() {
        return repository.findAll().stream()
                .map(CurrentUserResponse::fromEntity)
                .toList();
    }

    public CurrentUserResponse createUser(String fullName, String email, String password, UserRole role) {
        requireText(fullName, "Full name is required");
        requireText(email, "Email is required");
        requireText(password, "Password is required");
        Objects.requireNonNull(role, "Role is required");
        if (repository.existsByEmailIgnoreCase(email.trim())) {
            throw new IllegalArgumentException("Email already exists");
        }
        JpaUserAccountEntity user = repository.save(new JpaUserAccountEntity(
                UUID.randomUUID(),
                fullName.trim(),
                email.trim().toLowerCase(),
                passwordEncoder.encode(password),
                role,
                true,
                LocalDateTime.now()
        ));
        return CurrentUserResponse.fromEntity(user);
    }

    public CurrentUserResponse updateRole(UUID userId, UserRole role) {
        Objects.requireNonNull(userId, "User id is required");
        Objects.requireNonNull(role, "Role is required");
        JpaUserAccountEntity user = repository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        user.updateRole(role);
        return CurrentUserResponse.fromEntity(repository.save(user));
    }

    private void seedUser(String fullName, String email, String password, UserRole role) {
        if (repository.existsByEmailIgnoreCase(email)) {
            return;
        }
        repository.save(new JpaUserAccountEntity(
                UUID.randomUUID(),
                fullName,
                email,
                passwordEncoder.encode(password),
                role,
                true,
                LocalDateTime.now()
        ));
    }

    private void requireText(String value, String message) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(message);
        }
    }

    private void validateRegisterRequest(RegisterRequest request) {
        Objects.requireNonNull(request, "Registration request is required");
        requireText(request.tinNumber(), "TIN number is required");
        requireText(request.fullName(), "Full name is required");
        requireText(request.email(), "Email is required");
        requireText(request.region(), "Region is required");
        requireText(request.businessAddress(), "Business address is required");
        requireText(request.password(), "Password is required");
        requireText(request.legalCondition(), "Legal condition is required");
        requireText(request.bankAccountNumber(), "Bank account number is required");
    }
}
