package com.gogidix.ecommerce.tracking.application.dto;

import java.time.Instant;

public record TrackingUpdateDto(
    String status, String location, String description, Instant timestamp
) {}
