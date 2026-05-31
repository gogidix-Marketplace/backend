package com.gogidix.shared.courier.ecommerce.interfaces.rest.dto;

public record HubDetailsDto(
    String originHubId,
    String originHubAddress,
    Double originHubLatitude,
    Double originHubLongitude,
    String destinationHubId,
    String destinationHubAddress,
    Double destinationHubLatitude,
    Double destinationHubLongitude
) {}
