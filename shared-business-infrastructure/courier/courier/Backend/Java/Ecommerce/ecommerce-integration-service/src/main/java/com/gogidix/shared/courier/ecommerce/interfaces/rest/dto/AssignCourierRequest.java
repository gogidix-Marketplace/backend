package com.gogidix.shared.courier.ecommerce.interfaces.rest.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.Instant;
import java.util.List;

public record AssignCourierRequest(
    @NotBlank String orderId,
    @NotBlank String subOrderId,
    @NotBlank String vendorId,
    @NotBlank String deliveryType,
    ZoneInfoDto pickupZone,
    ZoneInfoDto deliveryZone,
    List<PackageDto> packages,
    String priority,
    Instant scheduledFor,
    boolean requiresHubProcessing,
    HubDetailsDto hubDetails
) {}
