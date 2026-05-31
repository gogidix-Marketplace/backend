package com.gogidix.shared.courier.ecommerce.interfaces.rest.dto;

import jakarta.validation.constraints.NotBlank;
import java.time.Instant;

public record ZoneInfoDto(
    @NotBlank String zoneId,
    String address,
    Double latitude,
    Double longitude,
    String contactName,
    String contactPhone
) {}
