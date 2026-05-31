package com.gogidix.shared.ai.domain.model;

import lombok.Builder;
import lombok.Value;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.List;

@Value
@Builder
public class EmbeddingRequest {

    @NotNull
    List<@NotBlank String> texts;

    @Builder.Default
    String model = "text-embedding-3-small";

    @Builder.Default
    int dimensions = 1536;
}
