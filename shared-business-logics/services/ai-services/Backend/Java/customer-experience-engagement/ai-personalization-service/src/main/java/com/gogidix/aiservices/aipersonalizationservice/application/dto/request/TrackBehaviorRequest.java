package com.gogidix.aiservices.aipersonalizationservice.application.dto.request;

import com.gogidix.aiservices.aipersonalizationservice.domain.model.EventType;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Value;

import java.time.Instant;
import java.util.List;
import java.util.Map;

@Value
@Builder
public class TrackBehaviorRequest {
    @NotNull(message = "User ID cannot be null")
    String userId;

    @NotBlank(message = "Session ID cannot be blank")
    String sessionId;

    @NotEmpty(message = "Events cannot be empty")
    @Size(max = 1000, message = "Events cannot exceed 1000")
    @Valid
    List<BehaviorEventRequest> events;

    @Value
    @Builder
    public static class BehaviorEventRequest {
        @NotNull(message = "Event type cannot be null")
        EventType eventType;

        @NotBlank(message = "Item ID cannot be blank")
        String itemId;

        @NotNull(message = "Timestamp cannot be null")
        Instant timestamp;

        @Builder.Default
        Map<String, Object> properties = Map.of();
    }
}
