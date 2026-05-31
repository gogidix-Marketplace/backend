package com.gogidix.shared.courier.ecommerce.interfaces.rest.dto;

import java.util.Map;

public record CourierAvailabilityResponse(
    String zoneId,
    int availableCouriers,
    Map<String, Integer> vehicleTypes,
    String averageETA,
    boolean surgePricing
) {}
