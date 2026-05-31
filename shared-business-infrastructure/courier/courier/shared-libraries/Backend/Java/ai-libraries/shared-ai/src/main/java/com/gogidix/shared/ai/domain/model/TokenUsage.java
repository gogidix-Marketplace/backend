package com.gogidix.shared.ai.domain.model;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class TokenUsage {

    int promptTokens;
    int completionTokens;
    int totalTokens;

    public static TokenUsage of(int promptTokens, int completionTokens) {
        return TokenUsage.builder()
                .promptTokens(promptTokens)
                .completionTokens(completionTokens)
                .totalTokens(promptTokens + completionTokens)
                .build();
    }

    public double estimatedCost(AiModelType model) {
        double inputCostPer1k = switch (model) {
            case GPT_4 -> 0.03;
            case GPT_4_TURBO -> 0.01;
            case GPT_35_TURBO -> 0.0005;
            case CLAUDE_3_OPUS -> 0.015;
            case CLAU_3_SONNET -> 0.003;
            case CLAUDE_3_HAIKU -> 0.00025;
            case CUSTOM -> 0.0;
        };
        double outputCostPer1k = inputCostPer1k * 2;
        return (promptTokens * inputCostPer1k / 1000.0) + (completionTokens * outputCostPer1k / 1000.0);
    }
}
