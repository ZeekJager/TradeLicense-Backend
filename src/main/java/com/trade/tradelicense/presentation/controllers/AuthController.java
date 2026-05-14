package com.trade.tradelicense.presentation.controllers;

import com.trade.tradelicense.infrastructure.security.AuthService;
import com.trade.tradelicense.infrastructure.security.AuthenticatedUser;
import com.trade.tradelicense.infrastructure.security.TokenSessionService;
import com.trade.tradelicense.presentation.dto.AuthResponse;
import com.trade.tradelicense.presentation.dto.CurrentUserResponse;
import com.trade.tradelicense.presentation.dto.LoginRequest;
import com.trade.tradelicense.presentation.dto.RegisterRequest;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private static final String BEARER_PREFIX = "Bearer ";

    private final AuthService authService;
    private final TokenSessionService tokenSessionService;

    public AuthController(AuthService authService, TokenSessionService tokenSessionService) {
        this.authService = authService;
        this.tokenSessionService = tokenSessionService;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest request) {
        return ResponseEntity.ok(authService.register(request));
    }

    @GetMapping("/me")
    public ResponseEntity<CurrentUserResponse> me(@AuthenticationPrincipal AuthenticatedUser user) {
        return ResponseEntity.ok(authService.me(user));
    }

    @PostMapping("/logout")
    public ResponseEntity<Map<String, String>> logout(HttpServletRequest request) {
        String authorization = request.getHeader("Authorization");
        if (authorization != null && authorization.startsWith(BEARER_PREFIX)) {
            tokenSessionService.revoke(authorization.substring(BEARER_PREFIX.length()));
        }
        return ResponseEntity.ok(Map.of("message", "Logged out"));
    }
}
