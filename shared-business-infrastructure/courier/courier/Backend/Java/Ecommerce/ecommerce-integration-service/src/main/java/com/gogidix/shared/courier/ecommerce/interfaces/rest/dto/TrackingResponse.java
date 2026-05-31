package com.gogidix.shared.courier.ecommerce.interfaces.rest.dto;

import java.time.Instant;
import java.util.List;

public record TrackingResponse(
    String orderId,
    String subOrderId,
    String deliveryType,
    String currentStage,
    List<StageDto> stages,
    Instant eta
) {}
