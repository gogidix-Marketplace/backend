package com.gogidix.ecommerce.customer.application.dto;

import java.time.Instant;

public record CustomerResponse(
    String id,
    String name,
    String description,
    String type,
    String email,
    String phone,
    String tier,
    Boolean isActive,
    Instant createdAt,
    Instant updatedAt
) {}
