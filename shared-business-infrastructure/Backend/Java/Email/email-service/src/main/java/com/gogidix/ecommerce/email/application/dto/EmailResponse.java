package com.gogidix.ecommerce.email.application.dto;

import java.time.LocalDateTime;

public record EmailResponse(
    String id,
    String name,
    String description,
    String type,
    boolean isActive,
    LocalDateTime createdAt,
    LocalDateTime updatedAt,
    String recipient, String subject, String bodyTemplate, String status
) {
}