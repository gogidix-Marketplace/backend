package com.gogidix.shared.ai.domain.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TokenUsageTest {

    @Test
    void of_createsTokenUsageWithCorrectTotals() {
        TokenUsage usage = TokenUsage.of(100, 50);
        assertEquals(100, usage.getPromptTokens());
        assertEquals(50, usage.getCompletionTokens());
        assertEquals(150, usage.getTotalTokens());
    }

    @Test
    void estimatedCost_gpt4() {
        TokenUsage usage = TokenUsage.of(1000, 500);
        double cost = usage.estimatedCost(AiModelType.GPT_4);
        assertTrue(cost > 0);
        assertEquals(0.06, cost, 0.001);
    }

    @Test
    void estimatedCost_gpt35Turbo() {
        TokenUsage usage = TokenUsage.of(1000, 500);
        double cost = usage.estimatedCost(AiModelType.GPT_35_TURBO);
        assertTrue(cost > 0);
        assertTrue(cost < 0.01);
    }

    @Test
    void estimatedCost_custom_returnsZero() {
        TokenUsage usage = TokenUsage.of(1000, 500);
        double cost = usage.estimatedCost(AiModelType.CUSTOM);
        assertEquals(0.0, cost);
    }

    @Test
    void estimatedCost_claude3Opus() {
        TokenUsage usage = TokenUsage.of(1000, 500);
        double cost = usage.estimatedCost(AiModelType.CLAUDE_3_OPUS);
        assertTrue(cost > 0);
    }
}
