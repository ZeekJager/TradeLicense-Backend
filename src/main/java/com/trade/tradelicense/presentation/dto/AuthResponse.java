package com.trade.tradelicense.presentation.dto;

public record AuthResponse(String token, CurrentUserResponse user) {
}
