package com.gogidix.shared.courier.ecommerce.interfaces.rest.dto;

import jakarta.validation.constraints.NotBlank;

public record HubInfoDto(
    @NotBlank String hubId,
    @NotBlank String zoneId,
    String address,
    Double latitude,
    Double longitude
) {}
