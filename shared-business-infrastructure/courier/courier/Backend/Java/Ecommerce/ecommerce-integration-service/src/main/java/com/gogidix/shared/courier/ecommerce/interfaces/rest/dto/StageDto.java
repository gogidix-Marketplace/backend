package com.gogidix.shared.courier.ecommerce.interfaces.rest.dto;

import java.time.Instant;

public record StageDto(
    String stage,
    String status,
    String courierName,
    String courierPhone,
    String hubName,
    Instant completedAt,
    Instant arrivedAt
) {}
