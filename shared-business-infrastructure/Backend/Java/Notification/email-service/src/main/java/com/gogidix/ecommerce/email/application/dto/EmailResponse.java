package com.gogidix.ecommerce.email.application.dto;

import java.time.Instant;

public record EmailResponse(
    String id,
    String name,
    String description,
    String type,
    String recipient,
    String subject,
    String status,
    Boolean isActive,
    Instant createdAt,
    Instant updatedAt
) {}
