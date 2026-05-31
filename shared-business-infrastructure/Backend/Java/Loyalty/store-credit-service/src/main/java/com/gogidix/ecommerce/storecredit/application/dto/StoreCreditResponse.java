package com.gogidix.ecommerce.storecredit.application.dto;

import java.time.Instant;

public record StoreCreditResponse(
    String id,
    String name,
    String description,
    String type,
    java.math.BigDecimal creditAmount,
    java.math.BigDecimal remainingBalance,
    String reason,
    Boolean isActive,
    Instant createdAt,
    Instant updatedAt
) {}
