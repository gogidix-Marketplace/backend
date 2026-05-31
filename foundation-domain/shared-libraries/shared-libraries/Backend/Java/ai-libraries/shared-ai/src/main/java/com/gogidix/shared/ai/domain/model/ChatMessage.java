package com.gogidix.shared.ai.domain.model;

import lombok.Builder;
import lombok.Value;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Value
@Builder
public class ChatMessage {

    @NotNull
    Role role;

    @NotBlank
    String content;

    public enum Role {
        SYSTEM,
        USER,
        ASSISTANT,
        FUNCTION
    }
}
