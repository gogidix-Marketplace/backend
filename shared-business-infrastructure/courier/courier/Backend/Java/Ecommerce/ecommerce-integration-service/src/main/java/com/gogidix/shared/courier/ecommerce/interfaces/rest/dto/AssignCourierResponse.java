package com.gogidix.shared.courier.ecommerce.interfaces.rest.dto;

import java.time.Instant;

public record AssignCourierResponse(
    String assignmentId,
    String courierId,
    String courierName,
    String courierPhone,
    String vehicleType,
    Instant estimatedPickupTime,
    Instant estimatedDeliveryTime,
    String trackingId,
    String status
) {}
