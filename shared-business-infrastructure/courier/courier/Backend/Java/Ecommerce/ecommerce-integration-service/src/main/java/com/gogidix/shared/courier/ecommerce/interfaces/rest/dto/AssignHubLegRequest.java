package com.gogidix.shared.courier.ecommerce.interfaces.rest.dto;

import jakarta.validation.constraints.NotBlank;
import java.time.Instant;
import java.util.List;

public record AssignHubLegRequest(
    @NotBlank String orderId,
    @NotBlank String subOrderId,
    @NotBlank String leg,
    HubInfoDto originHub,
    HubInfoDto destinationHub,
    CustomerDeliveryDto customerDelivery,
    List<PackageDto> packages,
    Instant scheduledFor
) {}
