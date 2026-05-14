package com.trade.tradelicense.presentation.dto;

import com.trade.tradelicense.domain.enums.ApplicationStatus;

public record ApplicationStatusSummaryResponse(ApplicationStatus status, long count) {
}
