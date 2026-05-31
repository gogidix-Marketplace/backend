package com.gogidix.aiservices.aipersonalizationservice.application.dto.request;

import com.gogidix.aiservices.aipersonalizationservice.domain.model.UserAttributes;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Value;

@Value
@Builder(toBuilder = true)
public class UpdateAttributesRequest {
    @NotNull(message = "User ID cannot be null")
    String userId;

    @NotNull(message = "Attributes cannot be null")
    UserAttributes attributes;
}
