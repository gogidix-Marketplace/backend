package com.gogidix.shared.ai.domain.model;

import lombok.Builder;
import lombok.Value;

import jakarta.validation.constraints.NotBlank;
import java.util.Map;

@Value
@Builder
public class PromptTemplate {

    @NotBlank
    String name;

    @NotBlank
    String template;

    @Builder.Default
    Map<String, String> defaultValues = Map.of();

    @Builder.Default
    int maxTokens = 4096;

    @Builder.Default
    double temperature = 0.7;

    public String resolve(Map<String, String> variables) {
        String resolved = template;
        Map<String, String> merged = new java.util.HashMap<>(defaultValues);
        merged.putAll(variables);
        for (Map.Entry<String, String> entry : merged.entrySet()) {
            resolved = resolved.replace("{{" + entry.getKey() + "}}", entry.getValue());
        }
        return resolved;
    }
}
