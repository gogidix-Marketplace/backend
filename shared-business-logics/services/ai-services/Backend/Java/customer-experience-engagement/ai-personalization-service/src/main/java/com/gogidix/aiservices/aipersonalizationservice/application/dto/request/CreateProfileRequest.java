package com.gogidix.aiservices.aipersonalizationservice.application.dto.request;

import com.gogidix.aiservices.aipersonalizationservice.domain.model.UserAttributes;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class CreateProfileRequest {
    @NotNull(message = "User ID cannot be null")
    String userId;

    UserAttributes attributes;
}
