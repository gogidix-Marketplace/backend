package com.gogidix.ecommerce.fulfillment.airfreight.application.dto;
import java.time.Instant;
public record AirFreightFulfillmentDto(String id, String tenantId, String name, boolean active, Instant createdAt, Instant updatedAt) {}
